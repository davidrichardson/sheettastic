package sheettastic.samples;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SampleRepo extends MongoRepository<Sample,String>{
}
