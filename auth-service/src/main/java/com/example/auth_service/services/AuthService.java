package com.example.auth_service.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth_service.dto.AuthRequest;
import com.example.auth_service.dto.AuthResponse;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.model.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.security.JwtService;

@Service
public class AuthService {

	@Autowired
    private UserRepository userRepo;
	
	@Autowired
    private PasswordEncoder encoder;
	
	@Autowired
    private JwtService jwtService;
	
	@Autowired
    private AuthenticationManager authManager;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepo.save(user);
        String token = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        ));
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        
        userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtService.generateToken((UserDetails)auth.getPrincipal());
        return new AuthResponse(token);
    }
    
    public Map<String, Object> validate(String authHeader) throws Exception {
    	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    		throw new Exception("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (!jwtService.isValid(token, user)) {
        	throw new Exception("Invalid token");
        }
        return Map.of("username", username, "role", user.getAuthorities());
    }
}
