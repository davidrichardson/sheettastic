package sheettastic.sheets;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

/**
 * Created by Dave on 21/10/2017.
 */
@RepositoryRestResource
public interface SheetRepository extends MongoRepository<Sheet,String> {
}
