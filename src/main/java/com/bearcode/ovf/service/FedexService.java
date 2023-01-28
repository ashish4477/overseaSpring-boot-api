package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.FedexCountryDAO;
import com.bearcode.ovf.DAO.FedexLabelDAO;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.express.CountryDescription;
import com.bearcode.ovf.model.express.FedexLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 18, 2008
 * Time: 9:34:27 PM
 * @author Leonid Ginzburg
 */
@Service
public class FedexService {

    @Autowired
    private FedexCountryDAO fedexCountryDAO;

    @Autowired
    private FedexLabelDAO fedexLabelDAO;


    public Collection<CountryDescription> findFedexCountries() {
        return fedexCountryDAO.getFedexCountries();
    }

    public Collection<CountryDescription> findActiveFedexCountries() {
        return fedexCountryDAO.getActiveFedexCountries();
    }

    public CountryDescription findFedexCountry( long id ) {
        return fedexCountryDAO.getFedexCountry( id );
    }

    public void saveFedexCountry( CountryDescription country ) {
        fedexCountryDAO.makePersistent( country);
    }

    public void saveFedexLabel( FedexLabel label ) {
        fedexLabelDAO.makePersistent( label );
    }

    public FedexLabel findFedexLabel( long id ) {
        return fedexLabelDAO.getLabel( id );
    }

    public FedexLabel findFedexLabelByNumber( String number ) {
        return fedexLabelDAO.getLabelByNumber( number );
    }

    public CountryDescription findFedexCountryByName(String countryName) {
        return fedexCountryDAO.getFedexCountryByName( countryName );
    }

    public Collection findFedexLabelsForUser(OverseasUser user) {
        return fedexLabelDAO.getLabelsForUser( user );  
    }
    
    public Collection findFedexLabelsForUserUnexpired(OverseasUser user) {
        return fedexLabelDAO.getLabelsForUserUnexpired( user );  
    }
    
    
}
