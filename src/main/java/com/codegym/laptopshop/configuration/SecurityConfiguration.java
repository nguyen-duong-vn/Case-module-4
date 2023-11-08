package com.codegym.laptopshop.configuration;

import com.codegym.laptopshop.repository.UserRepository;
import com.codegym.laptopshop.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableAutoConfiguration
@EnableWebSecurity
@ComponentScan(basePackageClasses = {UserDetailsServiceImpl.class, UserRepository.class})
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;


    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();


        http.authorizeHttpRequests()
                .antMatchers("/static/**", "/css/**", "/js/**", "/static/img/**",
                        "/login", "/logout")
                .permitAll();


        http.authorizeHttpRequests()
                .antMatchers("/home", "/check-email",
                        "/product/search/**", "product/list", "product/detail/**",
                        "/category/list",
                        "/user/list", "/user/detail/**",
                        "/user/search/**","/user/reset-password",
                        "user/principal/**", "/user/remove/**")
                .hasAnyRole("MANAGER", "SALE", "HR", "WAREHOUSE");

        http.authorizeHttpRequests()
                .antMatchers("/user/create", "/user/edit/**", "/user/layoff/**", "user/update-role/**")
                .hasAnyRole("MANAGER", "HR");



        http.authorizeHttpRequests()
                .antMatchers("/product/create", "/product/edit/**", "/product/remove/**",
                        "/category/create", "/category/edit/**", "/category/remove/**")
                .hasAnyRole("MANAGER", "WAREHOUSE");



        http.authorizeHttpRequests().and().exceptionHandling()
                .accessDeniedPage("/access-denied");

        http.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login")
                .defaultSuccessUrl("/home")

                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()

                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true");

        return http.build();
    }


}
