package com.example.readnovel.security;

import com.example.readnovel.constrains.Message;
import com.example.readnovel.models.entity.User;
import com.example.readnovel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException(Message.EMAIL_NOT_FOUND);
        }
        return new CustomUserDetails(user.get());
    }
}
