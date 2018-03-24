package neu.csye6225.dao;

import neu.csye6225.entity.Picture;
import neu.csye6225.entity.UserInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called IPictureRepository.
// CRUD refers Create, Read, Update, Delete.
@Profile("production")
public interface IUserInfoRepository extends CrudRepository<Picture, String> {

    // By extending CrudRepository, UserRepository inherits several methods for working with User persistence,
    // including methods for saving, deleting, and finding User entities.
    UserInfo findByUsername(String username);
}
