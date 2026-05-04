package com.example.user_management.service.auth;

import com.example.user_management.model.User;
import com.example.user_management.model.UserPrincipal;
import com.example.user_management.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("inside user Detail service");
        User user = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User 404"));
        return new UserPrincipal(user);
    }

}
