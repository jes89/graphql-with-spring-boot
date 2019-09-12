package com.ajs.graph.security.jwt;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ajs.graph.model.User;
import com.ajs.graph.repository.UserRepository;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info("load user...");
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            logger.info("user:: {}", user.get().getUsername());
            return getJwtUser(user.get());
        } else {
            logger.info("user not found");
            //throw new UsernameNotFoundException(String.format("User not found with username '%s'.", username));
            return null;
        }
    }


    public JwtUser getJwtUser(User user) {
    	
    	List<SimpleGrantedAuthority> list = new ArrayList<>();
    	
    	list.add(new SimpleGrantedAuthority("user"));
    	
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                list,
                user.getEnabled(),
                null
        );
    }
}
