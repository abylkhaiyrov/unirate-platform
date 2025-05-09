package kz.abylkhaiyrov.unirateplatformregistry.entity;

import kz.abylkhaiyrov.unirateplatformregistry.entity.AbstractAudit.AbstractAuditingEntity;
import kz.abylkhaiyrov.unirateplatformregistry.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "_user")
public class User extends AbstractAuditingEntity {

    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "activation_code")
    private Integer activationCode;

    @Column(name = "activation_code_sent_at")
    private LocalDateTime activationCodeSentAt;

    @Column(name = "telephone_number")
    private String telephone;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "active")
    private boolean isActive = false;

    @Column(name = "deleted")
    private boolean isDeleted = false;

    @Column(name = "user_profile_image_url", length = 500)
    private String profileImageUrl;

}
