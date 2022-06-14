package dev.road.map.service;

import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {     
	
	@Autowired
	private JavaMailSender javaMailSender;    

	String senderMail = "devroadmap0701@gmail.com";
	
	@Value("${backDomain}")
	String backDomain;

	// 인증 메일 발송 
	@Async
	public String SignupAuthMail(String receiverMail){     
		
		//6자리 난수 인증번호 생성
		String authKey = getAuthCode(6);
		System.out.println(authKey);
		try {
			
//            MailUtils mailUtils = new MailUtils(javaMailSender);
//			mailUtils.setSubject("[devroadmap]회원가입 인증 메일입니다.");
//			mailUtils.setText(new StringBuffer().append("<h1>[이메일 인증]</h1>")
//					.append("<p>아래 링크를 클릭하시면 아이디로 사용될 이메일 인증이 완료됩니다.</p>")
//					.append("<a href='" + domain + "/signup/mail/confirm?email=")
//					.append(receiverMail)
//					.append("&authKey=")
//					.append(authKey)
//					.append("' target='_blenk'>이메일 인증하기</a>")
//					.toString());
//			mailUtils.setFrom(senderMail, "devroadmap인증");
//			mailUtils.setTo(receiverMail);
//			mailUtils.send();
			
//			SimpleMailMessage simpleMessage = new SimpleMailMessage();
//			// 발신자
//			simpleMessage.setFrom(senderMail);
//			// 수신자
//			simpleMessage.setTo(receiverMail);
//			// 메일제목 
//			simpleMessage.setSubject("[devroadmap]회원가입 인증 메일입니다.");
//			// 메일 내용
//
//			simpleMessage.setText(new StringBuffer().append("<h1>[이메일 인증]</h1>")
//					.append("<p>아래 링크를 클릭하시면 아이디로 사용될 이메일 인증이 완료됩니다.</p>")
//					.append("<a href='" + domain + "/signup/mail/confirm?email=")
//					.append(receiverMail)
//					.append("&authKey=")
//					.append(authKey)
//					.append("' target='_blenk'>이메일 인증하기</a>")
//					.toString());
//			System.out.println(simpleMessage);
//			javaMailSender.send(simpleMessage);
//			

	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        //메일 제목 설정
	        helper.setSubject("[devroadmap]회원가입 인증 메일입니다.");

	        //수신자 설정
	        helper.setTo(receiverMail);

	        //메일 내용 설정 : 템플릿 프로세스
	        String html = new StringBuffer().append("<h1>[이메일 인증]</h1>")
					.append("<p>아래 링크를 클릭하시면 아이디로 사용될 이메일 인증이 완료됩니다.</p>")
					.append("<a href='" + backDomain + "/signup/mail/confirm?email=")
					.append(receiverMail)
					.append("&authKey=")
					.append(authKey)
					.append("' target='_blenk'>이메일 인증하기</a>")
					.toString();
	        helper.setText(html, true);
	        //메일 보내기
	        javaMailSender.send(message);
	        System.out.println("이메일 발송 완료");
			return authKey;					
		} catch (Exception e) {
			System.out.println("이메일 전송 실패");
			e.printStackTrace();
			return null;
		}
	}
	
	//인증키 생성
    private String getAuthCode(int size) {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num = 0;

        while(buffer.length() < size) {
            num = random.nextInt(10);
            buffer.append(num);
        }

        String AuthCode = buffer.toString();
        return AuthCode;
    }
}
