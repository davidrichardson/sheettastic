package sheettastic.sheets;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sheettastic.templates.Capture;

import java.util.*;

@Component
public class SheetHelper {

    public void beforeCreate(Sheet sheet) {
        this.fillInId(sheet);

        Assert.notNull(sheet.getTemplate());

        sheet.removeEmptyRows();
        sheet.removeColumnsPastLastNonEmpty();

        this.guessHeader(sheet);

        this.mapHeadings(sheet);

    }

    public void fillInId(Sheet sheet) {
        Assert.isNull(sheet.getId());
        sheet.setId( UUID.randomUUID().toString() );
    }

    /**
     * Picks a header row in the sheet
     * @param sheet
     */
    public void guessHeader(Sheet sheet) {
        sheet.setHeaderRowIndex(null);

        ListIterator<Row> rowIterator = sheet.getRows().listIterator();

        while(rowIterator.hasNext()){
            int rowIndex = rowIterator.nextIndex();
            Row row = rowIterator.next();

            Optional<Integer> r = row.columnIndexOflastNonEmptyCell();

            if (r.isPresent()) {
                sheet.setHeaderRowIndex(rowIndex);
                return;
            }
        }
    }

    /**
     * Must have data and a valid row picked
     * @param sheet
     */
    public void mapHeadings(Sheet sheet){
        Map<String,Capture> columnCaptures = sheet.getTemplate().capturesByColumnName();
        Optional<Capture> defaultCapture = Optional.of(sheet.getTemplate().getDefaultCapture());

        if (sheet.getHeaderRowIndex() == null ) return;

        List<String> headerRow = sheet.getRows().get( sheet.getHeaderRowIndex() ).getCells();

        Capture[] emptyCaptures = new Capture[headerRow.size()];

        List<Capture> capturePositions = new ArrayList<>(Arrays.asList(emptyCaptures));


        int position = 0;

        while (position < headerRow.size()){

            String currentHeader = headerRow.get(position);
            currentHeader = currentHeader.trim().toLowerCase();


            if (columnCaptures.containsKey(currentHeader)){
                Capture capture = columnCaptures.get(currentHeader);


                position = capture.map(position,capturePositions,headerRow);
            }
            else if (defaultCapture.isPresent()){
                position = defaultCapture.get().map(position,capturePositions,headerRow);
            }
            else {
                position++;
            }

        }

        sheet.setMappings(capturePositions);
    }

    public List<JSONObject> parse(Sheet sheet){

        List<String> headers = sheet.getRows().get( sheet.getHeaderRowIndex() ).getCells();
        ListIterator<Row> rowIterator = sheet.getRows().listIterator( sheet.getHeaderRowIndex() + 1 );

        ListIterator<Capture> captureIterator = sheet.getMappings().listIterator();

        List<JSONObject> documents = new ArrayList<>();

        while(rowIterator.hasNext()){
            JSONObject document = new JSONObject();
            documents.add(document);

            Row row = rowIterator.next();
            List<String> cells = row.getCells();

            while(captureIterator.hasNext()){
                int position = captureIterator.nextIndex();
                Capture capture = captureIterator.next();

                if (capture != null) {
                    capture.capture(position, headers, cells, document);
                }
            }
        }

        return documents;
    }

}
