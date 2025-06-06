package it.epicode.Circle.emails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        // Splitta la stringa "to" usando virgola o punto e virgola come delimitatori
        String[] recipients = to.split("\\s*[,;]\\s*");
        helper.setTo(recipients);

        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
        System.out.println("Email inviata con successo a " + to);
    }

    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachmentBytes, String attachmentName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        // 'true' abilita il supporto multipart per gli allegati
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Splitta la stringa "to" usando virgola o punto e virgola come delimitatori
        String[] recipients = to.split("\\s*[,;]\\s*");
        helper.setTo(recipients);

        helper.setSubject(subject);
        helper.setText(body, true);
        helper.addAttachment(attachmentName, new ByteArrayResource(attachmentBytes));
        mailSender.send(message);
        System.out.println("Email inviata con successo a " + to + " con allegato: " + attachmentName);
    }

    public void sendEmailRegistration(emailRequest request) throws MessagingException {

        if (request.getTo() == null || request.getTo().isEmpty()) {
            throw new IllegalArgumentException("Recipient email address cannot be null or empty");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String[] recipients = request.getTo().split("\\s*[,;]\\s*");
        String name = request.getFullName();

        String subject = "Welcome to Circle!";
        String body = "<p>Dear <strong>" + name + "</strong>,</p>" +
                "<p>Welcome to <strong>Circle</strong>!</p>" +
                "<p>Thank you for registering with us.</p>" +
                "<p>Best regards,<br/>Circle Team</p>";

        helper.setTo(recipients);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
        System.out.println("Email sent successfully to " + request.getTo());
    }

    public void sendUpdateProfile(emailRequest request) throws MessagingException {

        if (request.getTo() == null || request.getTo().isEmpty()) {
            throw new IllegalArgumentException("Recipient email address cannot be null or empty");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String[] recipients = request.getTo().split("\\s*[, ;]\\s*");
        String name = request.getFullName();

        String subject = "Profile Updated";
        String body = "<p>Dear " + name + ",</p>" +
                "<p>Your profile has been successfully updated.</p>" +
                "<p>Best regards,<br/>Circle Team</p>";

        helper.setTo(recipients);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
        System.out.println("Email sent successfully to " + request.getTo());
    }

    public void sendInvitation(emailRequest request) throws MessagingException {

        if (request.getTo() == null || request.getTo().isEmpty()) {
            throw new IllegalArgumentException("Recipient email address cannot be null or empty");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String[] recipients = request.getTo().split("\\s*[, ;]\\s*");
        String name = request.getFullName();

        String subject = "Invitation to Circle";
        String body = "<p>Dear " + name + ",</p>" +
                "<p>You have been invited to join <strong>Circle</strong>.</p>" +
                "<p>Please use the following link to register:</p>" +
                "<p><a href=\"https://circle-social-51yq.onrender.com/\">Join Circle</a></p>" +
                "<p>Best regards,<br/>Circle Team</p>";

        helper.setTo(recipients);
        helper.setSubject(subject);
        helper.setText(body, true); // true = send as HTML

        mailSender.send(message);
        System.out.println("Email sent successfully to " + request.getTo());
    }
}
