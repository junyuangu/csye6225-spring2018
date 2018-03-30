package neu.csye6225.dao;

import neu.csye6225.entity.Description;
import org.springframework.data.repository.CrudRepository;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
// This will be AUTO IMPLEMENTED by Spring into a Bean called IDescriptionRepository.
// CRUD refers Create, Read, Update, Delete.
public interface IDescriptionRepository extends CrudRepository<Description, String>{
}
