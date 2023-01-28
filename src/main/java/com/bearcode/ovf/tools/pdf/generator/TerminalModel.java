package com.bearcode.ovf.tools.pdf.generator;

import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 03.10.14
 * Time: 18:51
 *
 * @author Leonid Ginzburg
 */
public class TerminalModel {

    private Map<String,String> userFields = new HashMap<String, String>();
    private Map<String,String> formFields = new HashMap<String, String>();
    private LocalOffice localOfficial = null;

    public Map<String, String> getUserFields() {
        return userFields;
    }

    public void setUserFields(Map<String, String> userFields) {
        this.userFields = userFields;
    }

    public Map<String, String> getFormFields() {
        return formFields;
    }

    public void setFormFields(Map<String, String> formFields) {
        this.formFields = formFields;
    }

    public LocalOffice getLocalOfficial() {
        return localOfficial;
    }

    public void setLocalOfficial(LocalOffice localOfficial) {
        this.localOfficial = localOfficial;
    }
}
