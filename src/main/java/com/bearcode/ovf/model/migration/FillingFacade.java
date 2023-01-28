package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.questionnaire.BasicDependency;
import com.bearcode.ovf.model.questionnaire.PdfFilling;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Date: 22.12.11
 * Time: 14:15
 *
 * @author Leonid Ginzburg
 */
public class FillingFacade extends AbleToMigrate implements RelatedFacade {
    private long id;
    private String name;
    private String text;
    private String inPdfName;

    private Collection<DependencyFacade> dependencies = new LinkedList<DependencyFacade>();

    public FillingFacade() {
    }

    public FillingFacade( PdfFilling filling ) {
        id = filling.getId();
        name = filling.getName();
        text = filling.getText();
        inPdfName = filling.getInPdfName();

        for ( BasicDependency key : filling.getKeys() ) {
            dependencies.add( new DependencyFacade( key ) );
        }
    }

    public PdfFilling createFilling() {
        PdfFilling filling = new PdfFilling();
        exportTo( filling );
        filling.setKeys( new LinkedList<BasicDependency>() );
        return filling;
    }

    public void exportTo( PdfFilling filling ) {
        filling.setName( name );
        filling.setText( text );
        filling.setInPdfName( inPdfName );
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public String getTitle() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    public String getInPdfName() {
        return inPdfName;
    }

    public void setInPdfName( String inPdfName ) {
        this.inPdfName = inPdfName;
    }

    public Collection<DependencyFacade> getDependencies() {
        return dependencies;
    }

    public void setDependencies( Collection<DependencyFacade> dependencies ) {
        this.dependencies = dependencies;
    }

    @Override
    public String getBaseClassName() {
        return PdfFilling.class.getSimpleName();
    }

    @Override
    public long assignMigrationId( Map<String, Long> outputMap, Collection<MigrationId> createdIds, long migrationId, int version ) {
        long mId = super.assignMigrationId( outputMap, createdIds, migrationId, version );
        for ( DependencyFacade dependencyFacade : dependencies ) {
            mId = dependencyFacade.assignMigrationId( outputMap, createdIds, mId, version );
        }
        return mId;
    }
}
