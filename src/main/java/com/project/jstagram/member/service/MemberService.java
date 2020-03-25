package com.project.jstagram.member.service;

import com.project.jstagram.member.domain.Role;
import com.project.jstagram.member.model.Member;
import com.project.jstagram.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class MemberService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;


    public Optional<Member> findByNickname(String nickname){
        return memberRepository.findByNickname(nickname);
    }

    // spring security 로그인 method
    @Override //상세정보 조회 메서드. 사용자의 계정정보와 권한을 갖는 userDetail반환
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        //로그인시 입력한 아이디로 (pk가아닌)식별값. 예제는 이메일

        Optional<Member> member = memberRepository.findByEmail(useremail);
            Member m = member.get();
            List<GrantedAuthority> authorities = new ArrayList<>();


            if (("rkdwltn0425@gmail.com").equals(useremail)) {
                authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
                log.info("login admin user ||  email : " + useremail);
            } else {
                if (m.getVerify().equals("y"))
                    authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
                log.info("login member user ||  email : " + useremail);
            }
            return new User(m.getEmail(), m.getPassword(), authorities); //이 값 데리고 비밀번호랑 조회에서 스프링에서 알아서 로그인 시켜줌

    }

    @Transactional //비밀번호 암호화 회원가입 처리
    public Long signUpMember(Member member, String joinCode) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setCreatedDate(LocalDateTime.now());
        member.setModifiedDate(LocalDateTime.now());
        member.setVerify(joinCode);
        return memberRepository.save(member).getId();
    }

    //회원 탈퇴
    public void deleteMember(Long id) {
        log.info("delete member id : "+ id);
        this.memberRepository.deleteById(id);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    /// user
    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    public void updateUserInfo(Member member){
        Member oldMember = memberRepository.getOne(member.getId());
        oldMember.setNickname(member.getNickname());
        oldMember.setName(member.getName());
        oldMember.setText(member.getText());
        oldMember.setBirth(member.getBirth());
        oldMember.setModifiedDate(LocalDateTime.now());
        memberRepository.save(oldMember);
    }
}