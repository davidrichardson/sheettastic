package sheettastic.templates;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.json.JSONObject;

/**
 * Created by Dave on 18/10/2017.
 */
@Builder
@Data
public class SingleColumnCapture implements Capture {

    @NonNull
    private String displayName;

    @NonNull
    private String fieldName;

    @NonNull
    @Builder.Default
    private JsonType type = JsonType.String;

    @Builder.Default
    private boolean required = false;

    @Override
    public int capture(int position, String[] headers, String[] values, JSONObject document){

        String value = values[position];

        type.addValueToDocument(fieldName,value,document);

        return ++position;
    }





}
