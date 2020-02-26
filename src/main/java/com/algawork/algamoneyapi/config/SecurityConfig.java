package com.algawork.algamoneyapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("moises")
//                .password("{noop}admin")
//                .roles("ROLE");
//    }

    ///////////////// METODO SEM CRIPTOGRAFIA DA SENHA, USAR SOMENTE PARA AMBIENTE DE TESTE ///////////////////////

    //{noop} - utilizamos {noop} antes da senha para indicar que não será criptografada.
    // Link - http://bit.ly/2VvryBX

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//       algoritmo bcrypt, amplamente suportado , para fazer o hash das senhas.
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        final User.UserBuilder userBuilder = User.builder().passwordEncoder(encoder::encode);

        UserDetails admin = userBuilder
                .username("moises")
                .password("admin")
                .roles("ROLE")
                .build();

//        UserDetails user = userBuilder
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//              CRIAR USUARIO


//        System.out.println(admin.getPassword());
        return new InMemoryUserDetailsManager(admin);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/categorias")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable();

//        anyRequest - qualquer resquisao tera quer ser autenticada
//        antMatchers - qualquer resquisao para /categorias (add mais string com nome das urls) será permitida, o
//        restante precisa ser autenticado
//        sessionCreationPolicy(STATELESS) - nao criar sessao no servidor
//        csrf -
    }

}
