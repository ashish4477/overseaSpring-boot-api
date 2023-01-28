package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.FieldTypeDAO;
import com.bearcode.ovf.DAO.QuestionFieldDAO;
import com.bearcode.ovf.model.questionnaire.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author Alexey Polyakov
 *         Date: Aug 13, 2007
 *         Time: 4:43:25 PM
 */
@Service
public class QuestionFieldService {

    @Autowired
    private FieldTypeDAO fieldTypeDAO;

    @Autowired
    private QuestionFieldDAO questionFieldDAO;


	//@Transactional(readOnly=false)
    public void saveQuestionField( QuestionField field ) {
        boolean newField = field.getId() == 0;
        boolean down = false;

        if ( newField ) {
            field.setOldOrder( field.getOrder() );
        } else {
            down = field.getOldOrder() < field.getOrder();
            if( down ) {
                field.setOrder(field.getOrder() - 1);
            }
        }

        if ( !field.getType().isGenericOptionsAllowed() && field.getGenericOptions() != null && field.getGenericOptions().size() > 0 ) {
            // Type has changed, options not allowed any more - delete them
            questionFieldDAO.makeAllTransient( field.getGenericOptions() );
            field.getGenericOptions().clear();
        }
        int startNumber = Math.min( field.getOldOrder(), field.getOrder() );
        Collection<QuestionField> fieldsAfter = questionFieldDAO.findFieldsBeforeNumber( field, startNumber );

        for ( QuestionField shifted : fieldsAfter ) {
            if ( shifted == field ) {
                if(down) startNumber++;
                continue;
            }

            shifted.setOrder(down ? startNumber ++ : ++ startNumber);

        }
        if ( newField ) {
            fieldsAfter.add( field );
        }
        questionFieldDAO.makeAllPersistent( fieldsAfter );
    }

	//@Transactional(readOnly=false)
    public void deleteQuestionField( QuestionField field ) {
        int number = field.getOrder();
        Collection<QuestionField> fieldsBefore = questionFieldDAO.findFieldsBeforeNumber( field, number+1 );
        for ( QuestionField shifted : fieldsBefore ) {
            shifted.setOrder( number++ );
        }
        questionFieldDAO.makeAllPersistent( fieldsBefore );
        questionFieldDAO.makeAllTransient( field.getGenericOptions() );
        questionFieldDAO.makeTransient( field );
    }

    public boolean checkUsingInDependecies( QuestionField field ) {
        return questionFieldDAO.checkFieldUsing( field );
    }

    public boolean checkUsingInDependecies( FieldDictionaryItem item ) {
        return questionFieldDAO.checkOptionUsing( item );
    }


    public QuestionField findQuestionFieldById( long id ) {
        return questionFieldDAO.getById( id );
    }

    public List<FieldType> findFieldTypes() {
        return fieldTypeDAO.findFieldTypes();
    }

    public FieldType findFieldTypeById(long aLong) {
        return fieldTypeDAO.findById(aLong);
    }

	//@Transactional(readOnly=false)
    public void saveFieldType(FieldType fieldType) {
        fieldTypeDAO.makePersistent(fieldType);
    }

    public boolean checkFieldType(FieldType fieldType) {
        return fieldTypeDAO.checkFieldType(fieldType);
    }

    public FieldDictionaryItem findDictionaryItemById(long itemId) {
        return questionFieldDAO.getDictionaryItemById( itemId );
    }

    public FieldDependency findFieldDependencyById(long dependencyId) {
        return questionFieldDAO.getFieldDependencyById( dependencyId );
        
    }

	//@Transactional(readOnly=false)
    public void saveDependency(FieldDependency dependency) {
        questionFieldDAO.makePersistent( dependency );
    }

	//@Transactional(readOnly=false)
    public void deleteDependency(FieldDependency dependency) {
        questionFieldDAO.makeTransient( dependency );
    }

    public FieldDependency findFieldDependencyForField(QuestionField field) {
        return questionFieldDAO.getDependencyForField( field ); 
    }

	//@Transactional
    public Collection<FieldDependency> findFieldDependencies() {
        return questionFieldDAO.findFieldDependencies();
    }

    public Collection<QuestionField> findAnswerFiedsOfPage(Collection<Long> answerFields, QuestionnairePage page) {
        return questionFieldDAO.findAnswerFiedsOfPage( answerFields, page );
    }

    public Collection<QuestionField> findReportableFields() {
        return questionFieldDAO.findReportableFields();
    }

    public FieldType getCountryFieldType() {
        return findFieldTypeById(FieldType.COUNTRY_FIELDTYPE_ID);
    }

	/**
	 * Finds the (or a) field type corresponding to the template.
	 * 
	 * @author IanBrown
	 * @param template
	 *            the template.
	 * @return the field type.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	public FieldType findFieldTypeByTemplate(final String template) {
		return fieldTypeDAO.findByTemplate(template);
	}
}
