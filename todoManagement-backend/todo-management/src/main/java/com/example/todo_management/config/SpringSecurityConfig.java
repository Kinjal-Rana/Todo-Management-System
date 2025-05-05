package com.example.todo_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.todo_management.security.JwtAuthenticationEntryPoint;
import com.example.todo_management.security.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SpringSecurityConfig 
{
	private UserDetailsService userDetailsService;
	
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	private JwtAuthenticationFilter authenticationFilter;
	
	@Bean
	public static PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();	
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.csrf((csrf) -> csrf.disable()).authorizeHttpRequests((authorize) -> {
			
			//Instead of writing below code we can use Method Level Security
/*			//only admin can access add,update,delete Todo REST APIs 
			authorize.requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN");
			authorize.requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN");
			authorize.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN");
			
			authorize.requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER");
			authorize.requestMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("ADMIN", "USER");
*/			
			//authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll(); 
			//no need to pass user credentials because we have exposed these REST APIs publicly
			
			authorize.requestMatchers("/api/auth/**").permitAll(); 
			authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
			
			authorize.anyRequest().authenticated();
		}).httpBasic(Customizer.withDefaults());
		
		/* when unauthorized user tries to access the resource then spring security throws authentication exception and
		then JwtAuthenticationEntryPoint class will catch that exception and return the error response to the client */
		http.exceptionHandling( exception -> exception.authenticationEntryPoint(authenticationEntryPoint));
		
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
	{
		return configuration.getAuthenticationManager();
	}
	
	
	//Multiple Users to perform basic authentication
//	@Bean
//	public UserDetailsService userDetailsService()
//	{
//		UserDetails user = User.builder().username("user1")
//				.password(passwordEncoder().encode("password")).roles("USER").build();
//		
//		UserDetails admin = User.builder().username("admin")
//				.password(passwordEncoder().encode("admin")).roles("ADMIN").build();
//		
//		return new InMemoryUserDetailsManager(user, admin);
//	}
}
