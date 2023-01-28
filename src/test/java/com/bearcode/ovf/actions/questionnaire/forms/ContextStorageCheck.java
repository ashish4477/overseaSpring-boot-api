/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire.forms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;

/**
 * Abstract test for implementations of {@link ContextStorage}.
 * 
 * @author IanBrown
 * 
 * @param <S>
 *            the type of context storage to test.
 * @since Jun 19, 2012
 * @version Jun 19, 2012
 */
public abstract class ContextStorageCheck<S extends ContextStorage> extends EasyMockSupport {

	/**
	 * the context storage to test.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private S contextStorage;

	/**
	 * Sets up the context storage to test.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Before
	public final void setUpContextStorage() {
		setUpForContextStorage();
		setContextStorage(createContextStorage());
	}

	/**
	 * Tears down the context storage after testing.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@After
	public final void tearDownContextStorage() {
		setContextStorage(null);
		tearDownForContextStorage();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#activate(com.bearcode.ovf.model.questionnaire.FlowType)}
	 * for the case where the storage is activated already.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testActivate_alreadyActivated() {
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		setUpForActivate_activated(flowType);
		replayAll();

		final boolean actualActivated = getContextStorage().activate(flowType);

		assertFalse("The storage is not activated", actualActivated);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#activate(com.bearcode.ovf.model.questionnaire.FlowType)}
	 * for the case where the storage is not activated yet.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testActivate_notActivated() {
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		setUpForActivate_notActivated(flowType);
		replayAll();

		final boolean actualActivated = getContextStorage().activate(flowType);

		assertTrue("The storage is activated", actualActivated);
		assertActivated(flowType);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#delete()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testDelete() {
		setUpForDelete();
		replayAll();

		getContextStorage().delete();

		assertDelete();
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#delete()} for the case where the storage
	 * isn't ready.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testDelete_notReady() {
		replayAll();

		getContextStorage().delete();

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#deleteAll()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testDeleteAll() {
		setUpForDelete();
		replayAll();

		getContextStorage().deleteAll();

		assertDelete();
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#load()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testLoad() {
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = createWizardContext(wizardResults, flowType);
		setUpForReset(wizardResults);
		replayAll();

		final WizardContext actualWizardContext = getContextStorage().load();

		assertSame("The wizard context is loaded", wizardContext, actualWizardContext);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#load(boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testLoadBoolean() {
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = createWizardContext(wizardResults, flowType);
		setUpForReset(wizardResults);
		replayAll();

		final WizardContext actualWizardContext = getContextStorage().load(true);

		assertSame("The wizard context is loaded", wizardContext, actualWizardContext);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#load(boolean)} for the case where the
	 * reset clears things.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testLoadBoolean_resetClears() {
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		createWizardContext(wizardResults, flowType);
		setUpForReset_clears(wizardResults);
		replayAll();

		final WizardContext actualWizardContext = getContextStorage().load(true);

		assertNull("The wizard context not loaded", actualWizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#load(com.bearcode.ovf.model.questionnaire.FlowType, boolean)}
	 * for the case where the flow context is not reset.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testLoadFlowTypeBoolean_noReset() {
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = createWizardContext(wizardResults, flowType);
		replayAll();

		final WizardContext actualWizardContext = getContextStorage().load(flowType, false);

		assertSame("The wizard context is loaded", wizardContext, actualWizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#load(com.bearcode.ovf.model.questionnaire.FlowType, boolean)}
	 * for the case where there is no wizard context.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testLoadFlowTypeBoolean_noWizardContext() {
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		replayAll();

		final WizardContext actualWizardContext = getContextStorage().load(flowType, false);

		assertNull("No wizard context is loaded", actualWizardContext);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.forms.ContextStorage#save()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testSave() {
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final WizardContext wizardContext = createWizardContext(wizardResults, flowType);
		addWizardContext(getContextStorage(), wizardContext);
		replayAll();

		getContextStorage().save();

		assertSaved(flowType, wizardContext);
		verifyAll();
	}

	/**
	 * Adds the wizard context to the context storage.
	 * 
	 * @author IanBrown
	 * @param contextStorage
	 *            the context storage.
	 * @param wizardContext
	 *            the wizard context.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void addWizardContext(S contextStorage, WizardContext wizardContext);

	/**
	 * Custom assertion to ensure that the context storage is activated for the flow type.
	 * 
	 * @author IanBrown
	 * @param flowType
	 *            the flow type.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void assertActivated(FlowType flowType);

	/**
	 * Custom assertion to ensure that a call to {@link ContextStorage#delete()} worked.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void assertDelete();

	/**
	 * Custom assertion to ensure that the storage context was saved.
	 * 
	 * @author IanBrown
	 * @param flowType
	 *            the flow type.
	 * @param wizardContext
	 *            the wizard context.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void assertSaved(FlowType flowType, WizardContext wizardContext);

	/**
	 * Creates a context storage object of the type to test.
	 * 
	 * @author IanBrown
	 * @return the context storage object.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract S createContextStorage();

	/**
	 * Creates a wizard context wrapping the results.
	 * 
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @param flowType
	 *            the flow type.
	 * @return the wizard context.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract WizardContext createWizardContext(final WizardResults wizardResults, final FlowType flowType);

	/**
	 * Creates a flow key for the input flow type.
	 * 
	 * @author IanBrown
	 * @param flowType
	 *            the flow type.
	 * @return the flow key.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected final String flowKey(final FlowType flowType) {
		return String.format("%s.%s", WizardContext.class.getName(), flowType.name());
	}

	/**
	 * Gets the context storage.
	 * 
	 * @author IanBrown
	 * @return the context storage.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected final S getContextStorage() {
		return contextStorage;
	}

	/**
	 * Sets up for a call to {@link ContextStorage#activate(FlowType)} for the case where the storage is already activated.
	 * 
	 * @author IanBrown
	 * @param flowType
	 *            the flow type.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void setUpForActivate_activated(FlowType flowType);

	/**
	 * Sets up for a call to {@link ContextStorage#activate(FlowType)} for the case where the storage is not already activated.
	 * 
	 * @author IanBrown
	 * @param flowType
	 *            the new flow type.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void setUpForActivate_notActivated(FlowType flowType);

	/**
	 * Sets up to test the specific type of context storage,.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void setUpForContextStorage();

	/**
	 * Sets up to perform a call to {@link ContextStorage#delete()}.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected final void setUpForDelete() {
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;
		final WizardContext wizardContext = createWizardContext(wizardResults, flowType);
		addWizardContext(getContextStorage(), wizardContext);
	}

	/**
	 * Sets up for a reset.
	 * 
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void setUpForReset(WizardResults wizardResults);

	/**
	 * Sets up for a reset that clears the context.
	 * 
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void setUpForReset_clears(WizardResults wizardResults);

	/**
	 * Tears down the set up for testing the specific type of context storage.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	protected abstract void tearDownForContextStorage();

	/**
	 * Sets the context storage.
	 * 
	 * @author IanBrown
	 * @param contextStorage
	 *            the context storage to set.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private void setContextStorage(final S contextStorage) {
		this.contextStorage = contextStorage;
	}
}
