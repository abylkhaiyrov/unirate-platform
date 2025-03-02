package kz.abylkhaiyrov.unirateplatformregistry.entity;

import kz.abylkhaiyrov.unirateplatformregistry.entity.AbstractAudit.AbstractAuditingEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "_user_role")
@Data
public class UserRole extends AbstractAuditingEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "_user_role_id_seq")
    @SequenceGenerator(name = "_user_role_id_seq", sequenceName = "_user_role_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assign_user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;
}
