package hannyanggang.catchdoctor.repository.hospitalRepository;

import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.OpenApiHospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OpenApiRepository extends JpaRepository<OpenApiHospital, Long>, OpenApiRepositoryCustom  {
    Page<OpenApiHospital> searchWithDynamicQuery(String query, Pageable pageable);

    @Query("SELECT o FROM OpenApiHospital o WHERE o.addnum = ?1 AND o.hospitalname = ?2")
    OpenApiHospital findByAddressAndHospitalName(String addnum, String hospitalName);

    @Query("SELECT o FROM OpenApiHospital o WHERE o.hospital = ?1")
    OpenApiHospital findByHospital(Hospital hospital);

}
