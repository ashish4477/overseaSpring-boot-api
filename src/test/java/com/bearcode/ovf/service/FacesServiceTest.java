/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.ovf.DAO.FaceConfigDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.common.FaceFlowLogo;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.tools.FaceMigrationDealer;

/**
 * Test for {@link com.bearcode.ovf.service.FacesService}.
 * 
 * @author IanBrown
 * 
 * @since Jul 17, 2012
 * @version Jul 17, 2012
 */
public final class FacesServiceTest extends EasyMockSupport {

	/**
	 * the faces service to test.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FacesService facesService;

	/**
	 * the face config DAO.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FaceConfigDAO faceConfigDAO;

	/**
	 * Sets up to test the faces service.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Before
	public final void setUpFacesService() {
		setFaceConfigDAO(createMock("FaceConfigDAO", FaceConfigDAO.class));
		setFacesService(createFacesService());
		ReflectionTestUtils.setField(getFacesService(), "faceConfigDAO", getFaceConfigDAO());
	}

	/**
	 * Tears down after testing the faces service.
	 * 
	 * @author IanBrown
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@After
	public final void tearDownFacesService() {
		setFacesService(null);
		setFaceConfigDAO(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findAllConfigs()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindAllConfigs() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final Collection<FaceConfig> configs = Arrays.asList(config);
		EasyMock.expect(getFaceConfigDAO().findConfigs()).andReturn(configs);
		replayAll();

		final Collection<FaceConfig> actualConfigs = getFacesService().findAllConfigs();

		assertSame("The configs are returned", configs, actualConfigs);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findConfig(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindConfig() {
		final String path = "path";
		final FaceConfig config = createMock("Config", FaceConfig.class);
		EasyMock.expect(getFaceConfigDAO().findConfigByPath(path)).andReturn(config);
		replayAll();

		final FaceConfig actualConfig = getFacesService().findConfig(path);

		assertSame("The config is returned", config, actualConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findConfigById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindConfigById() {
		final long id = 28972l;
		final FaceConfig config = createMock("Config", FaceConfig.class);
		EasyMock.expect(getFaceConfigDAO().findById(id)).andReturn(config);
		replayAll();

		final FaceConfig actualConfig = getFacesService().findConfigById(id);

		assertSame("The config is returned", config, actualConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findConfigByPrefix(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindConfigByPrefix() {
		final String prefix = "prefix";
		final FaceConfig config = createMock("Config", FaceConfig.class);
		EasyMock.expect(getFaceConfigDAO().findConfigByPrefix(prefix)).andReturn(config);
		replayAll();

		final FaceConfig actualConfig = getFacesService().findConfigByPrefix(prefix);

		assertSame("The config is returned", config, actualConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findDefaultConfig()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindDefaultConfig() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		EasyMock.expect(getFaceConfigDAO().getDefaultConfig()).andReturn(config);
		replayAll();

		final FaceConfig actualConfig = getFacesService().findDefaultConfig();

		assertSame("The config is returned", config, actualConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findInstructionById(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindInstructionById() {
		final long id = 7856765764l;
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		EasyMock.expect(getFaceConfigDAO().findInstructionById(id)).andReturn(instruction);
		replayAll();

		final FaceFlowInstruction actualInstruction = getFacesService().findInstructionById(id);

		assertSame("The instruction is returned", instruction, actualInstruction);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FacesService#findInstructionOfFlow(com.bearcode.ovf.model.common.FaceConfig, com.bearcode.ovf.model.questionnaire.FlowType)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindInstructionOfFlow() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		EasyMock.expect(getFaceConfigDAO().findInstructionOfFlow(config, flowType)).andReturn(instruction);
		replayAll();

		final FaceFlowInstruction actualInstruction = getFacesService().findInstructionOfFlow(config, flowType);

		assertSame("The instruction is returned", instruction, actualInstruction);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FacesService#findInstructionOfFlow(com.bearcode.ovf.model.common.FaceConfig, com.bearcode.ovf.model.questionnaire.FlowType)}
	 * for the case where there is none for the specific config/flow type combination.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindInstructionOfFlow_noneForConfigFlowType() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		EasyMock.expect(getFaceConfigDAO().findInstructionOfFlow(config, flowType)).andReturn(null);
		final FaceConfig defaultConfig = createMock("DefaultConfig", FaceConfig.class);
		EasyMock.expect(getFaceConfigDAO().getDefaultConfig()).andReturn(defaultConfig);
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		EasyMock.expect(getFaceConfigDAO().findInstructionOfFlow(defaultConfig, flowType)).andReturn(instruction);
		replayAll();

		final FaceFlowInstruction actualInstruction = getFacesService().findInstructionOfFlow(config, flowType);

		assertSame("The instruction is returned", instruction, actualInstruction);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findInstructions()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindInstructions() {
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		final Collection<FaceFlowInstruction> instructions = Arrays.asList(instruction);
		EasyMock.expect(getFaceConfigDAO().findAllInstructions()).andReturn(instructions);
		replayAll();

		final Collection<FaceFlowInstruction> actualInstructions = getFacesService().findInstructions();

		assertSame("The instructions are returned", instructions, actualInstructions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findInstructions(com.bearcode.ovf.model.common.FaceConfig)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindInstructionsFaceConfig() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		final Collection<FaceFlowInstruction> instructions = Arrays.asList(instruction);
		EasyMock.expect(getFaceConfigDAO().findInstructions(config)).andReturn(instructions);
		replayAll();

		final Collection<FaceFlowInstruction> actualInstructions = getFacesService().findInstructions(config);

		assertSame("The instructions are returned", instructions, actualInstructions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findLogo(com.bearcode.ovf.model.common.FaceConfig)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindLogo() {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final FaceFlowLogo logo = createMock("Logo", FaceFlowLogo.class);
		EasyMock.expect(getFaceConfigDAO().findLogo(faceConfig)).andReturn(logo);
		replayAll();

		final FaceFlowLogo actualLogo = getFacesService().findLogo(faceConfig);

		assertSame("The logo is returned", logo, actualLogo);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findLogoOfFace(com.bearcode.ovf.model.common.FaceConfig)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindLogoOfFace() {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final FaceFlowLogo logo = createMock("Logo", FaceFlowLogo.class);
		EasyMock.expect(getFaceConfigDAO().findLogo(faceConfig)).andReturn(logo);
		replayAll();

		final FaceFlowLogo actualLogo = getFacesService().findLogoOfFace(faceConfig);

		assertSame("The logo is returned", logo, actualLogo);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#findLogoOfFace(com.bearcode.ovf.model.common.FaceConfig)} for
	 * the case where there is none for the specific face.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindLogoOfFace_noneForFace() {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFaceConfigDAO().findLogo(faceConfig)).andReturn(null);
		final FaceConfig defaultConfig = createMock("DefaultConfig", FaceConfig.class);
		EasyMock.expect(getFaceConfigDAO().getDefaultConfig()).andReturn(defaultConfig);
		final FaceFlowLogo logo = createMock("Logo", FaceFlowLogo.class);
		EasyMock.expect(getFaceConfigDAO().findLogo(defaultConfig)).andReturn(logo);
		replayAll();

		final FaceFlowLogo actualLogo = getFacesService().findLogoOfFace(faceConfig);

		assertSame("The logo is returned", logo, actualLogo);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#getApprovedFileName(java.lang.String, java.lang.String)} for the
	 * case where it is not approved.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testGetApprovedFileName_notApproved() {
		final String rawFileName = "rawFileName.tst";
		final String relativePath = "relativePath";
		final FaceConfig defaultConfig = createMock("DefaultConfig", FaceConfig.class);
		EasyMock.expect(getFaceConfigDAO().getDefaultConfig()).andReturn(defaultConfig);
		EasyMock.expect(defaultConfig.getRelativePrefix()).andReturn("defaultRelativePrefix");
		replayAll();

		final String actualApprovedFilename = getFacesService().getApprovedFileName(rawFileName, relativePath);

		assertEquals("There is no approved filename", "", actualApprovedFilename);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#invalidateCache()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testInvalidateCache() {
		replayAll();

		getFacesService().invalidateCache();

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#saveConfig(com.bearcode.ovf.model.common.FaceConfig)} for the
	 * case where the config is not the default path.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testSaveConfig_notDefaultPath() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		EasyMock.expect(config.isDefaultPath()).andReturn(false);
		getFaceConfigDAO().makePersistent(config);
		replayAll();

		getFacesService().saveConfig(config);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#saveConfig(com.bearcode.ovf.model.common.FaceConfig)} for the
	 * case where the config is now the default path.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testSaveConfig_nowDefaultPath() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		EasyMock.expect(config.isDefaultPath()).andReturn(true);
		final FaceConfig defaultConfig = createMock("DefaultConfig", FaceConfig.class);
		EasyMock.expect(getFaceConfigDAO().getDefaultConfig()).andReturn(defaultConfig);
		final long id = 87128l;
		EasyMock.expect(config.getId()).andReturn(id);
		final long defaultId = 123124l;
		EasyMock.expect(defaultConfig.getId()).andReturn(defaultId);
		defaultConfig.setDefaultPath(false);
		getFaceConfigDAO().makePersistent(defaultConfig);
		getFaceConfigDAO().makePersistent(config);
		replayAll();

		getFacesService().saveConfig(config);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#saveConfig(com.bearcode.ovf.model.common.FaceConfig)} for the
	 * case where the config is still the default path.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testSaveConfig_stillDefaultPath() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		EasyMock.expect(config.isDefaultPath()).andReturn(true);
		EasyMock.expect(getFaceConfigDAO().getDefaultConfig()).andReturn(config);
		final long id = 87128l;
		EasyMock.expect(config.getId()).andReturn(id).anyTimes();
		getFaceConfigDAO().makePersistent(config);
		replayAll();

		getFacesService().saveConfig(config);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#saveFaceLogo(com.bearcode.ovf.model.common.FaceFlowLogo)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testSaveFaceLogo() {
		final FaceFlowLogo logo = createMock("Logo", FaceFlowLogo.class);
		getFaceConfigDAO().makePersistent(logo);
		replayAll();

		getFacesService().saveFaceLogo(logo);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.FacesService#saveInstruction(com.bearcode.ovf.model.common.FaceFlowInstruction, com.bearcode.ovf.model.common.OverseasUser)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testSaveInstruction() {
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		final OverseasUser user = createMock("user", OverseasUser.class);
		instruction.setUpdatedBy(user);
		instruction.setUpdatedTime((Date) EasyMock.anyObject());
		getFaceConfigDAO().makePersistent(instruction);
		replayAll();

		getFacesService().saveInstruction(instruction, user);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.FacesService#saveMigration(com.bearcode.ovf.tools.FaceMigrationDealer)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testSaveMigration() {
		final FaceMigrationDealer faceMigrationDealer = createMock("FaceMigrationDealer", FaceMigrationDealer.class);
		final FaceConfig faceToPersist = createMock("FaceToPersist", FaceConfig.class);
		final Collection<FaceConfig> facesToPersist = Arrays.asList(faceToPersist);
		EasyMock.expect(faceMigrationDealer.getFacesToPersist()).andReturn(facesToPersist);
		getFaceConfigDAO().makeAllPersistent(facesToPersist);
		final FaceFlowInstruction instructionToPersist = createMock("InstructionToPersist", FaceFlowInstruction.class);
		final Collection<FaceFlowInstruction> instructionsToPersist = Arrays.asList(instructionToPersist);
		EasyMock.expect(faceMigrationDealer.getInstructionsToPersist()).andReturn(instructionsToPersist);
		getFaceConfigDAO().makeAllPersistent(instructionsToPersist);
		final FaceFlowLogo logoToPersist = createMock("LogoToPersist", FaceFlowLogo.class);
		final Collection<FaceFlowLogo> logosToPersist = Arrays.asList(logoToPersist);
		EasyMock.expect(faceMigrationDealer.getLogosToPersist()).andReturn(logosToPersist);
		getFaceConfigDAO().makeAllPersistent(logosToPersist);
		replayAll();

		getFacesService().saveMigration(faceMigrationDealer);

		verifyAll();
	}

	/**
	 * Creates a faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FacesService createFacesService() {
		return new FacesService();
	}

	/**
	 * Gets the face config DAO.
	 * 
	 * @author IanBrown
	 * @return the face config DAO.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FaceConfigDAO getFaceConfigDAO() {
		return faceConfigDAO;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Sets the face config DAO.
	 * 
	 * @author IanBrown
	 * @param faceConfigDAO
	 *            the face config DAO to set.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private void setFaceConfigDAO(final FaceConfigDAO faceConfigDAO) {
		this.faceConfigDAO = faceConfigDAO;
	}

	/**
	 * Sets the faces service.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service to set.
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	private void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}
}
