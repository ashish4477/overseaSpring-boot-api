/**
 * 
 */
package com.bearcode.ovf.actions.commons.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.forms.AdminInstructionsForm;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;

/**
 * Extended {@link BaseControllerCheck} test for {@link EditFaceInstruction}.
 * 
 * @author IanBrown
 * 
 * @since Dec 22, 2011
 * @version Sep 24, 2013
 */
public final class EditFaceInstructionTest extends BaseControllerCheck<EditFaceInstruction> {

	/**
	 * the validator.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private Validator validator;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceInstruction#getInstruction(java.lang.Long, java.lang.Long)}
	 * for the case where the config ID, but not the ID is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetInstruction_configID() {
		final long configId = 782l;
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfigById(configId)).andReturn(faceConfig);
		replayAll();

		final AdminInstructionsForm actualAdminInstructionsForm = getBaseController().getInstruction(null, configId);

		assertNotNull("An admin instructions form is returned", actualAdminInstructionsForm);
		final FaceFlowInstruction instruction = actualAdminInstructionsForm.getInstruction();
		assertNotNull("The instruction is set in the form", instruction);
		assertEquals("The face configuration is set in the instruction", faceConfig, instruction.getFaceConfig());
		assertEquals("The flow type name is not set in the form", "", actualAdminInstructionsForm.getFlowTypeName());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceInstruction#getInstruction(java.lang.Long, java.lang.Long)}
	 * for the case where the ID is provided. Note: there must be an instruction
	 * for the ID or the same <code>NullPointerException</code> thrown in
	 * {@link #testGetInstruction_noIDs()} will be thrown here, too.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetInstruction_id() {
		final long id = 782l;
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		EasyMock.expect(getFacesService().findInstructionById(id)).andReturn(instruction);
		final String flowTypeName = "Flow Type Name";
		EasyMock.expect(instruction.getFlowTypeName()).andReturn(flowTypeName);
		replayAll();

		final AdminInstructionsForm actualAdminInstructionsForm = getBaseController().getInstruction(id, null);

		assertNotNull("An admin instructions form is returned", actualAdminInstructionsForm);
		assertEquals("The instruction is set in the form", instruction, actualAdminInstructionsForm.getInstruction());
		assertEquals("The flow type name is set in the form", flowTypeName, actualAdminInstructionsForm.getFlowTypeName());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceInstruction#getInstruction(java.lang.Long, java.lang.Long)}
	 * for the case where neither the ID nor the config ID are provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Sep 24, 2013
	 */
	@Test
	public final void testGetInstruction_noIDs() {
		replayAll();

		final AdminInstructionsForm actualAdminInstructionsForm = getBaseController().getInstruction(null, null);

		assertNotNull("An admin instructions form is returned", actualAdminInstructionsForm);
		final FaceFlowInstruction instruction = actualAdminInstructionsForm.getInstruction();
		assertNotNull("The instruction is set in the form", instruction);
		assertNull("The face configuration is not set for the instruction", instruction.getFaceConfig());
		assertEquals("The flow type name is not set in the form", "", actualAdminInstructionsForm.getFlowTypeName());
		verifyAll();

	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceInstruction#initBinder(org.springframework.validation.DataBinder)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testInitBinder() {
		final DataBinder binder = createMock("Binder", DataBinder.class);
		binder.setValidator(getValidator());
		replayAll();

		getBaseController().initBinder(binder);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceInstruction#showFaceFlowInstruction(com.bearcode.ovf.forms.AdminInstructionsForm, javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there are instructions for the face configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testShowFaceFlowInstruction_instructionsForFaceConfiguration() {
		final AdminInstructionsForm instruction = createMock("Instruction", AdminInstructionsForm.class);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		instruction.textToShow();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(instruction.getFaceConfig()).andReturn(faceConfig);
		final FaceFlowInstruction faceFlowInstruction = createMock("FaceFlowInstruction", FaceFlowInstruction.class);
		final Collection<FaceFlowInstruction> instructions = Arrays.asList(faceFlowInstruction);
		EasyMock.expect(getFacesService().findInstructions(faceConfig)).andReturn(instructions);
		final List<String> usedTypes = new LinkedList<String>();
		EasyMock.expect(faceFlowInstruction.getFlowTypeName()).andReturn(FlowType.FWAB.name());
		usedTypes.add(FlowType.FWAB.name());
		addUnusedTypesToModel(model, usedTypes);
		replayAll();

		final String actualModelAndView = getBaseController().showFaceFlowInstruction(instruction, request, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceInstruction#showFaceFlowInstruction(com.bearcode.ovf.forms.AdminInstructionsForm, javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for the case where there are no instructions for the face configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Jun 11, 2012
	 */
	@Test
	public final void testShowFaceFlowInstruction_noInstructionsForFaceConfiguration() {
		final AdminInstructionsForm instruction = createMock("Instruction", AdminInstructionsForm.class);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		instruction.textToShow();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(instruction.getFaceConfig()).andReturn(faceConfig);
		final Collection<FaceFlowInstruction> instructions = new ArrayList<FaceFlowInstruction>();
		EasyMock.expect(getFacesService().findInstructions(faceConfig)).andReturn(instructions);
		final List<String> usedTypes = new LinkedList<String>();
		addUnusedTypesToModel(model, usedTypes);
		replayAll();

		final String actualModelAndView = getBaseController().showFaceFlowInstruction(instruction, request, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceInstruction#updateInstruction(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.forms.AdminInstructionsForm, org.springframework.validation.BindingResult, org.springframework.ui.ModelMap)}
	 * for the case where there were errors.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testUpdateInstruction_errors() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final AdminInstructionsForm instruction = createMock("Instruction", AdminInstructionsForm.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(true);
		final ModelMap model = createModelMap(null, request, null, true, false);
		replayAll();

		final String actualModelAndView = getBaseController().updateInstruction(request, instruction, errors, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceInstruction#updateInstruction(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.forms.AdminInstructionsForm, org.springframework.validation.BindingResult, org.springframework.ui.ModelMap)}
	 * for the case where there were no errors.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testUpdateInstruction_noErrors() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final AdminInstructionsForm instruction = createMock("Instruction", AdminInstructionsForm.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(false);
		final ModelMap model = createModelMap(user, request, null, true, false);
		instruction.textToSave();
		final FaceFlowInstruction faceFlowInstruction = createMock("FaceFlowInstruction", FaceFlowInstruction.class);
		EasyMock.expect(instruction.getInstruction()).andReturn(faceFlowInstruction);
		getFacesService().saveInstruction(faceFlowInstruction, user);
		replayAll();

		final String actualModelAndView = getBaseController().updateInstruction(request, instruction, errors, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final EditFaceInstruction createBaseController() {
		final EditFaceInstruction editFaceInstruction = new EditFaceInstruction();
		ReflectionTestUtils.setField(editFaceInstruction, "validator", getValidator());
		return editFaceInstruction;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/admin/FaceInstructionEdit.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setValidator(createMock("Validator", Validator.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setValidator(null);
	}

	/**
	 * Adds the unused flow types to the model.
	 * 
	 * @author IanBrown
	 * @param model
	 *            the model.
	 * @param usedTypes
	 *            the used types.
	 * @since Dec 22, 2011
	 * @version Jun 11, 2012
	 */
	private void addUnusedTypesToModel(final ModelMap model, final List<String> usedTypes) {
		// final List<String> types = new LinkedList<String>();
		// for (final FlowType type : FlowType.values()) {
		// if (!usedTypes.contains(type.toString())) {
		// types.add(type.toString());
		// }
		// }
		// addAttributeToModelMap(model, "types", types);
		// Have to use the easy mock comparators as the array of values isn't equal otherwise.
		//addAttributeToModelMap(model, EasyMock.eq("types"), EasyMock.aryEq(FlowType.values()));
	}

	/**
	 * Gets the validator.
	 * 
	 * @author IanBrown
	 * @return the validator.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private Validator getValidator() {
		return validator;
	}

	/**
	 * Sets the validator.
	 * 
	 * @author IanBrown
	 * @param validator
	 *            the validator to set.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void setValidator(final Validator validator) {
		this.validator = validator;
	}

}
