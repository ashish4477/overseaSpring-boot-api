package com.bearcode.ovf.model.migration;

import java.io.Serializable;

/**
 * Date: 13.10.12
 * Time: 15:37
 *
 * @author Leonid Ginzburg
 */
public class MigrationId implements Serializable {
    private static final long serialVersionUID = -1905026256720273944L;

    private long id;
    private long objectId;
    private long migrationId;
    private String className;
    private int version;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId( long objectId ) {
        this.objectId = objectId;
    }

    public long getMigrationId() {
        return migrationId;
    }

    public void setMigrationId( long migrationId ) {
        this.migrationId = migrationId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName( String className ) {
        this.className = className;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion( int version ) {
        this.version = version;
    }
}
