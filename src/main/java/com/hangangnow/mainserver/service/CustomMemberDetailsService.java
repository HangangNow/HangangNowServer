package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return memberRepository.findByLoginId(loginId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(loginId + " 을 DB에서 찾을 수 없습니다"));
    }


    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetailsByEmail)
                .orElseThrow(() -> new UsernameNotFoundException(email + " 을 DB에서 찾을 수 없습니다"));
    }


    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }


    private UserDetails createUserDetailsByEmail(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(
                String.valueOf(member.getId()),
                member.getEmail(),
                Collections.singleton(grantedAuthority)
        );
    }
}
