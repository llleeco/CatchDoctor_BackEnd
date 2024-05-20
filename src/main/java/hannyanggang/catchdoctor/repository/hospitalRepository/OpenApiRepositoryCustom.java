package hannyanggang.catchdoctor.repository.hospitalRepository;

import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.OpenApiHospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenApiRepositoryCustom {
    Page<OpenApiHospital> searchWithDynamicQuery(String query, Pageable pageable);
    Page<OpenApiHospital> searchByDepartment(String department, Pageable pageable);

}