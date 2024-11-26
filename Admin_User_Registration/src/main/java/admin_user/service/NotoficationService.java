package admin_user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotoficationService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to , String subject, String body){
        SimpleMailMessage message = new SimpleMailMessaege();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("your-email@gmail.com");
        mailSender.send(message);
    }
}
