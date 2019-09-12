package com.ajs.graph.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajs.graph.exception.InvalidCredentialsException;
import com.ajs.graph.model.User;
import com.ajs.graph.repository.UserRepository;
import com.ajs.graph.security.jwt.JwtTokenUtil;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GraphQLQuery(name = "users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GraphQLQuery(name = "user")
    public Optional<User> getUserById(@GraphQLArgument(name = "id") Long id) {
        return userRepository.findById(id);
    }


    @GraphQLMutation(name = "signin")
    public String signin(@GraphQLArgument(name = "username") String username,
                         @GraphQLArgument(name = "password") String password) throws InvalidCredentialsException {
        Optional<User> user = userRepository.findByUsername(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(user.isPresent()) {
            if (encoder.matches(password, user.get().getPassword())) {
                logger.info("success...");
                return jwtTokenUtil.generateToken(user.get().getUsername());
            } else {
                logger.info("Invalid Credentials1");
                throw new InvalidCredentialsException("Invalid Credentials!");
            }
        }else{
            logger.info("Invalid Credentials2");
            throw new InvalidCredentialsException("Invalid Credentials!");
        }
    }
    
    @GraphQLMutation(name = "getTest")
    public User getTest() throws InvalidCredentialsException {
        return new User();
    }

}
