package com.springsecurity.services;

import com.springsecurity.entities.Role;
import com.springsecurity.entities.User;
import com.springsecurity.repositories.RoleRepo;
import com.springsecurity.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ServiceImp implements UserService, UserDetailsService {
   @Autowired
    UserRepo userRepo;
   @Autowired
    RoleRepo roleRepo;
   @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public User saveUser(User user) {
        log.info("saving new user {}",user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role r = new Role();
        r.setName("USER_ROLE");
        roleRepo.save(r);
        user.getRoles().add(r);
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving new role {}",role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
         User user = userRepo.findByUsername(username);
         Role role=roleRepo.findByName(roleName);
         user.getRoles().add(role);
        log.info("adding role {} to user{}",role.getName(),user.getName());

        userRepo.save(user);
    }

    @Override
    public User getUser(String username) {
        log.info("get user {}",username);

        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("get all users");

        return userRepo.findAll();
    }

    @Override
    public User register(User user) {
        log.info("saving new company {}",user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role r = new Role();
        r.setName("COMAPANY_ROLE");
        roleRepo.save(r);
        user.getRoles().add(r);
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepo.findByUsername(username);
       if(user==null){
           log.error("user not found in the databasse");
       }else{
           log.info("User found in the database:{}",username);
       }
       Collection<SimpleGrantedAuthority> authorities =new ArrayList<>();
      user.getRoles().forEach(role->{
          authorities.add(new SimpleGrantedAuthority(role.getName()));
      });
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }
}
