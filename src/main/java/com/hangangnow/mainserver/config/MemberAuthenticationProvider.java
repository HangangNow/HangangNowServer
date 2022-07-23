package com.hangangnow.mainserver.config;

import com.hangangnow.mainserver.service.CustomMemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider{


    private final PasswordEncoder passwordEncoder;
    private final CustomMemberDetailsService customMemberDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication == null){
            throw new InternalAuthenticationServiceException("Authentication is null");
        }
        String username = authentication.getName();
        if(authentication.getCredentials() == null){
            throw new AuthenticationCredentialsNotFoundException("Credentials is null");
        }
        String password = authentication.getCredentials().toString();
        UserDetails loadedUser = customMemberDetailsService.loadUserByUsername(username);
        if(loadedUser == null){
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        }

        /* checker */
        if(!loadedUser.isAccountNonLocked()){
            throw new LockedException("User account is locked");
        }
        if(!loadedUser.isEnabled()){
            throw new DisabledException("User is disabled");
        }
        if(!loadedUser.isAccountNonExpired()){
            throw new AccountExpiredException("User account has expired");
        }

        /* 실질적인 인증 */
        if(!passwordEncoder.matches(password, loadedUser.getPassword())){
            throw new BadCredentialsException("Password does not match stored value");
        }
        /* checker */
        if(!loadedUser.isCredentialsNonExpired()){
            throw new CredentialsExpiredException("User credentials have expired");
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
