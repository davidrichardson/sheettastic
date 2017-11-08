package sheettastic.templates;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Dave on 18/10/2017.
 */
@Builder(toBuilder = true)
@Data
public class FieldCapture implements Capture {

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    @NonNull
    private String fieldName;

    @Builder.Default
    private JsonFieldType fieldType = JsonFieldType.String;

    @Builder.Default
    private boolean required = false;

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        String value = values.get(position);

        fieldType.addValueToDocument(fieldName,value,document);

        return ++position;
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position,captures,headers.get(position));

        return ++position;
    }
}
