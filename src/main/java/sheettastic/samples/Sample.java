package sheettastic.samples;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
@Data
public class Sample implements Identifiable<String>{

    private String id;

    private String alias;
    private Map<String,List<Attribute>> attributes = new HashMap<>();
    private Long taxonId;
    private String taxon;

}


