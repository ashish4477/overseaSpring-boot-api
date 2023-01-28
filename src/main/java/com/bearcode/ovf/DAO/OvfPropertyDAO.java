package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.system.OvfProperty;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author leonid.
 */
@Repository
public class OvfPropertyDAO extends BearcodeDAO
{
    public List<OvfProperty> findAll() {
        return getHibernateTemplate().loadAll( OvfProperty.class );
    }
}
