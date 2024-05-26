package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.BookMark;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    Optional<BookMark> findByHospitalAndUser(Hospital hospital, User user);

    Page<BookMark> findAllByUser(User user, Pageable pageable);

    void delete(BookMark bookmark);
}
