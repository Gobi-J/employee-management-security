package com.i2i.ems.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * <p>
 *   Configures the security settings for the application.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver handlerExceptionResolver;

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public JwtTokenFilter jwtTokenFilter() {
    return new JwtTokenFilter(handlerExceptionResolver);
  }

  /**
   * <p>
   *   Configures the security filter chain that carries out authentication and authorization.
   * </p>
   *
   * @param http
   *       the {@link HttpSecurity} object.
   * @return {@link SecurityFilterChain}
   *      returns the security filter chain.
   * @throws Exception
   *       if any error occurs during the configuration.
   */
  @Bean
  protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(customizer -> customizer
            .requestMatchers("v1/auth/*").permitAll()
            .anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  /**
   * <p>
   *   Configures the authentication provider.
   * </p>
   *
   * @return {@link AuthenticationProvider}
   *      returns the authentication provider.
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
    return authProvider;
  }

  /**
   * <p>
   *   Configures the authentication manager.
   * </p>
   *
   * @param http
   *       the {@link HttpSecurity} object.
   * @return {@link AuthenticationManager}
   *      returns the authentication manager.
   * @throws Exception
   *       if any error occurs during the configuration.
   */
  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.eraseCredentials(false)
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
  }

  /**
   * <p>
   *   Configures the password encoder.
   * </p>
   *
   * @return {@link BCryptPasswordEncoder}
   *      returns the password encoder.
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}