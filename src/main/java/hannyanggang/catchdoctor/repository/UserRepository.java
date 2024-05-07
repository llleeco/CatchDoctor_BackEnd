package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(String id);
    boolean existsByName(String name);
    User findById(String id);

    @Query("SELECT u.id FROM User u WHERE u.userid = ?1")
    String findUsernameByUserId(String userId);

}
