/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.common.FaceFlowLogo;
import com.bearcode.ovf.model.questionnaire.FlowType;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link FaceConfigDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 18, 2012
 * @version Jul 18, 2012
 */
public final class FaceConfigDAOTest extends BearcodeDAOCheck<FaceConfigDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findAllInstructions()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindAllInstructions() {
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		final List<FaceFlowInstruction> instructions = Arrays.asList(instruction);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FaceFlowInstruction.class)).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(instructions);
		replayAll();

		final Collection<FaceFlowInstruction> actualInstructions = getBearcodeDAO().findAllInstructions();

		assertSame("The instructions are returned", instructions, actualInstructions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindById() {
		final long id = 3567l;
		final FaceConfig config = createMock("Config", FaceConfig.class);
		EasyMock.expect(getHibernateTemplate().get(FaceConfig.class, id)).andReturn(config);
		replayAll();

		final FaceConfig actualConfig = getBearcodeDAO().findById(id);

		assertSame("The config is returned", config, actualConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findConfigByPath(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindConfigByPath() {
		final String path = "Path";
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final List<FaceConfig> configs = Arrays.asList(config);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(configs);
		replayAll();

		final FaceConfig actualConfig = getBearcodeDAO().findConfigByPath(path);

		assertSame("The config is returned", config, actualConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findConfigByPath(java.lang.String)} for the case where there is no
	 * match.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test(expected = NoSuchElementException.class)
	public final void testFindConfigByPath_noMatch() {
		final String path = "Path";
		final List<FaceConfig> configs = new LinkedList<FaceConfig>();
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(configs);
		replayAll();

		getBearcodeDAO().findConfigByPath(path);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findConfigByPrefix(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindConfigByPrefix() {
		final String prefix = "Prefix";
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final List<FaceConfig> configs = Arrays.asList(config);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(configs);
		replayAll();

		final FaceConfig actualConfig = getBearcodeDAO().findConfigByPrefix(prefix);

		assertSame("The config is returned", config, actualConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findConfigByPrefix(java.lang.String)} for the case where there is
	 * no match.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test(expected = NoSuchElementException.class)
	public final void testFindConfigByPrefix_noMatch() {
		final String prefix = "Prefix";
		final List<FaceConfig> configs = new LinkedList<FaceConfig>();
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(configs);
		replayAll();

		getBearcodeDAO().findConfigByPrefix(prefix);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findConfigs()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindConfigs() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final List<FaceConfig> configs = Arrays.asList(config);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(configs);
		replayAll();

		final Collection<FaceConfig> actualConfigs = getBearcodeDAO().findConfigs();

		assertSame("The configs are returned", configs, actualConfigs);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findInstructionById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindInstructionById() {
		final long id = 65426l;
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		EasyMock.expect(getHibernateTemplate().get(FaceFlowInstruction.class, id)).andReturn(instruction);
		replayAll();

		final FaceFlowInstruction actualInstruction = getBearcodeDAO().findInstructionById(id);

		assertSame("The instruction is returned", instruction, actualInstruction);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.FaceConfigDAO#findInstructionOfFlow(com.bearcode.ovf.model.common.FaceConfig, com.bearcode.ovf.model.questionnaire.FlowType)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindInstructionOfFlow() {
		final FaceConfig faceConfig = createMock("Config", FaceConfig.class);
		final FlowType type = FlowType.DOMESTIC_REGISTRATION;
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FaceFlowInstruction.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(instruction);
		replayAll();

		final FaceFlowInstruction actualInstruction = getBearcodeDAO().findInstructionOfFlow(faceConfig, type);

		assertSame("The instruction is returned", instruction, actualInstruction);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findInstructions(com.bearcode.ovf.model.common.FaceConfig)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindInstructions() {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final FaceFlowInstruction instruction = createMock("Instruction", FaceFlowInstruction.class);
		final List<FaceFlowInstruction> instructions = Arrays.asList(instruction);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FaceFlowInstruction.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(instructions);
		replayAll();

		final Collection<FaceFlowInstruction> actualInstructions = getBearcodeDAO().findInstructions(faceConfig);

		assertSame("The instructions are returned", instructions, actualInstructions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#findLogo(com.bearcode.ovf.model.common.FaceConfig)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindLogo() {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final FaceFlowLogo logo = createMock("Logo", FaceFlowLogo.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FaceFlowLogo.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.setMaxResults(1)).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(logo);
		replayAll();

		final FaceFlowLogo actualLogo = getBearcodeDAO().findLogo(faceConfig);

		assertSame("The logo is returned", logo, actualLogo);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#getDefaultConfig()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetDefaultConfig() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final List<FaceConfig> configs = Arrays.asList(config);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(configs);
		replayAll();

		final FaceConfig actualConfig = getBearcodeDAO().getDefaultConfig();

		assertSame("The config is returned", config, actualConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#getDefaultConfig()} for the case where there isn't one in the
	 * database.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test(expected = NoSuchElementException.class)
	public final void testGetDefaultConfig_noMatch() {
		final List<FaceConfig> configs = new LinkedList<FaceConfig>();
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(configs);
		replayAll();

		getBearcodeDAO().getDefaultConfig();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#getDefaultConfig(com.bearcode.ovf.model.common.FaceConfig)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testGetDefaultConfigFaceConfig() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final FaceConfig expectedConfig = createMock("ExpectedConfig", FaceConfig.class);
		final long id = 6522l;
		EasyMock.expect(config.getId()).andReturn(id);
		final List<FaceConfig> configs = Arrays.asList(expectedConfig);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(configs);
		replayAll();

		final FaceConfig actualConfig = getBearcodeDAO().getDefaultConfig(config);

		assertSame("The expected config is returned", expectedConfig, actualConfig);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FaceConfigDAO#getDefaultConfig(com.bearcode.ovf.model.common.FaceConfig)} for the case where there is no match.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test(expected = NoSuchElementException.class)
	public final void testGetDefaultConfigFaceConfig_noMatch() {
		final FaceConfig config = createMock("Config", FaceConfig.class);
		final long id = 6522l;
		EasyMock.expect(config.getId()).andReturn(id);
		final List<FaceConfig> configs = new LinkedList<FaceConfig>();
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(configs);
		replayAll();

		getBearcodeDAO().getDefaultConfig(config);
	}

	/** {@inheritDoc} */
	@Override
	protected final FaceConfigDAO createBearcodeDAO() {
		return new FaceConfigDAO();
	}
}
