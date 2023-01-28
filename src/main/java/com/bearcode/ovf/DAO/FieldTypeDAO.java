package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.questionnaire.FieldType;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexey Polyakov
 *         Date: Aug 13, 2007
 *         Time: 4:45:42 PM
 */
@Repository
public class FieldTypeDAO extends BearcodeDAO {
    public List<FieldType> findFieldTypes() {
        return getSession().createCriteria(FieldType.class).list();
    }

    public FieldType findById(long aLong) {
        return (FieldType) getHibernateTemplate().get(FieldType.class, aLong);
    }

    /**
     * @deprecated Use makePersistent instead
     * @param fieldType
     */
    public void saveFieldType(FieldType fieldType) {
        getSession().saveOrUpdate(fieldType);
    }

    public boolean checkFieldType(FieldType fieldType) {
        return ( (Long) getSession().createCriteria(FieldType.class)
                .add(Restrictions.ne("id", fieldType.getId()))
                .add(Restrictions.eq("name", fieldType.getName()))
                .setProjection(Projections.countDistinct("id"))
                .uniqueResult() ) == 0;
    }

	/**
	 * Finds the (or a) field type corresponding to the template.
	 * 
	 * @author IanBrown
	 * @param template
	 *            the template.
	 * @return the field type or <code>null</code> if none is found.
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	@SuppressWarnings("unchecked")
	public FieldType findByTemplate(String template) {
		Criteria criteria = getSession().createCriteria(FieldType.class);
		criteria.add(Restrictions.eq("templateName", template));
		List<FieldType> fieldTypes = criteria.list();
		return fieldTypes.isEmpty() ? null : fieldTypes.get(0);
	}
}
