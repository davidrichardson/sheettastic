package sheettastic.sheets;

import lombok.Data;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by Dave on 21/10/2017.
 */
@Data
public class Row {
    @NonNull
    private List<String> cells;


    public Row() {
        cells = new LinkedList<>();
    }

    public Row(String[] s) {
        this.cells = new LinkedList<>(Arrays.asList(s));
    }

    public Row(List<String> cells) {
        Assert.notNull(cells);
        this.cells = new LinkedList<>(cells);
    }

    public void deleteCellByColumnIndex(int columnIndex) {
        if (cells.size() > columnIndex) {
            cells.remove(columnIndex);
        }
    }

    public void deleteCellsPastColumnIndex(int lastIndexToKeep) {
        if (lastIndexToKeep < 0 || lastIndexToKeep > cells.size() - 1){
            return;
        }

        ListIterator<String> cellsIterator = cells.listIterator(lastIndexToKeep + 1);

        while (cellsIterator.hasNext()) {
            cellsIterator.next();
            cellsIterator.remove();
        }
    }

    /**
     * @return column index of the right most cell that is not null or empty
     */
    public Optional<Integer> columnIndexOflastNonEmptyCell() {
        ListIterator<String> cellsIterator = cells.listIterator(cells.size());

        while (cellsIterator.hasPrevious()) {
            int currentIndex = cellsIterator.previousIndex();
            String cellContents = cellsIterator.previous();

            boolean emptyCell = (
                    cellContents == null ||
                            cellContents.isEmpty() ||
                            cellContents.trim().isEmpty()
            );

            if (!emptyCell) {
                return Optional.of(currentIndex);
            }
        }

        return Optional.empty();
    }
}
