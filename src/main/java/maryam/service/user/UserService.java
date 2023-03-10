package maryam.service.user;

import lombok.RequiredArgsConstructor;
import maryam.data.role.RoleRepository;
import maryam.data.user.UserRepository;
import maryam.models.role.Role;
import maryam.models.user.User;
import maryam.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@Service @RequiredArgsConstructor @Transactional
public class UserService implements UserServiceInterface, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService storageService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found in the database");
        } else {
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

    public User changeEmail(String email){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        user.setEmail(email);
        return user;
    }
    public User changeGender(String gender){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        if(gender == "male"){
            user.setGender(User.Gender.Male);
        } else if (gender == "female") {
            user.setGender(User.Gender.Female);
        }
        return user;
    }
    public User changePhoneNumber(String phoneNumber){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        user.setPhoneNumber(phoneNumber);
        return user;
    }
    public User changeProfilePic(MultipartFile profilePic){
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userRepository.findByUsername(username);
        String picName = storageService.save(profilePic);
        user.setProfilePic(picName);
        return user;
    }



}
