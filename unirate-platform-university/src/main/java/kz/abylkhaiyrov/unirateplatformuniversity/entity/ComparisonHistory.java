package kz.abylkhaiyrov.unirateplatformuniversity.entity;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.AbstractAudit.AbstractAuditEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "comparison_histories")
public class ComparisonHistory extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    /**
     * Многие к одному: один Specialty, много ComparisonHistory
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    /**
     * Два столбца в виде JSONB (PostgreSQL).
     * Можно хранить как String или использовать @Convert для Jackson/JSON (на ваш выбор).
     */
    @Column(name = "university_ids", columnDefinition = "jsonb")
    private String universityIds;

    @Column(name = "criteria", columnDefinition = "jsonb")
    private String criteria;

}
