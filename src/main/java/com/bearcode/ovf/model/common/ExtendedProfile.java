package com.bearcode.ovf.model.common;

import org.apache.commons.lang.StringUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Date: 23.10.13
 * Time: 18:07
 *
 * @author Leonid Ginzburg
 */
public class ExtendedProfile implements Serializable {
    private static final long serialVersionUID = 3590070354201738053L;

    private static final String ARR_SEPARATOR = ";";

    private long id;
    private OverseasUser user;
    private Date created = new Date();

    private String politicalParty = "";
    private String votingMethod = "";
    private String voterType = "";
    private String voterClassificationType = "";

    private String voterParticipation = "";
    private String voterParticipationOther = "";
    @Transient
    private String[] voterParticipationArr;

    private String outreachParticipation;
    private String outreachParticipationOther = "";
    @Transient
    private String[] outreachParticipationArr;

    private String socialMedia = "";
    private String socialMediaOther = "";
    @Transient
    private String[] socialMediaArr;

    private String volunteering = "";
    private String volunteeringOther = "";
    @Transient
    private String[] volunteeringArr;

    private String satisfaction = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OverseasUser getUser() {
        return user;
    }

    public void setUser(OverseasUser user) {
        this.user = user;
    }

    public String getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(String politicalParty) {
        this.politicalParty = politicalParty;
    }

    public String getVotingMethod() {
        return votingMethod;
    }

    public void setVotingMethod(String votingMethod) {
        this.votingMethod = votingMethod;
    }

    public String getVoterType() {
        return voterType;
    }

    public void setVoterType(String voterType) {
        this.voterType = voterType;
    }

    public String getVoterClassificationType() {
        return voterClassificationType;
    }

    public void setVoterClassificationType(String voterClassificationType) {
        this.voterClassificationType = voterClassificationType;
    }

    public String getVoterParticipation() {
        return voterParticipation;
    }

    public void setVoterParticipation(String voterParticipation) {
        this.voterParticipation = voterParticipation;
        this.voterParticipationArr = StringUtils.split( voterParticipation, ARR_SEPARATOR );
    }

    public String getVoterParticipationOther() {
        return voterParticipationOther;
    }

    public void setVoterParticipationOther(String voterParticipationOther) {
        this.voterParticipationOther = voterParticipationOther;
    }

    public String getOutreachParticipation() {
        return outreachParticipation;
    }

    public void setOutreachParticipation(String outreachParticipation) {
        this.outreachParticipation = outreachParticipation;
        this.outreachParticipationArr = StringUtils.split( outreachParticipation, ARR_SEPARATOR );
    }

    public String getOutreachParticipationOther() {
        return outreachParticipationOther;
    }

    public void setOutreachParticipationOther(String outreachParticipationOther) {
        this.outreachParticipationOther = outreachParticipationOther;
    }

    public String getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(String socialMedia) {
        this.socialMedia = socialMedia;
        this.socialMediaArr = StringUtils.split( socialMedia, ARR_SEPARATOR );
    }

    public String getSocialMediaOther() {
        return socialMediaOther;
    }

    public void setSocialMediaOther(String socialMediaOther) {
        this.socialMediaOther = socialMediaOther;
    }

    public String getVolunteering() {
        return volunteering;
    }

    public void setVolunteering(String volunteering) {
        this.volunteering = volunteering;
        this.volunteeringArr = StringUtils.split( volunteering, ARR_SEPARATOR );
    }

    public String getVolunteeringOther() {
        return volunteeringOther;
    }

    public void setVolunteeringOther(String volunteeringOther) {
        this.volunteeringOther = volunteeringOther;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String[] getVoterParticipationArr() {
        return voterParticipationArr;
    }

    public void setVoterParticipationArr(String[] voterParticipationArr) {
        this.voterParticipationArr = voterParticipationArr;
        this.voterParticipation = StringUtils.join( voterParticipationArr, ARR_SEPARATOR );
    }

    public void setVoterParticipationArr(String voterParticipationArr) {
        this.voterParticipationArr = new String[] { voterParticipationArr };
        this.voterParticipation = voterParticipationArr;
    }

    public String[] getOutreachParticipationArr() {
        return outreachParticipationArr;
    }

    public void setOutreachParticipationArr(String[] outreachParticipationArr) {
        this.outreachParticipationArr = outreachParticipationArr;
        this.outreachParticipation = StringUtils.join( outreachParticipationArr, ARR_SEPARATOR );
    }

    public void setOutreachParticipationArr(String outreachParticipationArr) {
        this.outreachParticipationArr = new String[] {outreachParticipationArr};
        this.outreachParticipation = outreachParticipationArr;
    }

    public String[] getSocialMediaArr() {
        return socialMediaArr;
    }

    public void setSocialMediaArr(String[] socialMediaArr) {
        this.socialMediaArr = socialMediaArr;
        this.socialMedia = StringUtils.join(socialMediaArr, ARR_SEPARATOR);
    }

    public void setSocialMediaArr(String socialMediaArr) {
        this.socialMediaArr = new String[] {socialMediaArr};
        this.socialMedia = socialMediaArr;
    }

    public String[] getVolunteeringArr() {
        return volunteeringArr;
    }

    public void setVolunteeringArr(String[] volunteeringArr) {
        this.volunteeringArr = volunteeringArr;
        this.volunteering = StringUtils.join( volunteeringArr, ARR_SEPARATOR );
    }

    public void setVolunteeringArr(String volunteeringArr) {
        this.volunteeringArr = new String[] {volunteeringArr};
        this.volunteering = volunteeringArr;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
