package com.hangangnow.mainserver;

import com.hangangnow.mainserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;


//    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        public void dbInit1() {
            Member member = new Member();
            member.createMember("kosa0914", "kosa0914@naver.com", passwordEncoder.encode("helloWorld"), "김동욱");
            em.persist(member);

        }
    }
}
