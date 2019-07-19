package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author jinrun.xie
 * @date 2019/7/18
 **/
@Configuration
@EnableWebSecurity
public class SecrutiyConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


 /*   @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.authorizeRequests().antMatchers("/product/**").permitAll().antMatchers("/admin/**").permitAll()
               .anyRequest().authenticated().and().formLogin().and().httpBasic();
        super.configure(http);
    }*/

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).
//                withUser("admin1").password(new BCryptPasswordEncoder().encode("admin1")).roles("ADMIN", "USER").and().
//                withUser("user1").password(new BCryptPasswordEncoder().encode("user1")).roles("USER");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
