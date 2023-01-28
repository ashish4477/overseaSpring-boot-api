package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.questionnaire.PdfFilling;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 4, 2007
 * Time: 7:27:24 PM
 * @author Leonid Ginzburg
 */
@Component
public class PdfFillingValidator implements Validator {

    @Autowired
    private VelocityEngine velocityEngine;

    protected class XmlValidator extends DefaultHandler {
        public void validate( String text ) throws SAXException, IOException {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            //XmlValidator handler = new XmlValidator();
            reader.setContentHandler(this);
            reader.setErrorHandler(this);
            String checking = "<start>" + text + "</start>";
            checking = checking.replaceAll("\\#\\w+( *\\(.*\\))?", "");
            reader.parse(new InputSource( new ByteArrayInputStream( checking.getBytes() ) ));
        }

    }


    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public boolean supports(Class clazz) {
        return clazz.equals(PdfFilling.class );
    }

    public void validate(Object target, Errors errors) {
        XmlValidator validator = new XmlValidator();
        PdfFilling filling = (PdfFilling) target;
        try {
            validator.validate( filling.getText() );
        } catch (SAXException e) {
            errors.rejectValue( "text", "", "There are unclosed tags. " + e.getMessage() );
        } catch (IOException e) {
            errors.rejectValue( "text", "", "IO error " + e.getMessage() );
        }
        try {
            final ToolManager toolManager = new ToolManager();
            final ToolContext toolContext = toolManager.createContext();
            VelocityContext context = new VelocityContext( toolContext );
            StringWriter w = new StringWriter();
            Matcher matcher = Pattern.compile( "\\$\\!?\\{?(\\w*)\\}?" ).matcher( filling.getText() );
            Set<String> variables = new HashSet<String>();
            while ( matcher.find() ) {
                variables.add( matcher.group(1) );
            }
            for ( String var : variables ) {
                context.put( var, "" );   //put empty values
            }
            velocityEngine.evaluate(context, w, "validation", filling.getText());
        } catch (ParseErrorException e) {
            errors.rejectValue( "text", "", "Velocity syntax error: " + e.getMessage() );
        } catch ( MethodInvocationException e ) {
            errors.rejectValue( "text", "", "Velocity error: Something threw an Exception, " + e.getMessage() );
        } catch (Exception e) {
            errors.rejectValue( "text", "", "There is unexpected error. " + e.getMessage() );
        }

    }
}
