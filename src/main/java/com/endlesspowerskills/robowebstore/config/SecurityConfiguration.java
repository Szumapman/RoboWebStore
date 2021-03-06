package com.endlesspowerskills.robowebstore.config;

import com.endlesspowerskills.robowebstore.repository.AdminRepository;
import com.endlesspowerskills.robowebstore.util.PageMappings;
import com.endlesspowerskills.robowebstore.util.RoleNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;


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
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(PageMappings.ROBOWEBSTORE + PageMappings.PRODUCTS,
                        PageMappings.ROBOWEBSTORE + PageMappings.PRODUCTS + PageMappings.ALL,
                        PageMappings.ROBOWEBSTORE + PageMappings.LOGIN,
                        PageMappings.ROBOWEBSTORE+PageMappings.SPECIAL_OFFER,
                        PageMappings.ROBOWEBSTORE+PageMappings.INVALID_PROMO_CODE,
                        "/error")
                .permitAll()
                .antMatchers(PageMappings.ROBOWEBSTORE + PageMappings.ADD_PRODUCT).hasRole(RoleNames.ADMIN)
                .and()
                .formLogin()
                .loginPage(PageMappings.ROBOWEBSTORE + PageMappings.LOGIN)
                .defaultSuccessUrl(PageMappings.ROBOWEBSTORE + PageMappings.ADD_PRODUCT)
                .and()
                .logout().logoutSuccessUrl(PageMappings.ROBOWEBSTORE + PageMappings.PRODUCTS);
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall(){
        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }
}
