package hannyanggang.catchdoctor.repository.hospitalRepository;

import hannyanggang.catchdoctor.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, String>, HospitalRepositoryCustom {

    Optional<Hospital> findByHospitalid(Long hospitalid);

    Page<Hospital> searchWithDynamicQuery(String query, Pageable pageable);
    Page<Hospital> findByDepartment(String department, Pageable pageable);


    //Optional<Hospital> findByBusinessId(String businessId);

    @Query("SELECT h.name FROM Hospital h WHERE h.hospitalid = :hospitalid")
    String findNameByHospitalid(@Param("hospitalid") Long hospitalid);
}
