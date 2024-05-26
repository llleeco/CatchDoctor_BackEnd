package hannyanggang.catchdoctor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "OPENAPI")
public class OpenApiHospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "hospitalname")
    private String hospitalname; // 병원이름

    @Column(name = "address")
    private String address;

    @Column(name = "tel")
    private String tel;

    @Column(name = "addnum")
    private String addnum;

    @Column(name = "mapX")
    private Double mapX; // 위도

    @Column(name = "mapY")
    private Double mapY; // 경도

    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="hospital_id")
    private Hospital hospital;

    public OpenApiHospital(Long id, String hospitalname, String address, String tel, String addnum, Double mapX, Double mapY, Hospital hospital) {
        this.id = id;
        this.hospitalname = hospitalname;
        this.address = address;
        this.tel = tel;
        this.addnum = addnum;
        this.mapX = mapX;
        this.mapY = mapY;
        this.hospital = hospital;
    }

}
