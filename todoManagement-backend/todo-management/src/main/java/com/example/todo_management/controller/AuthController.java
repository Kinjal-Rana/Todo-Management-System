package com.example.todo_management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo_management.dto.JwtAuthResponse;
import com.example.todo_management.dto.LoginDto;
import com.example.todo_management.dto.RegisterDto;
import com.example.todo_management.service.AuthService;

import lombok.AllArgsConstructor;


@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController 
{
	private AuthService authService;
	
	//Register REST API
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto)
	{
		String response = authService.register(registerDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
	//Login REST API
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto)
	{
		JwtAuthResponse jwtAuthResponse = authService.login(loginDto);
		
		return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
	}
}
