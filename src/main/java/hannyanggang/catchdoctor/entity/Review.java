package hannyanggang.catchdoctor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"hospital","user"})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long review_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospitalid", nullable = false)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private int grade;

    private String text;

    @Column(updatable = false)
    private LocalDate regDate;

    private LocalDate modDate;

    // 추가
    public void changeGrade(int grade){
        this.grade = grade;
    }

    // 추가
    public void changeText(String text){
        this.text = text;
    }

    @PrePersist
    protected void onCreate() {
        regDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modDate = LocalDate.now();
    }
}

