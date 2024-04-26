package hannyanggang.catchdoctor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "OPENAPIPharmacy")
public class OpenApiPharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "pharmacyname")
    private String pharmacyname; // 약국이름

    @Column(name = "address")
    private String address; // 약국 주소

    @Column(name = "tel")
    private String tel; // 약국 전화번호

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

    @Column(name = "mapX")
    private Double mapX; // 위도

    @Column(name = "mapY")
    private Double mapY; // 경도

    public OpenApiPharmacy(Long id, String pharmacyname, String address,
                           String tel, String mon_open, String mon_close,
                           String tue_open, String tue_close,
                           String wed_open, String wed_close,
                           String thu_open, String thu_close,
                           String fri_open, String fri_close,
                           String sat_open, String sat_close,
                           String sun_open, String sun_close,
                           String hol_open, String hol_close,Double mapX, Double mapY) {
        this.id = id;
        this.pharmacyname = pharmacyname;
        this.address = address;
        this.tel = tel;
        this.mon_open = mon_open;
        this.mon_close = mon_close;
        this.tue_open = tue_open;
        this.tue_close = tue_close;
        this.wed_open = wed_open;
        this.wed_close = wed_close;
        this.thu_open = thu_open;
        this.thu_close = thu_close;
        this.fri_close = fri_close;
        this.fri_open = fri_open;
        this.sat_open = sat_open;
        this.sat_close = sat_close;
        this.sun_open = sun_open;
        this.sun_close = sun_close;
        this.hol_open = hol_open;
        this.hol_close = hol_close;
        this.mapX = mapX;
        this.mapY = mapY;
    }

}
