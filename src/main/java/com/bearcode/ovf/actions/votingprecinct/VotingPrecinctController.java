/**
 * 
 */
package com.bearcode.ovf.actions.votingprecinct;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;

/**
 * Controller for voting precinct handling (REST).
 * 
 * @author IanBrown
 * 
 * @since Jul 27, 2012
 * @version Oct 9, 2012
 */
@Controller
public class VotingPrecinctController {

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Provides the list of cities for the specified state abbreviation and voting region name.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param votingRegionName
	 *            the voting region name.
	 * @return the cities.
	 * @since Jul 27, 2012
	 * @version Oct 9, 2012
	 */
	@RequestMapping(value = "/votingPrecinct/cities/state/{stateAbbreviation}/region/{votingRegionName" + ":.*}")
	public @ResponseBody
	List<String> citiesByVotingRegion(@PathVariable(value="stateAbbreviation") final String stateAbbreviation, @PathVariable(value="votingRegionName") final String votingRegionName) {
		return getVotingPrecinctService().findCitiesByVotingRegion(stateAbbreviation, votingRegionName);
	}

	/**
	 * Provides the list of cities for the specified state abbreviation and ZIP code.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param zip
	 *            the ZIP code.
	 * @return the cities.
	 * @since Jul 27, 2012
	 * @version Oct 9, 2012
	 */
	@RequestMapping(value = "/votingPrecinct/cities/state/{stateAbbreviation}/zip/{zip:.*}")
	public @ResponseBody
	List<String> citiesByZip(@PathVariable(value="stateAbbreviation") final String stateAbbreviation, @PathVariable(value="zip") final String zip) {
		return getVotingPrecinctService().findCitiesByZip(stateAbbreviation, zip);
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	public VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	public void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/**
	 * Provides a list of street names for the specified state abbreviation and city.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param city
	 *            the city.
	 * @return the street names.
	 * @since Jul 27, 2012
	 * @version Jul 31, 2012
	 */
	@RequestMapping(value = "/votingPrecinct/streets/state/{stateAbbreviation}/city/{city:.*}")
	public @ResponseBody
	List<String> streetNamesByCity(@PathVariable final String stateAbbreviation, @PathVariable final String city) {
		return getVotingPrecinctService().findStreetNamesByCity(stateAbbreviation, city);
	}
}
