package com.endlesspowerskills.robowebstore.config;

import com.endlesspowerskills.robowebstore.entity.Admin;
import com.endlesspowerskills.robowebstore.repository.AdminRepository;
import com.endlesspowerskills.robowebstore.util.PageMappings;
import com.endlesspowerskills.robowebstore.util.RoleNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // -- fields
    private final AdminRepository adminRepository;

    // -- constructors
    @Autowired
    public SecurityConfiguration(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    // -- methods
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username ->
                adminRepository.getAdminByUsername(username)).passwordEncoder(passwordEncoder());
        Iterable<Admin> admins = adminRepository.findAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(PageMappings.ROBOWEBSTORE + PageMappings.PRODUCTS, PageMappings.ROBOWEBSTORE + PageMappings.LOGIN).permitAll()
                .antMatchers(PageMappings.ROBOWEBSTORE + PageMappings.ADD_PRODUCT).hasRole(RoleNames.ADMIN)
                .and()
                .formLogin()
                .loginPage(PageMappings.ROBOWEBSTORE + PageMappings.LOGIN)
                .defaultSuccessUrl(PageMappings.ROBOWEBSTORE + PageMappings.ADD_PRODUCT)
                .and()
                .logout().logoutSuccessUrl(PageMappings.ROBOWEBSTORE + PageMappings.PRODUCTS);
    }

}
