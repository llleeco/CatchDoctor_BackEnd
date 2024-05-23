package hannyanggang.catchdoctor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import hannyanggang.catchdoctor.role.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.apache.bcel.classfile.Module;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
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

    private UserRole role; // USER

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="hospital_detail_id")
    @JsonManagedReference
    private HospitalDetail hospitalDetail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="hospital_openapi_id")
    @JsonManagedReference
    private OpenApiHospital openApiHospital;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Review> review = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Reservations> reservations; //예약

}
