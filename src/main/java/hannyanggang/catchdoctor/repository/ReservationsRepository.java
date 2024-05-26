package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.Reservations;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationsRepository extends JpaRepository<Reservations, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({ @QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000") }) // 5초 타임아웃 설정
    @Query("SELECT COUNT(a) FROM Reservations a WHERE a.hospital.hospitalid = :hospitalid AND a.reservationDate= :reservationDate AND a.reservationTime = :startTime")
    int countReservationsForTimeSlot(@Param("hospitalid") Long hospitalid,
                                     @Param("reservationDate") LocalDate reservationDate,
                                     @Param("startTime") LocalTime startTime);

    int countByUser_IdAndStatusAndReservationDateAfter(String userId, String 예약부도, LocalDate oneMonthAgo);

    boolean existsByUser_IdAndReservationDateAndReservationTime(String userId, LocalDate date, LocalTime time);

    boolean existsByUser_IdAndHospital_HospitalidAndReservationDate(String userId, Long hospitalId, LocalDate date);

    int countByUser_IdAndStatus(String userId, String 예약신청);

    @Modifying
    @Query("UPDATE Reservations a SET a.status = '진료완료' WHERE a.reservationDate < :currentDate AND a.status = '예약신청'")
    int updateStatusForPastReservations(LocalDate currentDate);

    List<Reservations> findByUser_IdOrderByReservationDateAsc(String userId);

    List<Reservations> findByHospital_IdOrderByReservationDateAscReservationTimeAsc(String hospitalId);

    //예약 취소하기 위해
    @Query("SELECT a FROM Reservations a WHERE a.user.id = :id AND a.reservationId = :reservationId")
    Optional<Reservations> findByUser_IdAndReservationId(@Param("reservationId") Long reservationId, @Param("id") String id);

    Reservations findByHospitalAndReservationDateAndReservationTime(Hospital hospital, LocalDate date, LocalTime time);

    Reservations findByReservationId(Long id);
}
