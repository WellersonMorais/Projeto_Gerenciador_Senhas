package security;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import db.ConfigPass;

public class EmailService {

    private EmailService() {
    throw new IllegalStateException("Utility class");
    }

    
    private static final String REMETENTE = ConfigPass.pegarEmail();
    private static final String SENHA_APP = ConfigPass.pegarSenhaApp();

    private static Session criarSessao() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMETENTE, SENHA_APP);
            }
        });
    }

    public static void enviarEmail(String destinatario, String corpo) throws MessagingException {
        Session session = criarSessao();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(REMETENTE));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject("Código de Verificação");
        message.setText(corpo);

        Transport.send(message);
        System.out.println("E-mail enviado com sucesso.");
    }

    public static void sendVerificationEmail(String destinatario, String codigo) throws MessagingException {
        String corpo = "Seu código de verificação é: " + codigo;
        enviarEmail(destinatario, corpo);
    }
}
