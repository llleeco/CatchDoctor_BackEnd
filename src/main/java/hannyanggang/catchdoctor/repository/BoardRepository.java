package hannyanggang.catchdoctor.repository;


import hannyanggang.catchdoctor.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Override
    Optional<Board> findById(Long id);
    List<Board> findAllByOrderByRegDateDescRegTimeDesc(); // 내림차순 정렬
}
