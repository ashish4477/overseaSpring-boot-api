package com.bearcode.ovf.model.common;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Nov 6, 2007
 * Time: 8:02:15 PM
 * @author Leonid Ginzburg
 */

@Entity
@Table(name="facebook_api")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class FaceBookApi extends BusinessKeyObject implements Serializable {
    private static final long serialVersionUID = -7455992352170577220L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "app_key")
	private String appKey = "";

    @Column(name = "app_secret")
    private String appSecret = "";

    @Column(name = "domain")
    private String domain = "";

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "updated")
    private Date updated;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getAppKey(){
        return appKey;
    }

    public void setAppKey(String appKey){
        this.appKey = appKey;
    }

    public String getAppSecret(){
        return appSecret;
    }

    public void setAppSecret(String appSecret){
        this.appSecret = appSecret;
    }

    public String getDomain(){
        return domain;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public Date getUpdated(){
        return updated;
    }

    public void setUpdated(Date updated){
        this.updated = updated;
    }   
}
