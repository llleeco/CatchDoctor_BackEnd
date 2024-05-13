package hannyanggang.catchdoctor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hannyanggang.catchdoctor.role.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.apache.bcel.classfile.Module;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
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

    @Column(nullable = false)
    private String addnum;

    private UserRole role; // USER

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="hospital_detail_id")
    private HospitalDetail hospitalDetail;

//    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    @JoinColumn(name="hospital_oepnapi_id")
//    private OpenApiHospital openApiHospital;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    private List<Review> review;

    @OneToMany(mappedBy = "hospital")
    private Set<Reservations> reservations; //예약

}
