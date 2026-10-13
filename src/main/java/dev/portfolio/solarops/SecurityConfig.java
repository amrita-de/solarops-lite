package dev.portfolio.solarops;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration public class SecurityConfig {
 @Bean PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
 @Bean UserDetailsService users(PasswordEncoder e,@Value("${app.manager-password}") String m,@Value("${app.viewer-password}") String v){return new InMemoryUserDetailsManager(User.withUsername("manager").password(e.encode(m)).roles("MANAGER").build(),User.withUsername("viewer").password(e.encode(v)).roles("VIEWER").build());}
 @Bean SecurityFilterChain security(HttpSecurity http)throws Exception {
  return http.csrf(c->c.csrfTokenRepository(new org.springframework.security.web.csrf.CookieCsrfTokenRepository())).sessionManagement(s->s.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers(org.springframework.http.HttpMethod.GET,"/api/inventory","/api/csrf").authenticated().requestMatchers("/api/**").hasRole("MANAGER").anyRequest().permitAll()).httpBasic(Customizer.withDefaults()).build();
 }
}
