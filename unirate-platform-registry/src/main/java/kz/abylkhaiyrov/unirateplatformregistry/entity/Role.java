package kz.abylkhaiyrov.unirateplatformregistry.entity;

import kz.abylkhaiyrov.unirateplatformregistry.entity.AbstractAudit.AbstractAuditingEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
public class Role extends AbstractAuditingEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
    @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", allocationSize = 1)
    public Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "ru", nullable = false)
    private String ru;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}
