package com.bearcode.ovf.tools.pdf.generator.propagators;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.tools.pdf.generator.TerminalModel;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 03.10.14
 * Time: 18:59
 *
 * @author Leonid Ginzburg
 */
public abstract class FieldPropagator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public int getOrder() {
        return 0;
    }

    /**
   	 * Eliminates the formatting for the input value - undoes XML escaping and replaces HTML
   	 * <p>
   	 * </p>
   	 * with a trailing newline.
   	 *
   	 * @author IanBrown
   	 * @param value
   	 *            the value.
   	 * @return the unformatted value.
   	 * @since May 10, 2012
   	 * @version May 11, 2012
   	 */
   	public static String unformatValue(final String value) {
   		if (value == null) {
   			return null;
   		}

        return StringEscapeUtils.unescapeXml(value).replaceAll("</p>", "\n")
                .replaceAll("</?[^>]+>", "")                // remove any html/xml tags
                .replaceAll("[ \\t\\x0B\\f\\r]{2,}", " ")   // remove extra spaces
                .replaceAll("[ \\t\\x0B\\f\\r]*\\n\\s*", "\n").trim();
   	}


    abstract public void propagate( TerminalModel model, WizardContext wizardContext );
}
