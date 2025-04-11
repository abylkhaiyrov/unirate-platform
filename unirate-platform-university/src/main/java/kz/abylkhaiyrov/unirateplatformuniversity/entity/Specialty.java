package kz.abylkhaiyrov.unirateplatformuniversity.entity;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.AbstractAudit.AbstractAuditEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "specialties")
public class Specialty extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specialties_comments_id_seq")
    @SequenceGenerator(name = "specialties_comments_id_seq", sequenceName = "specialties_comments_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @Column(name = "specialty_image_url", length = 500)
    private String specialtyImageUrl;

    @Column(name = "faculty_name")
    private String facultyName;

    @Column(name = "university_name")
    private String universityName;

    @Column(name = "gop_code")
    private String gopCode;

    @Column(name = "grants")
    private String grants;

    @Column(name = "min_scores")
    private String minScores;

    /**
     * Многие ко многим с "Course" через промежуточную таблицу "course_specialties".
     * mappedBy="specialties" указывает, что "Specialty" описан в поле "specialties" в классе Course.
     */
    @ManyToMany(mappedBy = "specialties")
    private List<Course> courses = new ArrayList<>();
}
