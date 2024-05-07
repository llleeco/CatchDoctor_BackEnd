package hannyanggang.catchdoctor.repository;


import hannyanggang.catchdoctor.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}
