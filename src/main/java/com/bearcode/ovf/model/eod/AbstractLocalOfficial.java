package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.Person;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 19, 2007
 * Time: 4:40:52 PM
 * @author Leonid Ginzburg
 */
public abstract class AbstractLocalOfficial implements Serializable {
    private static final long serialVersionUID = -3492764452524204053L;

    private long id = 0;
    private Address physical;
    private Address mailing;

    private String generalEmail = "";
    private String dsnPhone = "";

    private List<Officer> officers;

    private String website = "";
    private String hours = "";
    private String furtherInstruction = "";
    private Date updated;

    private int status;

    public AbstractLocalOfficial() {
        mailing = new Address();
        physical = new Address();
        officers = new LinkedList<Officer>();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Address getPhysical() {
        return physical;
    }

    public void setPhysical(Address physical) {
        this.physical = physical;
    }

    public Address getMailing() {
        return mailing;
    }

    public void setMailing(Address mailing) {
        this.mailing = mailing;
    }

    public Person getLeo() {
        Person person = new Person();
        if ( officers.size() >= 1 ) {
            person.setFirstName( officers.get( 0 ).getFirstName() );
            person.setLastName( officers.get( 0 ).getLastName() );
        }
        return person;
    }

    public String getLeoPhone() {
        return officers.size() >= 1 ? officers.get(0).getPhone() : "";
    }

    public String getLeoFax() {
        return officers.size() >= 1 ? officers.get(0).getFax() : "";
    }

    public String getLeoEmail() {
        return officers.size() >= 1 ? officers.get(0).getEmail() : "";
    }

    public String getDsnPhone() {
        return dsnPhone;
    }

    public void setDsnPhone(String dsnPhone) {
        this.dsnPhone = dsnPhone;
    }

    public Person getLovc() {
        Person person = new Person();
        if ( officers.size() >= 2 ) {
            person.setFirstName( officers.get( 1 ).getFirstName() );
            person.setLastName( officers.get( 1 ).getLastName() );
        }
        return person;
    }

    public String getLovcPhone() {
        return officers.size() >= 2 ? officers.get(1).getPhone() : "";
    }

    public String getLovcFax() {
        return officers.size() >= 2 ? officers.get(1).getFax() : "";
    }

    public String getLovcEmail() {
        return officers.size() >= 2 ? officers.get(1).getEmail() : "";
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getFurtherInstruction() {
        return furtherInstruction;
    }

    public void setFurtherInstruction(String furtherInstruction) {
        this.furtherInstruction = furtherInstruction;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

    public Person getAddContact() {
        Person person = new Person();
        if ( officers.size() >= 3 ) {
            person.setFirstName( officers.get( 2 ).getFirstName() );
            person.setLastName( officers.get( 2 ).getLastName() );
        }
        return person;
    }

    public String getAddPhone() {
        return officers.size() >= 3 ? officers.get(2).getPhone() : "";
    }

    public String getAddEmail() {
        return officers.size() >= 3 ? officers.get(2).getEmail() : "";
    }

    @JsonIgnore  //TODO remove annotation when mobile application will use same model for this class
    public List<Officer> getOfficers() {
        return officers;
    }

    public void setOfficers( List<Officer> officers ) {
        this.officers = officers;
    }

    @JsonIgnore   //TODO remove annotation when mobile application will use same model for this class
    public String getGeneralEmail() {
        return generalEmail;
    }

    public void setGeneralEmail( String generalEmail ) {
        this.generalEmail = generalEmail;
    }
}
