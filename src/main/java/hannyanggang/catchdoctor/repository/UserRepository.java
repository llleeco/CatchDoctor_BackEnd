package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String id);
}
