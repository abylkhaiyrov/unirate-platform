package kz.abylkhaiyrov.unirateplatformuniversity.entity;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.AbstractAudit.AbstractAuditEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review_comments")
@Getter
@Setter
public class ReviewComment extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_comments_id_seq")
    @SequenceGenerator(name = "review_comments_id_seq", sequenceName = "review_comments_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private Long userId;

    private String comment;
}