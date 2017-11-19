package sheettastic.templates;

import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class NoOpCapture implements Capture {

    private String displayName;

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        return ++position;
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {
        return 0;
    }

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String displayName) {

    }

    @Override
    public List<String> expectedColumnHeaders() {
        return Collections.emptyList();
    }
}
