package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.RelatedDAO;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.model.questionnaire.PdfFilling;
import com.bearcode.ovf.model.questionnaire.Related;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 18, 2007
 * Time: 6:26:58 PM
 * @author Leonid Ginzburg
 */
@Service
public class RelatedService {

    @Autowired
    private RelatedDAO relatedDAO;

    public Collection<PdfFilling> findPdfFillings( CommonFormObject form ) {
        return relatedDAO.findFillings( form.createPagingInfo() );
    }

    public Collection<PdfFilling> findPdfFillings() {
        return relatedDAO.findFillings();
    }

    public void savePdfFilling( PdfFilling filling ) {
        relatedDAO.makePersistent( filling );
    }

    public Related findRelated( long id ) {
        return relatedDAO.findRelatedById( id );
    }

   	public void deletePdfFilling(PdfFilling filling) {
        if ( filling.getKeys().size() > 0 ) {
            relatedDAO.makeAllTransient( filling.getKeys() );
        }
        relatedDAO.makeTransient( filling );
    }
}
