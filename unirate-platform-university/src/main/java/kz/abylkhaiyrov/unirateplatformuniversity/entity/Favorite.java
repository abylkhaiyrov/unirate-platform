package kz.abylkhaiyrov.unirateplatformuniversity.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorite_id_seq")
    @SequenceGenerator(name = "favorite_id_seq", sequenceName = "favorite_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

}

