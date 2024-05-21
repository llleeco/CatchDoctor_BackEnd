package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    Optional<BoardImage> findByName(String fileName);
}
