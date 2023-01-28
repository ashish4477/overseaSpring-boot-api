package com.bearcode.ovf.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.bearcode.commons.config.Environment;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.mail.MailTemplate;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailTemplates;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 30, 2008
 * Time: 5:00:11 PM
 * @author Leonid Ginzburg
 */
public class MailTemplateAccessService {

    public MailTemplate getTemplate( String fileName ) {
        final MailTemplate mail = new MailTemplate();
        mail.setName( fileName );

        final Environment xmlReader = new Environment(fileName);

        mail.setSubject( xmlReader.getStringProperty( Email.SUBJECT,"") );
        mail.setFrom( xmlReader.getStringProperty( Email.FROM,"") );
        mail.setReplyTo( xmlReader.getStringProperty( Email.REPLY_TO,"") );
        mail.setBodyTemplate( xmlReader.getCompoundStringProperty( Email.BODY_TEMPLATE,"") );

        return mail;
    }

    public void saveTemplate( MailTemplate mail ) throws IOException {
        StringBuffer out = new StringBuffer();

        out.append("<email>\n")
                .append("\t<").append(Email.SUBJECT).append(">")
                .append(mail.getSubject())
                .append("</").append(Email.SUBJECT).append(">\n")
                .append("\t<").append(Email.FROM).append(">")
                .append(mail.getFrom())
                .append("</").append(Email.FROM).append(">\n")
                .append("\t<").append(Email.REPLY_TO).append(">")
                .append(mail.getReplyTo())
                .append("</").append(Email.REPLY_TO).append(">\n")
                .append("\t<").append(Email.BODY_TEMPLATE).append("><![CDATA[\n")
                .append(mail.getBodyTemplate())
                .append("\n\t]]></").append(Email.BODY_TEMPLATE).append(">\n")
                .append("</email>");
        String realPath = getClass().getClassLoader().getResource( mail.getName() ).getFile();

        FileOutputStream output = new FileOutputStream( realPath );
        output.write( out.toString().getBytes() );
        output.close();
    }

    public Collection<String> getFaceTemplates( FaceConfig face ) {
        List<String> result = new LinkedList<String>();
        String file = EmailTemplates.XML_RAVA_THANK_YOU.replaceAll("/WEB-INF", "../"+face.getRelativePrefix() );
        InputStream inp = this.getClass().getClassLoader().getResourceAsStream( file );
        if ( inp != null ) {
            result.add( "RAVA" );
        }
        file = EmailTemplates.XML_FWAB_THANK_YOU.replaceAll("/WEB-INF", "../"+face.getRelativePrefix() );
        inp = this.getClass().getClassLoader().getResourceAsStream( file );
        if ( inp != null ) {
            result.add( "FWAB" );
        }
        return result;
    }
}
