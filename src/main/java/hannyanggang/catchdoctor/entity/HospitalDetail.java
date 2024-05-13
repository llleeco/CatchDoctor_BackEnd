package hannyanggang.catchdoctor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
@Table(name="HospitalDetail")
public class HospitalDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name="hospital_id")
    private Hospital hospital;

    @Column(name="hospitalInfo")
    private String hospitalInfo;

    @Column(name="department")
    private String department;

    @Column(name="doctorInfo")
    private String doctorInfo;

    @Column(name = "mon_open")
    private String mon_open; // 월요일
    @Column(name = "mon_close")
    private String mon_close;

    @Column(name = "tue_open")
    private String tue_open; // 화요일
    @Column(name = "tue_close")
    private String tue_close;

    @Column(name = "wed_open")
    private String wed_open; // 수요일
    @Column(name = "wed_close")
    private String wed_close;

    @Column(name = "thu_open")
    private String thu_open; // 목요일
    @Column(name = "thu_close")
    private String thu_close;

    @Column(name = "fri_open")
    private String fri_open; // 금요일
    @Column(name = "fri_close")
    private String fri_close;

    @Column(name = "sat_open")
    private String sat_open; // 토요일
    @Column(name = "sat_close")
    private String sat_close;

    @Column(name = "sun_open")
    private String sun_open; // 일요일
    @Column(name = "sun_close")
    private String sun_close;

    @Column(name = "hol_open")
    private String hol_open; // 공휴일
    @Column(name = "hol_close")
    private String hol_close;

    // 병원 이미지는 추후 추가예정
}