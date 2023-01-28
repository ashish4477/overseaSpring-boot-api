package com.bearcode.ovf.actions.express.admin;

import com.bearcode.ovf.actions.commons.OverseasFormController;
import com.bearcode.ovf.service.FedexService;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 20, 2008
 * Time: 7:36:24 PM
 * @author Leonid Ginzburg
 */
public class FedexCountryListController extends OverseasFormController {
    private FedexService fedexService;


    public void setFedexService(FedexService fedexService) {
        this.fedexService = fedexService;
    }

    public Map buildReferences(HttpServletRequest request, Object object, Errors errors) throws Exception {
        Map ref = new HashMap();
        ref.put( "fcountries", fedexService.findFedexCountries() );
        return ref;  
    }
}
