package com.project.jstagram.config;

import com.project.jstagram.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private MemberService memberService;
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) //로그인 상관 없이 허용을 해줘야할 리소스 위치를 정의한다.
    {
        web.ignoring().antMatchers("/css/**", "/script/**", "image/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.andMatchers는 특정 경로 지정
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/info").hasRole("ADMIN")
                .antMatchers("/user/info").hasRole("MEMBER")
                .antMatchers("/insert").hasRole("ADMIN")
                .antMatchers("/insert").hasRole("MEMBER")
                .antMatchers("/update").hasRole("ADMIN")
                .antMatchers("/update").hasRole("MEMBER")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/user/login")
                .defaultSuccessUrl("/user/login/result")
                //.failureUrl
                //.loginProcessinUrl()은 post로 수행하는 url
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/user/login")
                .invalidateHttpSession(true)//http세션 초기화
                //deleteCookies("key")이런거도있음 로그아웃시 특정 쿠키 제거
                .and()
                .exceptionHandling().accessDeniedPage("/user/denied");

    }


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

}