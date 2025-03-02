package kz.abylkhaiyrov.unirateplatformregistry.entity.AbstractAudit;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public class AbstractAuditingEntity {

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    public AbstractAuditingEntity() {
        this.createdBy = "system";
        this.createdDate = Instant.now();
    }
}
