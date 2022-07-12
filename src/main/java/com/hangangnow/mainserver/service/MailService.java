package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.domain.member.dto.EmailAuthRequestDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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


    public String authEmail(EmailAuthRequestDto emailAuthDto){
        Random random = new Random();
        String code = String.valueOf(random.nextInt(888888) + 111111);

        return sendAuthEmail(emailAuthDto.getEmail(), code);
    }


    private String sendAuthEmail(String email, String code) {
        String subject = "한강나우 이메일 인증코드입니다.";
        String text = "회원인증을 위한 인증코드는 " + code + "입니다. <br/>";

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

        return code;
    }
}
