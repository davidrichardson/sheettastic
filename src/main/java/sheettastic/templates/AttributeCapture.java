package sheettastic.templates;

import lombok.Builder;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.List;
import java.util.ListIterator;


@Data
@Builder(toBuilder = true)
public class AttributeCapture implements Capture {

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    private static final String ATTRIBUTES_FIELD_NAME = "attributes";
    private static final String TERMS_FIELD_NAME = "terms";

    private String displayName;

    @Builder.Default
    private boolean required = false;

    @Builder.Default
    private boolean allowUnits = true;

    @Builder.Default
    private boolean allowTerms = true;

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position,captures,headers.get(position));

        position++;
        ListIterator<String> headerIterator = headers.listIterator(position);

        while(headerIterator.hasNext()){
            position = headerIterator.nextIndex();
            String header = headerIterator.next().trim().toLowerCase();

            if (allowUnits && header.equals("units")){
                //next
            }
            else if (allowTerms && header.equals("term")){
                //next
            }
            else{
                return position;
            }
        }

        return ++position;
    }

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        JSONArray attributes = ensureArray(document, ATTRIBUTES_FIELD_NAME);

        JSONObject attribute = new JSONObject();
        attributes.put(attribute);

        String value = values.get(position);

        attribute.put("name",this.getDisplayName());
        attribute.put("value",value);

        return parseDependentColumns(position,headers,values,attribute);
    }


    private int parseDependentColumns(int position, List<String> headers, List<String> values, JSONObject attribute){
        position++;

        while(position < headers.size()){
            String header = headers.get(position).trim().toLowerCase();
            String value = values.get(position);

            System.out.println(MessageFormat.format("pos {0} h {1} v {2}",position,header,value));

            if (header.equals( "units")){
                attribute.put("units",value);
            }
            else if (header.equals("term")){
                JSONArray terms = ensureArray(attribute, TERMS_FIELD_NAME);
                JSONObject term = new JSONObject();
                term.put("url",value);
                terms.put(term);
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
