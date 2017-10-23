package sheettastic.templates;

import lombok.Data;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dave on 18/10/2017.
 */
@Data
public class Parser {

    private Map<String, Capture> columnCaptures = new LinkedHashMap<>();
    private Capture defaultCapture;

    public Parser add(SingleColumnCapture capture){
        columnCaptures.put(capture.getDisplayName(), capture);
        return this;
    }

    public JSONObject parser(String[] headers, String[] values){
        JSONObject document = new JSONObject();

        int position = 0;

        while (position < headers.length){

            String currentHeader = headers[position];

            System.out.println(currentHeader+ " "+ position);

            if (columnCaptures.containsKey(currentHeader)){
                Capture capture = columnCaptures.get(currentHeader);
                position = capture.capture(position,headers,values,document);
            }
            else if (defaultCapture != null){
                position = defaultCapture.capture(position,headers,values,document);
            }
            else {
                position++;
            }

        }

        return document;
    }

}
