package maryam.service.user;

import lombok.RequiredArgsConstructor;
import maryam.data.role.RoleRepository;
import maryam.data.user.UserRepository;
import maryam.models.role.Role;
import maryam.models.user.User;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@Service @RequiredArgsConstructor @Transactional
public class UserService implements UserServiceInterface{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    //@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User not fount in the database");
        } else {
            ///
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username,String name){
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(name);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username){
        return userRepository.findByUsername(username);
    }
    @Override
    public List<User> getUsers(){
        return (List<User>) userRepository.findAll();
    }



}
