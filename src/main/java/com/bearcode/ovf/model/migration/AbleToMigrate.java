package com.bearcode.ovf.model.migration;

import javax.persistence.Transient;
import java.util.Collection;
import java.util.Map;

/**
 * Date: 15.10.12
 * Time: 17:29
 *
 * @author Leonid Ginzburg
 */
public abstract class AbleToMigrate {
    private long migrationId;

    abstract public long getId();
    @Transient
    abstract public String getBaseClassName();

    public long getMigrationId() {
        return migrationId;
    }

    public void setMigrationId( long migrationId ) {
        this.migrationId = migrationId;
    }

    public long assignMigrationId( Map<String,Long> outputMap, Collection<MigrationId> createdIds, long migrationId, int version ) {
        Long existedId = outputMap.get( String.format( "%d_%s", getId(), getBaseClassName() ) );
        if ( existedId == null ) {
            MigrationId mId = new MigrationId();
            mId.setClassName( getBaseClassName() );
            mId.setObjectId( getId() );
            mId.setMigrationId( migrationId++ );
            mId.setVersion( version );
            this.setMigrationId( mId.getMigrationId() );
            createdIds.add( mId );
        }
        else {
            this.setMigrationId( existedId );
        }
        return migrationId;
    }
}
