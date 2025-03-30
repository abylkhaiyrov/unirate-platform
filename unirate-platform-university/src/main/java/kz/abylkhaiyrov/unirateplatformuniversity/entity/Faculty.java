package kz.abylkhaiyrov.unirateplatformuniversity.entity;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.AbstractAudit.AbstractAuditEntity;
import kz.abylkhaiyrov.unirateplatformuniversity.enums.CommonName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "faculties")
public class Faculty extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faculty_id_seq")
    @SequenceGenerator(name = "faculty_id_seq", sequenceName = "faculty_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "base_cost")
    private Long baseCost;

    @Column(name = "common_name")
    @Enumerated(EnumType.STRING)
    private CommonName commonName;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Specialty> specialties = new ArrayList<>();

}
