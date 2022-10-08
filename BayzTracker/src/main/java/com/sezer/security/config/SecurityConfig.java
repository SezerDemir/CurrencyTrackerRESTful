package com.sezer.security.config;

import com.sezer.security.filter.CustomAuthenticationFilter;
import com.sezer.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig{
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authManagerBuilder;
    private final SecurityProperties securityProperties;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/addUser", "/api/user/assignRole", "/api/user/addRole").permitAll()
                .antMatchers(HttpMethod.GET, "/api/currency/get").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/currency/add").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/currency/update").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/currency/delete").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/alert/add").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/api/alert/delete").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/api/alert/getBySymbol", "/api/alert/getAll").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authManagerBuilder.getOrBuild(), securityProperties));
        http.addFilterBefore(new CustomAuthorizationFilter(securityProperties), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
