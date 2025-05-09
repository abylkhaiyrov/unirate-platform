package kz.abylkhaiyrov.unirateplatformuniversity.entity;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.AbstractAudit.AbstractAuditEntity;
import kz.abylkhaiyrov.unirateplatformuniversity.enums.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_id_seq")
    @SequenceGenerator(name = "review_id_seq", sequenceName = "review_id_seq", allocationSize = 1)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

    private String comment;

    private Short rating;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

    private Integer likes = 0;

    private Integer dislikes = 0;

}