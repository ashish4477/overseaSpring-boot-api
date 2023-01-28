package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.GenericStringItem;

/**
 * Date: 19.12.11
 * Time: 21:07
 *
 * @author Leonid Ginzburg
 */
public class FieldDictionaryItemFacade extends AbleToMigrate {
    private long id;
    private String value;

    public FieldDictionaryItemFacade() {
    }

    public FieldDictionaryItemFacade( FieldDictionaryItem item ) {
        if ( item instanceof GenericStringItem ) {
            id = item.getId();
            value = item.getValue();
        }
    }

    public FieldDictionaryItem createItem() {
        GenericStringItem item =  new GenericStringItem();
        exportTo( item );
        return item;
    }

    public void exportTo( GenericStringItem item ) {
        item.setValue( value );
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue( String value ) {
        this.value = value;
    }

    @Override
    public String getBaseClassName() {
        return GenericStringItem.class.getSimpleName();
    }
}
