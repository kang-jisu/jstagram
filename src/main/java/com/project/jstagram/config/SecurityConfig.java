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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
        web.ignoring().antMatchers("/css/**", "/script/**", "image/**","/js/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.andMatchers는 특정 경로 지정
                .antMatchers("/user/admin/**").hasRole("ADMIN")
                .antMatchers("/user/info").hasAnyRole("ADMIN","MEMBER")
                .antMatchers("/user/follow/**").hasAnyRole("ADMIN","MEMBER")
                .antMatchers("/insert").hasAnyRole("ADMIN","MEMBER")
                .antMatchers("/update").hasAnyRole("ADMIN","MEMBER")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                    .loginPage("/user/login") // 기존 spring security 로그인 페이지 말고 custom으로
                    .loginProcessingUrl("/user/login") // user/login.html에서 form post 로 보내주는 url
                    .defaultSuccessUrl("/user/success/login") // 저기 위에 hasrole에 걸려서 로그인하게되면 저기 페이지로 가고 그런거 없으면 default페이지로감
//                    .failureUrl("/user/denied/login") 로그인 정보 틀리면 /user/login?error 로 보내짐. 그거로 html에서 구분하면됨
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/") // 로그아웃하면 메인페이지로 이
                .invalidateHttpSession(true)//http세션 초기화
                //deleteCookies("key")이런거도있음 로그아웃시 특정 동쿠키 제거
                .and()
                .exceptionHandling().accessDeniedPage("/user/denied");

    }


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

}