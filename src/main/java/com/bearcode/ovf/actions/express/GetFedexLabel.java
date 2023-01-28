package com.bearcode.ovf.actions.express;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.OverseasFormController;
import com.bearcode.ovf.actions.express.forms.ExpressForm;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.express.FedexLabel;
import com.bearcode.ovf.service.FedexService;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 14, 2008
 * Time: 5:47:13 PM
 * @author Leonid Ginzburg
 */
public class GetFedexLabel extends OverseasFormController {

    private FedexService fedexService;

    public void setFedexService(FedexService fedexService) {
        this.fedexService = fedexService;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ExpressForm form = new ExpressForm();

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( (user instanceof OverseasUser ) ) {
            FedexLabel fedexLabel = null;
            String number = MapUtils.getString( request.getParameterMap(), "tracking", "" );
            if ( number.length() > 0 ) {
                fedexLabel = fedexService.findFedexLabelByNumber( number );
            }
            if ( form.getFedexLabel() == null ) {
                long id = MapUtils.getLong( request.getParameterMap(), "id", 0 );
                if ( id != 0 ) {
                    fedexLabel = fedexService.findFedexLabel( id );
                }
            }
            if ( fedexLabel != null
                    && ( ((OverseasUser)user).isInRole( UserRole.USER_ROLE_ADMIN )
                    || ( fedexLabel.getOwner() != null
                    && fedexLabel.getOwner().getId() == ((OverseasUser)user).getId()) )
                    ) {
                form.setFedexLabel( fedexLabel );
            }
        }
        return form;
    }

    public Map buildReferences(HttpServletRequest request, Object object, Errors errors) throws Exception {
        ExpressForm form = (ExpressForm) object;
        Map ref = null;
        if (form.getFedexLabel() != null && form.getFedexLabel().getFileName().indexOf(".png") > 0) {
            ref = new HashMap();
            ref.put("imageLabel", true);
        }
        return ref;
    }


    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ExpressForm form = (ExpressForm) errors.getTarget();
        if ( form.getFedexLabel() != null ) {
            Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if ( !(form.getFedexLabel().isPaymentStatus() || ((OverseasUser)user).isInRole(UserRole.USER_ROLE_ADMIN)) ) {
                errors.rejectValue("fedexLabel", "eyv.label.not_paid" );
            }
            else if ( form.getFedexLabel().isExpired() && !((OverseasUser)user).isInRole(UserRole.USER_ROLE_ADMIN) ) {
                errors.rejectValue("fedexLabel", "eyv.label.download_period_expired" );
            }
            else if ( form.getFedexLabel().getFileName().indexOf(".pdf") > 0 ) {
                String pdfFileName = form.getFedexLabel().getFileName();

                File labelFile = new File(pdfFileName);
                if ( labelFile.exists() ) {
                    FileInputStream inpFile = new FileInputStream( labelFile );
                    byte[] pdfBuffer = new byte[64 * 1024];

                    response.setHeader("Content-Type", "application/force-download");
                    response.setHeader("Content-Type", "application/pdf");
                    response.setHeader("Content-Disposition", "attachment; filename="+form.getFedexLabel().getTrackingNumber()+".pdf;");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    int readed;
                    while ( (readed = inpFile.read(pdfBuffer)) > 0 ) {
                        response.getOutputStream().write( pdfBuffer, 0, readed );
                    }
                    response.getOutputStream().close();
                    return null;
                }
                else{
                    errors.rejectValue("fedexLabel", "eyv.label.does_not_exist" );
                }
            }
            else  {
                String pdfFileName = form.getFedexLabel().getFileName();

                File labelFile = new File(pdfFileName);
                if ( !labelFile.exists() ) {
                    errors.rejectValue("fedexLabel", "eyv.label.does_not_exist" );
                }
            }
        }
        else {
            errors.rejectValue("fedexLabel", "eyv.label.does_not_exist" );
        }
        return super.showForm(request, response, errors);
    }
}
