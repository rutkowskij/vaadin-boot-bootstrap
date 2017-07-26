package io.thingcare.bootstrap.be.security;

import io.thingcare.bootstrap.be.security.type.RoleType;
import io.thingcare.bootstrap.be.shared.BaseEntity;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class Role extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType type;

    public Role() {
    }

}
