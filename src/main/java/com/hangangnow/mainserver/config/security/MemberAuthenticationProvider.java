package com.hangangnow.mainserver.config.security;

import com.hangangnow.mainserver.member.service.CustomMemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {


    private final PasswordEncoder passwordEncoder;
    private final CustomMemberDetailsService customMemberDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            throw new InternalAuthenticationServiceException("Authentication is null");
        }
        String username = authentication.getName();
        if (authentication.getCredentials() == null) {
            throw new AuthenticationCredentialsNotFoundException("Credentials is null");
        }
        String password = authentication.getCredentials().toString();
        UserDetails loadedUser = customMemberDetailsService.loadUserByUsername(username);
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        }

        /* checker */
        if (!loadedUser.isAccountNonLocked()) {
            throw new LockedException("사용할 수 없는 사용자 계정입니다.");
        }
        if (!loadedUser.isEnabled()) {
            throw new DisabledException("로그인이 차단 된 유저입니다.");
        }
        if (!loadedUser.isAccountNonExpired()) {
            throw new AccountExpiredException("사용자 계정이 만료되었습니다.");
        }

        /* 실질적인 인증 */
        if (!passwordEncoder.matches(password, loadedUser.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        /* checker */
        if (!loadedUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("사용자 자격 증명이 만료되었습니다.");
        }

        /* 인증 완료 */
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
