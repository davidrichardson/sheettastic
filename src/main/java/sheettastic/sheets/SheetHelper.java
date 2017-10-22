package sheettastic.sheets;

import org.springframework.stereotype.Component;

import java.util.ListIterator;
import java.util.Optional;

@Component
public class SheetHelper {

    public void beforeCreate(Sheet sheet) {
        sheet.fillInId();

        sheet.removeEmptyRows();
        sheet.removeColumnsPastLastNonEmpty();

        this.guessHeader(sheet);



    }

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

    public void mapHeadings(Sheet sheet, Object template){

    }

}
