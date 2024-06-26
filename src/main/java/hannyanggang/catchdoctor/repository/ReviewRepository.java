package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    // @EntityGraph : 엔티티의 특정한 속성을 같이 로딩하도록 표시하는 어노테이션
    // -> 특정 기능을 수행할 때만 EAGER 로딩을 하도록 지정할 수 있다
    @EntityGraph(attributePaths = {"user"},type = EntityGraph.EntityGraphType.FETCH) // Review 처리시 @EntityGraph 적용해 Member도 같이 로딩
    List<Review> findByHospital(Hospital hospital);

}
