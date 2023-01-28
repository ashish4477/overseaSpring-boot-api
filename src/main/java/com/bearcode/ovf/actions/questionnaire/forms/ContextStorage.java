package com.bearcode.ovf.actions.questionnaire.forms;

import com.bearcode.ovf.model.questionnaire.FlowType;

public interface ContextStorage {

	/**
	 * Save WizardContext into the session object and set as active context
	 */
	void save();

	/**
	 * Load WizardContext object from the session for specific <code>flowType</code>
	 * 
	 * @param flowType <code>FlowType</code>
	 * 
	 * @param resetFlowContext <code>boolean</code>
	 * 			if the session user doesn't equal to the wizard context user then
	 * 			delete <code>FlowType</code> context from the session 
	 * 
	 * @return WizardContext for specific <code>flowType</code>
	 * 		   <code>null</code> if wizard context doesn't exist for specific <code>flowType</code> 
	 * 
	 * @throws	IllegalArgumentException
	 * 			if request.session object is null 
	 * 
	 * 
	 */
	WizardContext load(FlowType flowType, boolean resetFlowContext);
	
	/**
	 * Load WizardContext object from the session object
	 * 
	 * @param resetFlowContext <code>boolean</code>
	 * 			if the session user doesn't equal to the wizard context user then
	 * 			delete <code>FlowType</code> context from the session 

	 * @return
	 */
	WizardContext load(boolean resetFlowContext);
	
	/**
	 * @see load(true)
	 */
	WizardContext load();

	/**
	 * Delete active wizard context from the session object
	 */
	void delete();
	
	/**
	 * Delete all wizard contexts from the session object
	 */
	void deleteAll();
	
	/**
	 * Activate <code>flowType</code> 
	 * @param flowType
	 * @return
	 */
	boolean activate(FlowType flowType);
}
