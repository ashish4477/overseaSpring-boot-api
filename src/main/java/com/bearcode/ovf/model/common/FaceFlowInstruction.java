package com.bearcode.ovf.model.common;

import com.bearcode.ovf.model.questionnaire.FlowType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Date: 06.07.11
 * Time: 22:40
 *
 * @author Leonid Ginzburg
 */
@Entity
@Table(name="face_flow_instructions")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class FaceFlowInstruction implements Serializable {
    private static final long serialVersionUID = 9161971929347211327L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "face_config_id")
	@ManyToOne
	private FaceConfig faceConfig;

	@Column(name = "face_flow", nullable = false)
	@Enumerated(EnumType.STRING)
	private FlowType flowType;

	@Column
	private String text = "";

	@Column(name = "updated_time")
	private Date updatedTime;

	@JoinColumn(name = "user_id")
	@ManyToOne
	private OverseasUser updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FaceConfig getFaceConfig() {
        return faceConfig;
    }

    public void setFaceConfig( FaceConfig faceConfig ) {
        this.faceConfig = faceConfig;
    }

    @Required
    @NotEmpty
    public String getFlowTypeName() {
        if( flowType == null ) return "";
        return flowType.toString();
    }

    public void setFlowTypeName( String flowTypeName ) {
        this.flowType = FlowType.valueOf( flowTypeName.toUpperCase() );
    }

    public FlowType getFlowType() {
        return flowType;
    }

    public void setFlowType( FlowType flowType ) {
        this.flowType = flowType;
    }
    @Required
    @NotEmpty
    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }


    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime( Date updatedTime ) {
        this.updatedTime = updatedTime;
    }

    public OverseasUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy( OverseasUser updatedBy ) {
        this.updatedBy = updatedBy;
    }


}
