package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.OpenApiHospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenApiRepository extends JpaRepository<OpenApiHospital, Long> {
}
