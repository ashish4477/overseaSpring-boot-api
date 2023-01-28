package com.bearcode.ovf.actions.express.admin;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.OverseasFormController;
import com.bearcode.ovf.model.express.CountryDescription;
import com.bearcode.ovf.service.FedexService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 20, 2008
 * Time: 7:39:21 PM
 * @author Leonid Ginzburg
 */
public class EditCountryController extends OverseasFormController {
    private FedexService fedexService;


    public void setFedexService(FedexService fedexService) {
        this.fedexService = fedexService;
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
        dateFormat.setLenient(false);
        binder.registerCustomEditor( Date.class, new CustomDateEditor(dateFormat, true));

        DecimalFormat floatFormat = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        CustomNumberEditor numberEditor = new CustomNumberEditor(Double.class, floatFormat, true);
        binder.registerCustomEditor( Double.TYPE, numberEditor);
        binder.registerCustomEditor( Double.class, numberEditor);
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        long countryId = MapUtils.getInteger( request.getParameterMap(), "countryId", 0 );
        if ( countryId != 0 ) {
            return fedexService.findFedexCountry( countryId );
        }
        return new CountryDescription();
    }

    public Map buildReferences(HttpServletRequest request, Object object, Errors errors) throws Exception {
        return null;
    }


    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        if ( request.getParameterMap().containsKey("save") ) {
            CountryDescription country = (CountryDescription) command;
            fedexService.saveFedexCountry( country );

            return new ModelAndView( new RedirectView("/admin/EyvCountryList.htm", true));
        }
        return showForm( request, response, errors);
    }
}
