package kz.abylkhaiyrov.unirateplatformuniversity.entity;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.AbstractAudit.AbstractAuditEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "university_address")
public class UniversityAddress extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "full_address")
    private String fullAddress;

}
