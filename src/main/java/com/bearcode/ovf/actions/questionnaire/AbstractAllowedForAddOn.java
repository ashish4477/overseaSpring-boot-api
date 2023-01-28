package com.bearcode.ovf.actions.questionnaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.SvrPropertiesService;

/**
 * Abstract implementation of {@link AllowedForAddOn}.
 * 
 * @author Ian Brown
 * 
 */
@Component
public abstract class AbstractAllowedForAddOn implements AllowedForAddOn {

	/**
	 * the question field service.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	@Autowired
	private QuestionFieldService questionFieldService;

	/**
	 * the SVR properties service.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	@Autowired
	private SvrPropertiesService svrPropertiesService;

	/**
	 * the valet object used to acquire resources.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private PageAddOnValet valet;

	/**
	 * Gets the question field service.
	 * 
	 * @author IanBrown
	 * @return the question field service.
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	public final QuestionFieldService getQuestionFieldService() {
		return questionFieldService;
	}

	/**
	 * Gets the SVR properties service.
	 * 
	 * @author IanBrown
	 * @return the SVR properties service.
	 * @since Oct 22, 2012
	 * @version May 7, 2013
	 */
	public final SvrPropertiesService getSvrPropertiesService() {
		return svrPropertiesService;
	}

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	public final PageAddOnValet getValet() {
		return valet;
	}

	/**
	 * Sets the question field service.
	 * 
	 * @author IanBrown
	 * @param questionFieldService
	 *          the question field service to set.
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	public final void setQuestionFieldService(
	    final QuestionFieldService questionFieldService) {
		this.questionFieldService = questionFieldService;
	}

	/**
	 * Sets the SVR properties service.
	 * 
	 * @author IanBrown
	 * @param svrPropertiesService
	 *          the SVR properties service to set.
	 * @since Oct 22, 2012
	 * @version May 7, 2013
	 */
	public final void setSvrPropertiesService(
	    final SvrPropertiesService svrPropertiesService) {
		this.svrPropertiesService = svrPropertiesService;
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *          the valet to set.
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	public final void setValet(final PageAddOnValet valet) {
		this.valet = valet;
	}

	/**
	 * Builds a multi-line string for the specified property.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *          the abbreviation for the state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param propertyPrefix
	 *          the prefix for the property.
	 * @param defaultLines
	 *          the default set of lines for the property.
	 * @return the multi-line string.
	 * @since Oct 23, 2012
	 * @version May 7, 2013
	 */
	protected final String multilineProperty(final String stateAbbreviation,
	    final String votingRegionName, final String propertyPrefix,
	    final String[] defaultLines) {
		final StringBuilder sb = new StringBuilder();
		boolean sawValues = false;
		for (int idx = 0;; ++idx) {
			final String line = getSvrPropertiesService()
			    .findProperty(stateAbbreviation, votingRegionName,
			        propertyPrefix + "[" + idx + "]");
			if (line == null) {
				break;
			}
			sawValues = true;
			sb.append(line);
		}

		if (!sawValues) {
			for (final String line : defaultLines) {
				sb.append(line);
			}
		}

		return sb.toString();
	}

	/**
	 * Retrieves a single line property.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *          the abbreviation for the state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param propertyName
	 *          the name of the property.
	 * @param defaultLine
	 *          the default value.
	 * @return the single line value.
	 * @since Oct 23, 2012
	 * @version May 7, 2013
	 */
	protected final String singlelineProperty(final String stateAbbreviation,
	    final String votingRegionName, final String propertyName,
	    final String defaultLine) {
		String line = getSvrPropertiesService().findProperty(stateAbbreviation,
		    votingRegionName, propertyName);
		if (line == null) {
			line = defaultLine;
		}

		return line;
	}

}