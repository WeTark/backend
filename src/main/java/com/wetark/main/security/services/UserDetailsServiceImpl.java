package com.wetark.main.security.services;

import com.wetark.main.model.user.User;
import com.wetark.main.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String phoneNo) throws UsernameNotFoundException {
    User user = userRepository.findByPhoneNo(phoneNo)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + phoneNo));

    return UserDetailsImpl.build(user);
  }

}
