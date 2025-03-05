package kz.abylkhaiyrov.unirateplatformuniversity.entity;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.AbstractAudit.AbstractAuditEntity;
import kz.abylkhaiyrov.unirateplatformuniversity.enums.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    private String comment;

    private Short rating;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

    private Integer likes = 0;

    private Integer dislikes = 0;

}