package hannyanggang.catchdoctor.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
public class OperatingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "break_time")
    private String breakTime;

    @ManyToOne
    @JoinColumn(name = "hospitalid")
    private Hospital hospital;
}
