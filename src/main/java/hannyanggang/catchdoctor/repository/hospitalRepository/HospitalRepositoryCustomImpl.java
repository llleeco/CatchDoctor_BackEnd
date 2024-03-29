package hannyanggang.catchdoctor.repository.hospitalRepository;

import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import jakarta.persistence.criteria.Predicate;


import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Page<Hospital> searchWithDynamicQuery(String query, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hospital> cq = cb.createQuery(Hospital.class);
        Root<Hospital> hospital = cq.from(Hospital.class);

        if(query != null && !query.isEmpty()) {
            String[] searchTerms = query.split("\\s+"); // 검색어를 공백을 기준으로 분리
            List<Predicate> orPredicates = new ArrayList<>();
            try {
                for (String term : searchTerms) {
                    // 각 검색어에 대한 조건 그룹 생성
                    System.out.println("term = " + term);
                    List<Predicate> termPredicates = new ArrayList<>();
                    termPredicates.add(cb.like(hospital.get("address"), "%" + term + "%"));
                    termPredicates.add(cb.like(hospital.get("department"), "%" + term + "%"));
                    termPredicates.add(cb.like(hospital.get("name"), "%" + term + "%"));
                    System.out.println("termPredicates = " + termPredicates);
                    // 각 검색어에 대한 조건들을 'OR'로 연결
                    orPredicates.add(cb.or(termPredicates.toArray(new Predicate[0])));

                }

                // 모든 검색어 그룹을 'AND'로 연결
                cq.where(cb.and(orPredicates.toArray(new Predicate[0])));

                // 페이징 처리 적용
                int totalResults = entityManager.createQuery(cq).getResultList().size();
                int offset = pageable.getPageNumber() * pageable.getPageSize();
                int pageSize = pageable.getPageSize();

                cq.select(hospital);
                List<Hospital> hospitals = entityManager.createQuery(cq)
                        .setFirstResult(offset)
                        .setMaxResults(pageSize)
                        .getResultList();

                return new PageImpl<>(hospitals, pageable, totalResults);

            }catch (Exception e){
                throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "안돼2");

            }
        }
        return Page.empty(); // 쿼리가 비어있는 경우 빈 페이지 반환

    }


}
