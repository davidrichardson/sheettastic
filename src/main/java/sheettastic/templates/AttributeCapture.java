package sheettastic.templates;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;


@Data
@Builder
public class AttributeCapture implements Capture {

    private String columnName;

    @NonNull
    @Builder.Default
    private String attributesFieldName = "attributes";

    @Builder.Default
    private String termsFieldName = "attributes";

    @Override
    public int capture(int position, String[] headers, String[] values, JSONObject document) {

        JSONArray attributes = ensureArray(document,attributesFieldName);

        JSONObject attribute = new JSONObject();
        attributes.put(attribute);

        String header = headers[position];
        String value = values[position];

        attribute.put("name",header);
        attribute.put("value",value);

        return parseDependentColumns(position,headers,values,attribute);
    }

    private int parseDependentColumns(int position, String[] headers, String[] values, JSONObject attribute){
        position++;

        while(position < headers.length){
            String header = headers[position];
            String value = values[position];

            System.out.println(MessageFormat.format("pos {0} h {1} v {2}",position,header,value));

            if (header.equals( "units")){
                attribute.put("units",value);
            }
            else if (header.equals("term")){
                JSONArray terms = ensureArray(attribute,termsFieldName);
                JSONObject term = new JSONObject();
                term.put("url",value);
            }
            else {
                return position;
            }

            position++;
        }

        return position;
    }



    private JSONArray ensureArray(JSONObject document,String arrayFieldName) {

        if (!document.has(arrayFieldName)){
            document.put(arrayFieldName, new JSONArray());
        }

        return document.getJSONArray(arrayFieldName);

    }


}
