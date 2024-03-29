package hannyanggang.catchdoctor.repository.hospitalRepository;

import hannyanggang.catchdoctor.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepositoryCustom {
    Page<Hospital> searchWithDynamicQuery(String query, Pageable pageable);

}