package com.example.authserver.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    //Not required
    @ManyToMany(mappedBy ="roles")
   private Set<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
