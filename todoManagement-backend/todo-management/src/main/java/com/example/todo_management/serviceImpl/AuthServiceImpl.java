package com.example.todo_management.serviceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.todo_management.dto.JwtAuthResponse;
import com.example.todo_management.dto.LoginDto;
import com.example.todo_management.dto.RegisterDto;
import com.example.todo_management.entity.Role;
import com.example.todo_management.entity.User;
import com.example.todo_management.exception.TodoAPIException;
import com.example.todo_management.repository.RoleRepository;
import com.example.todo_management.repository.UserRepository;
import com.example.todo_management.security.JwtTokenProvider;
import com.example.todo_management.service.AuthService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService
{
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JwtTokenProvider jwtTokenProvider;
	
	@Override
	public String register(RegisterDto registerDto) 
	{
		//check username is already exist in a database
		if(userRepository.existsByUsername(registerDto.getUsername()))
		{
			throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!");
		}
		
		//check username is already exist in a database
		if(userRepository.existsByEmail(registerDto.getEmail()))
		{
			throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!");
		}
		
		User user = new User();
		user.setName(registerDto.getName());
		user.setUsername(registerDto.getUsername());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		
		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByName("ROLE_USER");
		roles.add(userRole);
		
		user.setRoles(roles);
		
		userRepository.save(user);
	
		return "User Registered Successfully";
	}

	
	@Override
	public JwtAuthResponse login(LoginDto loginDto) 
	{
		Authentication authentication =	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginDto.getUsernameOrEmail(), loginDto.getPassword()));	
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtTokenProvider.generateToken(authentication);
		
		Optional<User> userOptional = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail());
		
		String role = null;
		
		if(userOptional.isPresent())
		{
			User loggedInUser = userOptional.get();
			Optional<Role> optionalRole = loggedInUser.getRoles().stream().findFirst();
			
			if(optionalRole.isPresent())
			{
				Role userRole = optionalRole.get();
				role = userRole.getName();
			}
		}
		
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setRole(role);
		jwtAuthResponse.setAccessToken(token);
		
		return jwtAuthResponse;
	}

}
