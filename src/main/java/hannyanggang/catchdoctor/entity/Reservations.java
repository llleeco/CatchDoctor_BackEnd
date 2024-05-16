package hannyanggang.catchdoctor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
@Setter
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(name="reservation_date")
    private LocalDate reservationDate;

    @Column(name="reservation_time")
    private LocalTime reservationTime;

    private String status;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="userid")
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="hospitalid", nullable = false)
    private Hospital hospital;

    public void setUserId(String userId, UserRepository patientRepository) {
        User patient = patientRepository.findById(userId);
        if (patient != null) {
            this.user = patient;
        } else {
            // 사용자가 존재하지 않을 때의 처리
            throw new CustomValidationException(HttpStatus.UNAUTHORIZED.value(), "존재하지 않는 사용자");
        }
    }

    public void setHospitalId(Long hospitalId, HospitalRepository hospitalRepository) {
        Hospital hospitals = hospitalRepository.findByHospitalid(hospitalId).orElse(null);
        if (hospitals != null) {
            this.hospital = hospitals;
        } else {
            // 병원이 존재하지 않을 때의 처리
            throw new CustomValidationException(HttpStatus.UNAUTHORIZED.value(), "존재하지 않는 병원");
        }
    }

}
