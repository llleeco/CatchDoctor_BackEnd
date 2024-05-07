package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.OpenApiHospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OpenApiRepository extends JpaRepository<OpenApiHospital, Long> {
    @Query("SELECT o FROM OpenApiHospital o WHERE o.addnum = ?1 AND o.hospitalname = ?2")
    OpenApiHospital findByAddressAndHospitalName(String addnum, String hospitalName);
}
