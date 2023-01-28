package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.model.common.DependentRoot;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 18, 2007
 * Time: 3:37:00 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "dependents")
public abstract class Related implements Serializable {
    private static final long serialVersionUID = -3333223023515022799L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "dependent")
    @OrderBy("kind, depends_on_id, field_name, field_value")
    private Collection<BasicDependency> keys = null;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Collection<BasicDependency> getKeys() {
        return keys;
    }

    public void setKeys( Collection<BasicDependency> keys ) {
        this.keys = keys;
    }

    /**
     * Compose description which includes question name and conditions.
     * Used in administration interface
     *
     * @return description
     */
    @Transient
    public String getDependencyDescription() {
        if ( keys == null || keys.isEmpty() ) {
            return "default";
        }
        StringBuffer descr = new StringBuffer();
        DependentRoot grouping = null;
        boolean groupStart = false;
        for ( BasicDependency dependency : keys ) {
            if ( !dependency.checkGroup( grouping ) ) {
                grouping = dependency.getDependsOn();
                groupStart = true;
                if ( descr.length() > 0 ) {
                    descr.append( ";<br/> " );
                }
                descr.append( dependency.getDependsOnName() )
                        .append( ":" );
            }
            if ( groupStart ) {
                groupStart = false;
            } else {
                descr.append( ", " );
            }
            descr.append( dependency.getConditionName() );
        }
        return descr.toString();
    }

    @Transient
    public boolean isDefault() {
        return keys == null || keys.isEmpty();
    }

}
