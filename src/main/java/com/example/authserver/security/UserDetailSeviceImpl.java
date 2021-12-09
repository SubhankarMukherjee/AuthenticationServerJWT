package com.example.authserver.security;


import com.example.authserver.model.User;
import com.example.authserver.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailSeviceImpl implements UserDetailsService {
    @Autowired
    UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if(user==null)
        {
            throw new UsernameNotFoundException("User Not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword()
                ,user.getRoles());

    }
}
