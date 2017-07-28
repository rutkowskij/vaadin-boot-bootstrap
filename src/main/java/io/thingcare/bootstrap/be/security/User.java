package io.thingcare.bootstrap.be.security;

import io.thingcare.bootstrap.be.shared.BaseEntity;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class User extends BaseEntity {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private boolean enabled;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Role> roles;

    public User() {
    }
}
