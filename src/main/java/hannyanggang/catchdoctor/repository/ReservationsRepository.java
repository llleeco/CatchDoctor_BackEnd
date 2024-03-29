package hannyanggang.catchdoctor.repository;

import hannyanggang.catchdoctor.entity.Reservations;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationsRepository extends JpaRepository<Reservations, String> {

    @Query("SELECT COUNT(a) FROM Reservations a WHERE a.hospital.hospitalid = :hospitalid AND a.reservationDate = :reservationDate AND a.reservationTime = :reservationTime")
    int countByHospital_HospitalIdAndReservationDateAndReservationTime(@Param("hospitalid") Long hospitalId, @Param("reservationDate") LocalDate reservationDate, @Param("reservationTime") LocalTime reservationTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({ @QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000") }) // 5초 타임아웃 설정
    @Query("SELECT COUNT(a) FROM Reservations a WHERE a.hospital.hospitalid = :hospitalid AND a.reservationDate= :reservationDate AND a.reservationTime = :startTime")
    int countReservationsForTimeSlot(@Param("hospitalid") Long hospitalid,
                                     @Param("reservationDate") LocalDate reservationDate,
                                     @Param("startTime") LocalTime startTime);

    int countByUser_IdAndStatusAndReservationDateAfter(String userId, String 예약부도, LocalDate oneMonthAgo);

    boolean existsByUser_IdAndReservationDateAndReservationTime(String userId, LocalDate date, LocalTime time);

    boolean existsByUser_IdAndHospital_HospitalidAndReservationDate(String userId, Long hospitalId, LocalDate date);

    int countByUser_IdAndStatus(String userId, String 진료전);

    @Modifying
    @Query("UPDATE Reservations a SET a.status = '진료완료' WHERE a.reservationDate < :currentDate AND a.status = '진료전'")
    int updateStatusForPastReservations(LocalDate currentDate);

    List<Reservations> findByUser_IdOrderByReservationDateDesc(String userId);

    @Query("SELECT a FROM Reservations a JOIN a.user p JOIN a.hospital h WHERE p.id = :id AND h.name LIKE :name AND a.reservationDate = :reservationDate ORDER BY a.reservationDate DESC")
    List<Reservations> findByUser_IdAndHospital_NameLikeAndReservationDate(@Param("id") String id, @Param("name") String name, @Param("reservationDate") LocalDate reservationDate);

    @Query("SELECT a FROM Reservations a JOIN a.user p JOIN a.hospital h WHERE p.id = :id AND a.reservationDate = :reservationDate ORDER BY a.reservationDate DESC")
    List<Reservations> findByUser_IdAndReservationDate(@Param("id") String id, @Param("reservationDate") LocalDate reservationDate);

    @Query("SELECT a FROM Reservations a JOIN a.user p JOIN a.hospital h WHERE p.id= :id AND h.name LIKE :name ORDER BY a.reservationDate DESC")
    List<Reservations> findByUser_IdAndHospital_Name(@Param("id") String id, @Param("name") String name);

    //예약 취소하기 위해
    @Query("SELECT a FROM Reservations a WHERE a.user.id = :id AND a.reservationId = :reservationId")
    Optional<Reservations> findByUser_IdAndReservationId(@Param("reservationId") Long reservationId, @Param("id") String id);
}
