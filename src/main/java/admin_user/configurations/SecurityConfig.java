package admin_user.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import admin_user.service.CustomSuccessHandler;
import admin_user.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	@Autowired
	CustomSuccessHandler customSuccessHandler;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    
    http.csrf(c -> c.disable())
    
    .authorizeHttpRequests(request -> request
            .requestMatchers("/superadmin-page").hasAuthority("SUPER_ADMIN")  // Super Admin page access
            .requestMatchers("/admin-page").hasAuthority("ADMIN")              // Admin page access
            .requestMatchers("/user-page").hasAuthority("USER")   
            .requestMatchers("/user/bookings/pay").hasAuthority("USER")             // User page access
            .requestMatchers("/registration", "/css/**").permitAll()           // Permit registration and CSS files
            .anyRequest().authenticated())                                     // Other requests must be authenticated
    
    .formLogin(form -> form
            .loginPage("/login")                                               // Custom login page
            .loginProcessingUrl("/login")
            .successHandler(customSuccessHandler)                              // Custom login success handler
            .permitAll())
    
    .logout(form -> form
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login?logout").permitAll());
    
    return http.build();
}

	
	@Autowired
	public void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

}
