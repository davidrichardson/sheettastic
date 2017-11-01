package sheettastic.sheets;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.rest.core.config.Projection;
import sheettastic.templates.Capture;
import java.util.List;

@Projection(name = "truncatedColumns", types = {Sheet.class})
public interface SheetTruncatedColumnsProjection {



    @Value("#{target.firstRows}")
    List<Row> getFirstRows();

    List<Capture> getMappings();

}
