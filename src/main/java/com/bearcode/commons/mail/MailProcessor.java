package com.bearcode.commons.mail;

//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessagePreparator;
//import org.springframework.mail.javamail.MimeMessageHelper;
/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 16, 2007
 * Time: 6:35:54 PM
 */
/* NOT USED
public class MailProcessor {

    private JavaMailSender mailSender;
    private VelocityEngine velocityEngine;

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void sendEmail(final String template, final Map model) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo("leongin@bear-code.com");
                message.setFrom("ovf_mail_robot@bear-code.com"); // todo: could be parameterized...
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, template, model);
                message.setText(text, true);
            }
        };
        this.mailSender.send(preparator);
    }
}
*/
