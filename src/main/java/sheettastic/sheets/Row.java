package sheettastic.sheets;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dave on 21/10/2017.
 */
@Data
public class Row {
    private List<String> cells;

    public Row() {cells = new ArrayList<>();}

    public Row(String[] s) {
        this.cells = Arrays.asList(s);
    }

    public Row(List<String> cells) {
        this.cells = cells;
    }
}
