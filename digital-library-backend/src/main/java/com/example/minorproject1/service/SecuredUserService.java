package com.example.minorproject1.service;

import com.example.minorproject1.model.SecuredUser;
import com.example.minorproject1.repository.SecuredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecuredUserService implements UserDetailsService {

    @Autowired
    SecuredUserRepository securedUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return securedUserRepository.findByUsername(username);
    }

    public SecuredUser save(SecuredUser securedUser) {
        return securedUserRepository.save(securedUser);
    }

    public void deleteById(int id) {
        securedUserRepository.deleteById(id);
    }
}
