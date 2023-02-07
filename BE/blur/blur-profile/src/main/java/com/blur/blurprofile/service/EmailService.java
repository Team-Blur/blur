package com.blur.blurprofile.service;

import com.blur.blurprofile.config.EmailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public static final String ePw = createKey();


    public static String createKey() {

        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    private String createMessage(String to)throws Exception{

        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 안녕하세요 Blur입니다. </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";

        return msgg;
    }

    public String sendAuthMessage(String to)throws Exception {
        String message = createMessage(to);
        EmailHandler emailHandler = new EmailHandler(mailSender);
        emailHandler.setTo(to);
        emailHandler.setSubject("이메일 인증 테스트");
        emailHandler.setText(message, true);//내용
        emailHandler.setFrom("blurb307@gmail.com");//보내는 사람
        System.out.println("11111111111111111111111111111");
        System.out.println(to);
        try{//예외처리
            emailHandler.send();
            System.out.println("이메일");
            return "1111";
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

}
