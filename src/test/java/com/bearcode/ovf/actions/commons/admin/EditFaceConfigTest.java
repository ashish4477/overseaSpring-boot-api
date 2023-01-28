/**
 * 
 */
package com.bearcode.ovf.actions.commons.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.common.FaceFlowLogo;
import com.bearcode.ovf.model.questionnaire.FlowType;

/**
 * Extended {@link BaseControllerCheck} test for {@link EditFaceConfig}.
 * 
 * @author IanBrown
 * 
 * @since Dec 22, 2011
 * @version Jan 3, 2012
 */
public final class EditFaceConfigTest extends BaseControllerCheck<EditFaceConfig> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceConfig#formBackingObject(java.lang.Long)}
	 * for the case where there is an existing face configuration for the ID is
	 * provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem getting the form backing object for the
	 *             ID.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testFormBackingObject_existingId() throws Exception {
		final long id = 975l;
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfigById(id)).andReturn(faceConfig);
		replayAll();

		final FaceConfig actualFormBackingObject = getBaseController().formBackingObject(id);

		assertSame("The face configuration provided to the faces service is returned", faceConfig, actualFormBackingObject);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceConfig#formBackingObject(java.lang.Long)}
	 * for the case where no ID is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem getting the form backing object for the
	 *             ID.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testFormBackingObject_noId() throws Exception {
		replayAll();

		final FaceConfig actualFormBackingObject = getBaseController().formBackingObject(null);

		assertTrue("A face configuration is returned", FaceConfig.class.isInstance(actualFormBackingObject));
		final FaceConfig actualFaceConfig = FaceConfig.class.cast(actualFormBackingObject);
		assertEquals("The face configuration does not have an identifier", null, actualFaceConfig.getId());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceConfig#formBackingObject(java.lang.Long)}
	 * for the case where there is no existing face configuration for the ID is
	 * provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem getting the form backing object for the
	 *             ID.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testFormBackingObject_noSuchId() throws Exception {
		final long id = 753l;
		EasyMock.expect(getFacesService().findConfigById(id)).andReturn(null);
		replayAll();

		final FaceConfig actualFormBackingObject = getBaseController().formBackingObject(id);

		assertTrue("A face configuration is returned", FaceConfig.class.isInstance(actualFormBackingObject));
		final FaceConfig actualFaceConfig = FaceConfig.class.cast(actualFormBackingObject);
		assertEquals("The face configuration does not have an identifier", null, actualFaceConfig.getId());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceConfig#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.common.FaceConfig)}
	 * for the case where there is an existing face configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting the request.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testOnSubmit_existingConfiguration() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createMock("ModelMap", ModelMap.class);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceConfig.getId()).andReturn(6739l);
		getFacesService().saveConfig(faceConfig);
		replayAll();

		final String actualModelAndView = getBaseController().onSubmit(request, model, faceConfig);

		assertEquals("A redirection is returned", "redirect:/admin/FacesConfigsList.htm", actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceConfig#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.common.FaceConfig)}
	 * for the case where there is a new face configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting the request.
	 * @since Dec 22, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testOnSubmit_newConfiguration() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceConfig.getId()).andReturn(0l).atLeastOnce();
		getFacesService().saveConfig(faceConfig);
		replayAll();

		final String actualModelAndView = getBaseController().onSubmit(request, model, faceConfig);

		assertEquals("A redirection is returned", "redirect:/admin/FacesConfigsList.htm", actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceConfig#showFaceConfig(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.common.FaceConfig)}
	 * for the case where the configuration already exists.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem showing the face configuration.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testShowFaceConfig_existingConfiguration() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceConfig.getId()).andReturn(1l);
		final Collection<FaceFlowInstruction> instructions = new ArrayList<FaceFlowInstruction>();
		EasyMock.expect(getFacesService().findInstructions(faceConfig)).andReturn(instructions);
		addAttributeToModelMap(model, "faceInstructions", instructions);
		final FaceFlowLogo logo = createMock("Logo", FaceFlowLogo.class);
		EasyMock.expect(getFacesService().findLogo(faceConfig)).andReturn(logo);
		addAttributeToModelMap(model, "logo", logo);
		addAttributeToModelMap(model, "addingAvailable", FlowType.values().length > instructions.size());
		replayAll();

		final String actualModelAndView = getBaseController().showFaceConfig(request, model, faceConfig);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.EditFaceConfig#showFaceConfig(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.common.FaceConfig)}
	 * for the case where the configuration is new.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem showing the face configuration.
	 * @since Dec 22, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testShowFaceConfig_newConfiguration() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceConfig.getId()).andReturn(0l);
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		final Collection<FaceFlowInstruction> instructions = Arrays.asList(instruction);
		EasyMock.expect(getFacesService().findInstructions(faceConfig)).andReturn(instructions);
		addAttributeToModelMap(model, "faceInstructions", instructions);
		final FaceFlowLogo logo = createMock("Logo", FaceFlowLogo.class);
		EasyMock.expect(getFacesService().findLogo(faceConfig)).andReturn(logo);
		addAttributeToModelMap(model, "logo", logo);
		addAttributeToModelMap(model, "addingAvailable", true);
		replayAll();

		final String actualModelAndView = getBaseController().showFaceConfig(request, model, faceConfig);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final EditFaceConfig createBaseController() {
		final EditFaceConfig editFaceConfig = new EditFaceConfig();
		return editFaceConfig;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/admin/FacesConfigEdit.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "Edit Face Config";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return "/css/admin.css";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return "admin";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
	}

}
