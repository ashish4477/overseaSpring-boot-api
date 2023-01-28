package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.migration.MigrationId;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 15.10.12
 * Time: 15:36
 *
 * @author Leonid Ginzburg
 */
@Repository
@SuppressWarnings( "unchecked" )
public class MigrationDAO extends BearcodeDAO {

    public Collection<MigrationId> findAll() {
        return getSession().createCriteria( MigrationId.class ).list();
    }

    public void removeNewest( int version ) {
        getSession().createQuery( "DELETE FROM MigrationId WHERE version >= :version" )
                .setInteger( "version", version )
                .executeUpdate();
    }

    public int findVersion() {
        Criteria criteria = getSession().createCriteria( MigrationId.class )
                .setProjection( Projections.max( "version" ) );
        Integer result = (Integer) criteria.uniqueResult();
        return result == null ? 0 : result;
    }

    public long findMaxMigrationId() {
        Criteria criteria = getSession().createCriteria( MigrationId.class )
                .setProjection( Projections.max( "migrationId" ) );
        Long result = (Long) criteria.uniqueResult();
        return result == null ? 0L : result;
    }

    public List<MigrationId> findObjectConflicts() {
        List<MigrationId> conflicts = new LinkedList<MigrationId>();
        String sql = "select objectId, className, max(version) as version from MigrationId mId " +
                "group by objectId, className " +
                "having count(migrationId) > 1";
        Query query = getSession().createQuery( sql );
        for ( Object row : query.list() ) {
            if ( row instanceof Object[] ) {
                MigrationId mId = new MigrationId();
                mId.setObjectId( (Long)((Object[])row)[0] );
                mId.setClassName( (String) ((Object[]) row)[1] );
                mId.setVersion( (Integer) ((Object[]) row)[2] );
                conflicts.add( mId );
            }
        }
        return conflicts;
    }

    public List<MigrationId> findMigrationConflicts() {
        List<MigrationId> conflicts = new LinkedList<MigrationId>();
        String sql = "select migrationId, max(version) as version from MigrationId mId " +
                "group by migrationId " +
                "having count(id) > 1";
        Query query = getSession().createQuery( sql );
        for ( Object row : query.list() ) {
            if ( row instanceof Object[] ) {
                MigrationId mId = new MigrationId();
                mId.setMigrationId( (Long) ((Object[]) row)[0] );
                mId.setVersion( (Integer) ((Object[]) row)[1] );
                conflicts.add( mId );
            }
        }
        return conflicts;
    }

    public void clearObjectConflicts( List<MigrationId> conflicts ) {
        String sql = "delete from MigrationId " +
                "where objectId = :objectId " +
                "and className = :className " +
                "and version < :version";
        Session session = getSession();
        for ( MigrationId mId : conflicts ) {
            session.createQuery( sql )
            .setLong( "objectId", mId.getObjectId() )
            .setString( "className", mId.getClassName() )
            .setInteger( "version", mId.getVersion() )
            .executeUpdate();
        }
    }

    public void clearMigrationConflicts( List<MigrationId> conflicts ) {
        String sql = "delete from MigrationId " +
                "where migrationId = :migrationId " +
                "and version < :version";
        Session session = getSession();
        for ( MigrationId mId : conflicts ) {
            session.createQuery( sql )
            .setLong( "migrationId", mId.getMigrationId() )
            .setInteger( "version", mId.getVersion() )
            .executeUpdate();
        }
    }
}
