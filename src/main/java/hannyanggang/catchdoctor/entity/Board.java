package hannyanggang.catchdoctor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String content;

    @Lob
    @Column(name = "boardimage1")
    private byte[] boardImage1;

    @Lob
    @JsonIgnore
    @Column(name = "boardimage2")
    private byte[] boardImage2;

    @Lob
    @JsonIgnore
    @Column(name = "boardimage3")
    private byte[] boardImage3;

    @Lob
    @JsonIgnore
    @Column(name = "boardimage4")
    private byte[] boardImage4;

    @Lob
    @JsonIgnore
    @Column(name = "boardimage5")
    private byte[] boardImage5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(updatable = false)
    private LocalDate regDate;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(updatable = false)
    private LocalTime regTime;
}
