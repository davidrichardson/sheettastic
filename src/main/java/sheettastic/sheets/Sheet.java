package sheettastic.sheets;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dave on 21/10/2017.
 */
@Document
@Data
public class Sheet {

    @Id
    private UUID id = UUID.randomUUID();

    @Version
    private Long version;

    private List<Row> rows = new ArrayList<>();

    public void addRow(String[] row) {
        rows.add(
                new Row(row)
        );
    }
    public void addRow(List<String> row) {
        rows.add(
                new Row(row)
        );
    }


}
