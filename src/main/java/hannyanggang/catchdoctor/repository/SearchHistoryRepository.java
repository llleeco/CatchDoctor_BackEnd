package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.SearchHistory;
import hannyanggang.catchdoctor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory,Long> {
    Optional<SearchHistory> findByUser(User user);

    SearchHistory findByKeywordAndUser(String keyword,User user);
}
