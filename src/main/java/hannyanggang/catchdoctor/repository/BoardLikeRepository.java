package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.Board;
import hannyanggang.catchdoctor.entity.BoardLike;
import hannyanggang.catchdoctor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {
    Optional<BoardLike> findByBoardAndUser(Board board, User user);

    @Query("SELECT bl FROM BoardLike bl WHERE bl.board.id = :boardid")
    List<BoardLike> findByBoardId(Long boardid);
}
