package maryam.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maryam.models.role.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Table(name="user_list")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "user_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_id_genrator", sequenceName = "sc.user_id_generator",allocationSize=1)
    @Column(unique=true, nullable=false)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();



}
