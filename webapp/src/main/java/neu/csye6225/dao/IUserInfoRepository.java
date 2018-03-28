package neu.csye6225.dao;

import neu.csye6225.entity.UserInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called IPictureRepository.
// CRUD refers Create, Read, Update, Delete.
@Profile("aws")
public interface IUserInfoRepository extends CrudRepository<UserInfo, String> {

}
