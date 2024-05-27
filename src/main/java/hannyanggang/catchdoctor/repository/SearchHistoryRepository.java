package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.SearchHistory;
import hannyanggang.catchdoctor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory,Long> {
    List<SearchHistory> findByUserOrderBySearchDateDescSearchTimeDesc(User user);
    SearchHistory findByUserAndKeyword(User user,String keyword);
    SearchHistory findByKeywordAndUser(String keyword,User user);
}
