package fr.m2i.hotelback.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class ApplicationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void configure( AuthenticationManagerBuilder auth  ) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder( passwordEncoder() );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {

            http.authorizeRequests().antMatchers("/images/**" , "/images/uploads/**" , "/api/login" ).permitAll();

            http.cors().and()
                    .antMatcher("/api/**")
                    .csrf()
                    .disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // On utilise pas les sessions, toute req est déconnectée suite à l'exécution
                    .and().authorizeRequests(authorize -> authorize
                            .anyRequest().authenticated()
                    )
                    .httpBasic();
        }
    }

    @Configuration
    @Order(2)
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //formLogin = utiliser un formulaire d'authetification - loginPage : chemin de l'authentification
            http.formLogin().loginPage("/login").defaultSuccessUrl("/");
            // Autoriser un accès anonyme sur les routes /login et /css/**
            http.authorizeRequests().antMatchers("/login" , "/css/**" , "/images/**" , "/images/uploads/**" , "/api/login" ).permitAll();

            //http.authorizeRequests().antMatchers("/api/login" ).permitAll();

            // Autoriser les actions post pour les admins : ROLE_ADMIN
            http.authorizeRequests().antMatchers("**/add" , "**/edit/**" , "**/delete/**").hasRole("ADMIN");

            // Tous les utilisateurs qui ne sont pas mentionnées en haut devrait s'authentifier
            http.authorizeRequests().anyRequest().authenticated();

            // désactiver la protection csrf
            http.csrf().disable();
        }
    }



}