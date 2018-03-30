package neu.csye6225.dao;

import neu.csye6225.entity.Picture;
import org.springframework.data.repository.CrudRepository;


/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
// This will be AUTO IMPLEMENTED by Spring into a Bean called IPictureRepository.
// CRUD refers Create, Read, Update, Delete.
public interface IPictureRepository extends CrudRepository<Picture, String> {
}
