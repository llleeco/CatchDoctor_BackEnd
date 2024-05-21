package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.Board;
import hannyanggang.catchdoctor.entity.BoardLike;
import hannyanggang.catchdoctor.entity.BookMark;
import hannyanggang.catchdoctor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {
    Optional<BoardLike> findByBoardAndUser(Board board, User user);
}
