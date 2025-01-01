package com.interland.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	  @Bean 
	  public SecurityFilterChain securityFilterChain(HttpSecurity http)
	  throws Exception {
	  
	  http.cors(AbstractHttpConfigurer::disable);
	  http.csrf(AbstractHttpConfigurer::disable);
	  http.authorizeHttpRequests(request->request.requestMatchers(HttpMethod.POST,"/parking/getaccesstoken").permitAll()
			  .anyRequest().authenticated());
	  http.cors().and().csrf().disable(); 
	  http.sessionManagement(sess ->
	  sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	  http.oauth2ResourceServer(authServer->authServer.jwt(Customizer.withDefaults(
	  ))); return http.build(); }
	 
}
