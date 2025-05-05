package com.example.todo_management.service;

import com.example.todo_management.dto.JwtAuthResponse;
import com.example.todo_management.dto.LoginDto;
import com.example.todo_management.dto.RegisterDto;

public interface AuthService 
{
	String register(RegisterDto registerDto);
	
	JwtAuthResponse login(LoginDto loginDto);
}
