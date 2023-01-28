package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.mail.MailTemplate;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author leonid.
 */
@Repository
public class MailingTemplateDAO extends BearcodeDAO {

    public MailTemplate findById( Long id ) {
        return getHibernateTemplate().get( MailTemplate.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public List<MailTemplate> findAllTemplates() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( MailTemplate.class );
        return criteria.list();
    }
}
