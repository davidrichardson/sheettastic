package sheettastic.templates;

import org.json.JSONObject;

/**
 * Created by Dave on 18/10/2017.
 */
public interface Capture {
    int capture(int position, String[] headers, String[] values, JSONObject document);
    String getDisplayName();
    boolean isRequired();

}
