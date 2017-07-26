package io.thingcare.erp.be.security;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    @CreatedDate
    @Type(type="java.sql.Timestamp")
    @Column(updatable = false)
    private Date createdDate;

    @CreatedDate
    @Type(type="java.sql.Timestamp")
    @Column(updatable = false)
    private Date lastModifiedDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roles;
}
