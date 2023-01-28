package com.bearcode.ovf.actions.express;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.express.FedexLabel;
import com.bearcode.ovf.service.FedexService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 9, 2008
 * Time: 3:55:43 PM
 * @author Leonid Ginzburg
 */
public class GetFedexLabelImage extends AbstractController {

    private FedexService fedexService;

    public void setFedexService(FedexService fedexService) {
        this.fedexService = fedexService;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {


        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( (user instanceof OverseasUser ) ) {
            String number = MapUtils.getString(request.getParameterMap(), "tracking", "");
            FedexLabel fedexLabel = null;
            if (number.length() > 0) {
                fedexLabel = fedexService.findFedexLabelByNumber(number);
            }
            if (fedexLabel == null) {
                long id = MapUtils.getLong(request.getParameterMap(), "id", 0);
                if (id != 0) {
                    fedexLabel = fedexService.findFedexLabel(id);
                }
            }
            if ( fedexLabel == null
                    || fedexLabel.getOwner() == null
                    || ((!fedexLabel.isPaymentStatus()
                    		|| fedexLabel.isExpired()
                    		|| fedexLabel.getOwner().getId() != ((OverseasUser)user).getId()) 
                    		&& !((OverseasUser)user).isInRole(UserRole.USER_ROLE_ADMIN))) {
                // do not show the image if label is unpaid or expired or owned by another user and the logged in user is not an admin
                return null;
            }
            String pngFileName = fedexLabel.getFileName();

            File labelFile = new File(pngFileName);
            if ( labelFile.exists() ) {
                FileInputStream inpFile = new FileInputStream( labelFile );
                byte[] pdfBuffer = new byte[ 1024 ];

                response.setHeader("Cache-Control", "private");
                long currentTime = System.currentTimeMillis();
                long tenMinutes = 20*60*1000; // In milliseconds
                response.setDateHeader("Expires",currentTime + tenMinutes);
                response.setHeader("Content-Type","image/png");

                int readed;
                while ( (readed = inpFile.read(pdfBuffer)) > 0 ) {
                    response.getOutputStream().write( pdfBuffer, 0, readed );
                }
                response.getOutputStream().close();
            }
        }
        return null;
    }
}
