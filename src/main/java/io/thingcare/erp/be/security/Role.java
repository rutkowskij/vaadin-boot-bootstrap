package io.thingcare.erp.be.security;

import io.thingcare.erp.be.security.type.RoleType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "RoleType", nullable = false, unique = true)
    private RoleType type;
    @CreatedDate
    @Type(type = "java.sql.Timestamp")
    @Column(updatable = false)
    private Date createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private User user;

    public Role() {
    }
}
