package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.RedisUtil;
import com.hangangnow.mainserver.domain.member.dto.EmailAuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;


    public EmailAuthDto authEmail(EmailAuthDto emailAuthDto){
        Random random = new Random();
        String code = String.valueOf(random.nextInt(888888) + 111111);

        createEmailAuthContent(emailAuthDto.getEmail(), code);
        redisUtil.setDataWithExpire(emailAuthDto.getEmail(), code, 300);

        return new EmailAuthDto(emailAuthDto.getEmail(), code);
    }


    public void authLoginId(String email, String name, String loginId){
        createLoginIdAuthContent(email, name, loginId);
    }


    public boolean checkEmailCode(EmailAuthDto emailAuthDto){
        String findCode = redisUtil.getDataWithKey(emailAuthDto.getEmail());

        if(findCode == null){
            throw new RuntimeException("코드 인증 시간이 만료되었습니다.");
        }

        if(findCode.equals(emailAuthDto.getCode())){
            redisUtil.deleteData(emailAuthDto.getEmail());
            return true;
        }

        else{
            return false;
        }
    }


    private void createEmailAuthContent(String email, String code) {
        String subject = "[한강나우] 이메일 인증코드입니다.";
        String text = "회원인증을 위한 인증코드는 " + code + "입니다. <br/>";

        send(email, subject, text);
    }

    private void createLoginIdAuthContent(String email, String name, String loginId) {
        String subject = "[한강나우] 아이디 찾기 메일입니다.";
        String text = name + "회원님의 한강나우 아이디는 " + loginId + "입니다. <br/>";

        send(email, subject, text);
    }


    private void send(String email, String subject, String text) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom("한강나우 <hangangnow@naver.com>");
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
