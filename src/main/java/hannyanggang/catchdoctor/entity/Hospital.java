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
@Table(name="Hospital")
public class Hospital {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalid;

    @Column(nullable = false, unique = true)
    private String id;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="hospital_detail_id")
    private HospitalDetail hospitalDetail;

    @OneToMany(mappedBy = "hospital")
    private Set<Reservations> reservations; //예약
//    private UserRole role; // ADMIN
}
