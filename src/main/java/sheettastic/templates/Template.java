package sheettastic.templates;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class Template {

    @NonNull
    private String targetType;

    private List<Capture> columnCaptures = new ArrayList<>();

    private Capture defaultCapture;

    public Template add(Capture capture) {
        columnCaptures.add(capture);
        return this;
    }

    public Map<String,Capture> capturesByColumnName(){
        Map<String,Capture> capturesByColumnName = new LinkedHashMap<>();

        columnCaptures
                .forEach(capture -> capturesByColumnName.put(
                        capture.getDisplayName().trim().toLowerCase(),
                        capture)
                );

        return capturesByColumnName;
    }
}
