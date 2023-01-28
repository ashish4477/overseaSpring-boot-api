package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.FaceBookApiDAO;
import com.bearcode.ovf.model.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by IntelliJ IDEA.
 * Date: Jan 20, 2012
 * Time: 4:35:26 PM
 * @author Daemmon Hughes
 */
@Service
public class FaceBookApiService{

    @Autowired
    private FaceBookApiDAO faceBookApiDAO;

    public FaceBookApi findForDomain(String domain) {
        return faceBookApiDAO.getByDomain(domain);
    }

    public FaceBookApi findForSubDomain(String domain) {

        // for non-local environments we remove the subdomain section of the domain
        if(!domain.endsWith(".ovf") && !domain.endsWith(".usvote")){
             domain = domain.substring(domain.indexOf(".")+1);
        }

        return findForDomain(domain);
    }

  }