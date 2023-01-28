package com.bearcode.ovf.model.common;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Date: 07.07.11
 * Time: 22:04
 *
 * @author Leonid Ginzburg
 */

@Entity
@Table(name="face_flow_logos")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class FaceFlowLogo implements Serializable {
    private static final long serialVersionUID = 1749605771887992494L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
    private byte[] logo;
	
	@Column(name = "content_type")
	private String contentType;

	@Column(name = "updated_time")
	private Date updatedTime;

	@JoinColumn(name = "face_config_id")
	@ManyToOne
	private FaceConfig faceConfig;

	@JoinColumn(name = "user_id")
	@ManyToOne
	private OverseasUser updatedBy;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getLogo() {
        return logo;
    }

    public void setLogo( byte[] logo ) {
        this.logo = logo;
    }

    public FaceConfig getFaceConfig() {
        return faceConfig;
    }

    public void setFaceConfig( FaceConfig faceConfig ) {
        this.faceConfig = faceConfig;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType( String contentType ) {
        this.contentType = contentType;
    }
}
