package hannyanggang.catchdoctor.repository.hospitalRepository;

import hannyanggang.catchdoctor.entity.OpenApiHospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenApiRepository extends JpaRepository<OpenApiHospital, Long>, OpenApiRepositoryCustom  {
    Page<OpenApiHospital> searchWithDynamicQuery(String query, Pageable pageable);
}
