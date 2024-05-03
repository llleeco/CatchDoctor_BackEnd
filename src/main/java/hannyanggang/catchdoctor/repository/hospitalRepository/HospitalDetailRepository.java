package hannyanggang.catchdoctor.repository.hospitalRepository;

import hannyanggang.catchdoctor.entity.HospitalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalDetailRepository extends JpaRepository<HospitalDetail,Long> {
}
