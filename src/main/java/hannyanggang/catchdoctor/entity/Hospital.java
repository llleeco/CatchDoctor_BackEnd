package hannyanggang.catchdoctor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hannyanggang.catchdoctor.role.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
public class Hospital {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalid;

//    @Column(nullable = false, unique = true)
//    prㅌㅌㅌㅌivate String id;

//    @JsonIgnore
//    @Column(nullable = false)
//    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private String department;//진료과목

    @OneToMany(mappedBy = "hospital")
    private Set<OperatingHours> operatingHours; //운영시간Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'hospitalRepository' defined in hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository defined in @EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration: Could not create query for public abstract org.springframework.data.domain.Page hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository.searchWithDynamicQuery(java.lang.String,org.springframework.data.domain.Pageable); Reason: Failed to create query for method public abstract org.springframework.data.domain.Page hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository.searchWithDynamicQuery(java.lang.String,org.springframework.data.domain.Pageable); No property 'searchWithDynamicQuery' found for type 'Hospital'


    @OneToMany(mappedBy = "hospital")
    private Set<Reservations> reservations; //예약
//    private UserRole role; // ADMIN
}
