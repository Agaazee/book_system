package com.book.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.book.security.filter.JwtAuthenticationFilter;
import com.book.security.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private MyUserDetailsService myUserDetailsService;
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(MyUserDetailsService myUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.myUserDetailsService = myUserDetailsService;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf(csrf -> csrf.disable())
				.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
						.configurationSource(corsConfigurationSource()))

				.authorizeHttpRequests(httpRequest -> {

					httpRequest.requestMatchers(HttpMethod.POST, "/api/admin/add").permitAll()
							.requestMatchers(HttpMethod.POST, "/api/admin/login").permitAll();
					httpRequest.anyRequest().authenticated();

				}).formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable)
				.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return httpSecurity.build();

	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
		return authenticationManagerBuilder.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}

	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		configuration.addAllowedOrigin("http://localhost:8081");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
