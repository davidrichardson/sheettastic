package sheettastic.templates;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.*;

@Data
@Builder(toBuilder = true)
public class Template {

    @NonNull
    private String name;

    private String targetType;

    @Builder.Default
    private Map<String, Capture> columnCaptures = new CaseInsensitiveMap();

    private Capture defaultCapture;

    public Template add(String columnName, Capture capture) {
        columnCaptures.put(columnName, capture);
        return this;
    }

    private static class CaseInsensitiveMap extends LinkedHashMap<String, Capture> {

        public Capture get(Object key) {
            return super.get(mungeKey(key));
        }

        public Capture put(String key, Capture value) {
            return super.put(mungeKey(key), value);
        }

        @Override
        public boolean containsKey(Object key) {
            return super.containsKey(mungeKey(key));
        }

        private static String mungeKey(Object key) {
            return key.toString().toLowerCase();
        }


    }
}
