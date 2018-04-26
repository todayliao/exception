package com.exception.qms.config;

import com.exception.qms.security.AuthProvider;
import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/9
 * @time 下午2:21
 * @discription
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    /**
     * http 权限控制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 资源访问控制
        http.authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/tag/**").permitAll()
                .antMatchers("/search/question/allIndex/update").hasRole("USER")
//                .antMatchers("/question/**").permitAll()
//                .antMatchers("/static/**").permitAll()
                .antMatchers(HttpMethod.POST, "/question").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/question/edit").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/question/viewNum/increase").permitAll()
                .antMatchers(HttpMethod.POST, "/answer/edit").hasRole("USER")
                .antMatchers(HttpMethod.POST,"/file/editorMdImg/upload").hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/user/login") // 配置角色登录处理入口
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home") // 设置默认登录后 forword url
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and();

    }

    /**
     * 自定义认证策略
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider()).eraseCredentials(true);
    }

    @Bean
    public AuthProvider authProvider() {
        return new AuthProvider();
    }

}
