package com.exception.qms.config;

import com.exception.qms.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/9
 * @time 下午2:21
 * @discription
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * http 权限控制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                    .loginPage("/user/login") // 配置角色登录处理入口
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home") // 设置默认登录后 forword url
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailHandler())
                    .and()
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds(7*24*60*60)
                    .and()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/user/login").permitAll()
                    .antMatchers("/home/**").permitAll()
                    .antMatchers(HttpMethod.GET,"/user/**").permitAll()
                    .antMatchers("/tag/**").permitAll()
                    .antMatchers("/about/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/question/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/answer/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/search/**").permitAll()
                    .antMatchers("/favicon.ico").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/plugins/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/question/viewNum/increase").permitAll()
                    .antMatchers("/sitemap.xml").permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    //                .logoutSuccessUrl("/home")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .logoutSuccessHandler(logoutSuccessHandler())
                    .clearAuthentication(true)
                    .deleteCookies()
                    .invalidateHttpSession(true)
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(loginUrlEntryPoint())
                    .and()
                .headers()
                    .frameOptions()
                    .disable()
                    .and();

//        http.csrf().disable();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new MyAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailHandler() {
        return new MyAuthenticationFailureHandler("/user/login");
    }

    @Bean
    public MyLogoutSuccessHandler logoutSuccessHandler() {
        return new MyLogoutSuccessHandler();
    }

    @Bean
    public MyLoginUrlAuthenticationEntryPoint loginUrlEntryPoint() {
        return new MyLoginUrlAuthenticationEntryPoint("/user/login");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     spring security 记住我 表结构：

     CREATE TABLE `persistent_logins` (
     `username` varchar(64) NOT NULL,
     `series` varchar(64) NOT NULL,
     `token` varchar(64) NOT NULL,
     `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (`series`)
     ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

}
