package org.infiniteam.autoservice;

import org.infiniteam.autoservice.model.Administrator;
import org.infiniteam.autoservice.model.ServiceEmployee;
import org.infiniteam.autoservice.model.User;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Map<Class<? extends User>, List<GrantedAuthority>> authorities = Map.of(
            VehicleOwner.class, AuthorityUtils.createAuthorityList("ROLE_USER"),
            ServiceEmployee.class, AuthorityUtils.createAuthorityList("ROLE_SERVICE_EMPLOYEE"),
            Administrator.class, AuthorityUtils.createAuthorityList("ROLE_ADMINISTRATOR")
    );

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/appLogin")
                .loginProcessingUrl("/appLogin")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> response.setStatus(200))
                .failureHandler((request, response, exception) -> response.setStatus(403))
                .permitAll();
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
//        http.authorizeRequests().anyRequest().authenticated();
        http.authorizeRequests().anyRequest().permitAll();
    }

}
