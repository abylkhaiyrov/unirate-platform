package kz.abylkhaiyrov.unirateplatformuniversity.entity;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.AbstractAudit.AbstractAuditEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "universities")
public class University extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "universities_id_seq")
    @SequenceGenerator(name = "universities_id_seq", sequenceName = "universities_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "rating_count")
    private Long ratingCount;

    @Column(name = "base_cost", columnDefinition = "Данная колонка предназначена для примерно ценой для оплаты за все")
    private Long baseCost;

    @Column(name = "location")
    private String location;

    @Column(name = "website")
    private String website;

    @Column(name = "accreditation")
    private String accreditation;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "military_department")
    private Boolean militaryDepartment;

    @Column(name = "dormitory")
    private Boolean dormitory;

    /**
     * Связь один ко многим (один университет - много курсов).
     * mappedBy="university" говорит, что колонка FK находится в сущности Course.
     */
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Course> courses = new ArrayList<>();

    @OneToOne(mappedBy = "university", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UniversityAddress universityAddress;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Forum> forums = new ArrayList<>();

}
