package sheettastic.sheets;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;
import sheettastic.templates.Capture;
import sheettastic.templates.Template;

import java.util.*;

/**
 * Created by Dave on 21/10/2017.
 */
@Document
@Data
public class Sheet {

    private Template template; //could be a ref rather than concrete

    @Id
    private String id;
    private Integer headerRowIndex;

    @Version
    private Long version;

    @NonNull
    private List<Row> rows = new LinkedList<>();

    private List<Capture> mappings = new ArrayList<>();


    public Row addRow(String[] row) {
        return this.addRow(new Row(row));

    }

    public Row addRow(List<String> row) {
        return this.addRow(new Row(row));
    }

    public Row addRow(Row row){
        this.rows.add(row);
        return row;
    }

    public void deleteColumn(int columnIndex) {
        rows
                .stream()
                .forEach(row -> row.deleteCellByColumnIndex(columnIndex));
    }

    public void deleteColumnsPastIndex(int lastIndexToKeep) {
        rows
                .stream()
                .forEach(row -> row.deleteCellsPastColumnIndex(lastIndexToKeep));
    }

    public Optional<Integer> indexOfLastNonEmptyColumn() {
        Optional<Integer> lastNonEmptyColumn = rows
                .stream()
                .map(Row::columnIndexOflastNonEmptyCell)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(Comparator.naturalOrder());

        return lastNonEmptyColumn;
    }

    public void removeEmptyRows() {
        ListIterator<Row> rowIterator = rows.listIterator();

        while (rowIterator.hasNext()) {
            Row currentRow = rowIterator.next();
            Optional<Integer> lastNonEmptyIndex = currentRow.columnIndexOflastNonEmptyCell();

            if (!lastNonEmptyIndex.isPresent()) {
                rowIterator.remove();
            }
        }
    }

    public void removeColumnsPastLastNonEmpty() {
        Optional<Integer> lastNonEmptyColumn = indexOfLastNonEmptyColumn();

        if (lastNonEmptyColumn.isPresent()){
            this.deleteColumnsPastIndex( lastNonEmptyColumn.get() );
        } else {
            this.rows.clear();
        }
    }



}
