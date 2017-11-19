package sheettastic.samples;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Attribute {
    private String value;
    private String units;
    private List<Term> terms = new ArrayList<>();
}
