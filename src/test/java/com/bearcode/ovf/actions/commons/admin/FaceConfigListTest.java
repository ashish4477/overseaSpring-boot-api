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
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;

/**
 * Extended {@link BaseControllerCheck} test for {@link FaceConfigList}.
 * 
 * @author IanBrown
 * 
 * @since Dec 22, 2011
 * @version Jan 3, 2012
 */
public final class FaceConfigListTest extends BaseControllerCheck<FaceConfigList> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceConfigList#buildReferences(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testBuildReferences() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		replayAll();

		final String actualModelAndView = getBaseController().buildReferences(request, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceConfigList#getFaces()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetFaces() {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final Collection<FaceConfig> faces = Arrays.asList(faceConfig);
		EasyMock.expect(getFacesService().findAllConfigs()).andReturn(faces);
		replayAll();

		final Collection<FaceConfig> actualFaces = getBaseController().getFaces();

		assertSame("The faces are returned", faces, actualFaces);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceConfigList#getInstructions()}
	 * for the case where there are instructions with different faces.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetInstructions_instructionsForDifferentFaces() {
		final FaceFlowInstruction instruction1 = createMock("Instruction1", FaceFlowInstruction.class);
		final FaceFlowInstruction instruction2 = createMock("Instruction2", FaceFlowInstruction.class);
		final Collection<FaceFlowInstruction> instructions = Arrays.asList(instruction1, instruction2);
		EasyMock.expect(getFacesService().findInstructions()).andReturn(instructions);
		final FaceConfig faceConfig1 = createMock("FaceConfig1", FaceConfig.class);
		EasyMock.expect(instruction1.getFaceConfig()).andReturn(faceConfig1).atLeastOnce();
		final long configId1 = 389269l;
		EasyMock.expect(faceConfig1.getId()).andReturn(configId1).atLeastOnce();
		final FaceConfig faceConfig2 = createMock("FaceConfig2", FaceConfig.class);
		EasyMock.expect(instruction2.getFaceConfig()).andReturn(faceConfig2).atLeastOnce();
		final long configId2 = 9545612l;
		EasyMock.expect(faceConfig2.getId()).andReturn(configId2).atLeastOnce();
		replayAll();

		final Map<Long, Collection<FaceFlowInstruction>> actualInstructions = getBaseController().getInstructions();

		assertEquals("There are instructions", 2, actualInstructions.size());
		assertTrue("There is an entry for the config ID 1", actualInstructions.containsKey(configId1));
		final Collection<FaceFlowInstruction> expectedFaceFlowInstructions1 = Arrays.asList(instruction1);
		final Collection<FaceFlowInstruction> actualFaceFlowInstructions1 = actualInstructions.get(configId1);
		assertEquals("The first instruction is in the list for the config ID 1", expectedFaceFlowInstructions1,
				actualFaceFlowInstructions1);
		assertTrue("There is an entry for the config ID 2", actualInstructions.containsKey(configId2));
		final Collection<FaceFlowInstruction> expectedFaceFlowInstructions2 = Arrays.asList(instruction2);
		final Collection<FaceFlowInstruction> actualFaceFlowInstructions2 = actualInstructions.get(configId2);
		assertEquals("The second instruction is in the list for the config ID 2", expectedFaceFlowInstructions2,
				actualFaceFlowInstructions2);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceConfigList#getInstructions()}
	 * for the case where there are multiple instructions for the same face.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetInstructions_multipleInstructionsForFace() {
		final FaceFlowInstruction instruction1 = createMock("Instruction1", FaceFlowInstruction.class);
		final FaceFlowInstruction instruction2 = createMock("Instruction2", FaceFlowInstruction.class);
		final Collection<FaceFlowInstruction> instructions = Arrays.asList(instruction1, instruction2);
		EasyMock.expect(getFacesService().findInstructions()).andReturn(instructions);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(instruction1.getFaceConfig()).andReturn(faceConfig).atLeastOnce();
		EasyMock.expect(instruction2.getFaceConfig()).andReturn(faceConfig).atLeastOnce();
		final long configId = 71270l;
		EasyMock.expect(faceConfig.getId()).andReturn(configId).atLeastOnce();
		replayAll();

		final Map<Long, Collection<FaceFlowInstruction>> actualInstructions = getBaseController().getInstructions();

		assertEquals("There are instructions", 1, actualInstructions.size());
		assertTrue("There is an entry for the config ID", actualInstructions.containsKey(configId));
		final Collection<FaceFlowInstruction> expectedFaceFlowInstructions = Arrays.asList(instruction1, instruction2);
		final Collection<FaceFlowInstruction> actualFaceFlowInstructions = actualInstructions.get(configId);
		assertEquals("Both of the instructions are in the list for the config ID", expectedFaceFlowInstructions,
				actualFaceFlowInstructions);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceConfigList#getInstructions()}
	 * for the case where there are no instructions.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetInstructions_noInstructions() {
		final Collection<FaceFlowInstruction> instructions = new ArrayList<FaceFlowInstruction>();
		EasyMock.expect(getFacesService().findInstructions()).andReturn(instructions);
		replayAll();

		final Map<Long, Collection<FaceFlowInstruction>> actualInstructions = getBaseController().getInstructions();

		assertTrue("There are no instructions", actualInstructions.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.FaceConfigList#getInstructions()}
	 * for the case where there is one instruction.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetInstructions_oneInstruction() {
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		final Collection<FaceFlowInstruction> instructions = Arrays.asList(instruction);
		EasyMock.expect(getFacesService().findInstructions()).andReturn(instructions);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(instruction.getFaceConfig()).andReturn(faceConfig).atLeastOnce();
		final long configId = 86210l;
		EasyMock.expect(faceConfig.getId()).andReturn(configId).atLeastOnce();
		replayAll();

		final Map<Long, Collection<FaceFlowInstruction>> actualInstructions = getBaseController().getInstructions();

		assertEquals("There are instructions", 1, actualInstructions.size());
		assertTrue("There is an entry for the config ID", actualInstructions.containsKey(configId));
		final Collection<FaceFlowInstruction> expectedFaceFlowInstructions = Arrays.asList(instruction);
		final Collection<FaceFlowInstruction> actualFaceFlowInstructions = actualInstructions.get(configId);
		assertEquals("The instruction is in the list for the config ID", expectedFaceFlowInstructions, actualFaceFlowInstructions);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final FaceConfigList createBaseController() {
		final FaceConfigList faceConfigList = new FaceConfigList();
		return faceConfigList;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/admin/FacesList.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "Faces Config List";
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
