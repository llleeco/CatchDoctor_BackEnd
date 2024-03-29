package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(String id);
    boolean existsByName(String name);
    User findById(String id);

}
