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

    /**
     * Многие ко многим с "Course" через промежуточную таблицу "course_specialties".
     * mappedBy="specialties" указывает, что "Specialty" описан в поле "specialties" в классе Course.
     */
    @ManyToMany(mappedBy = "specialties")
    private List<Course> courses = new ArrayList<>();
}
