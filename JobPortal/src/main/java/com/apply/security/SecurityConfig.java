package com.apply.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.Authentication;

/**
 * Created by Vivek Agarwal on 5/10/2017.
 */
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter{
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws  Exception{
//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
//    }
//
//    @Override
//    public void configure(HttpSecurity httpSecurity) throws Exception{
//        httpSecurity.authorizeRequests()
//                .antMatchers("/applicant/*").hasRole("USER")
//                .antMatchers("/company/*").hasRole("USER")
//                .and()
//                .formLogin();
//    }
//
//
//}
