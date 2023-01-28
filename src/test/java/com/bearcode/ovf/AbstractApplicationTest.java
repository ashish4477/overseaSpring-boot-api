package com.bearcode.ovf;

import com.bearcode.ovf.actions.HomeController;
import com.bearcode.ovf.service.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/webapp/WEB-INF/overseas-servlet.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
abstract public class AbstractApplicationTest extends AbstractJUnit38SpringContextTests {

	@Autowired
	protected FacesService facesService;

	@Autowired
	protected FedexService fedexService;

	@Autowired
	protected LocalOfficialService localOfficialService;

	@Autowired
	protected MailingListService mailingListService;

	@Autowired
	protected OverseasUserService overseasUserService;

	@Autowired
	protected QuestionnaireService questionnaireService;

	@Autowired
	protected StateService stateService;

	@Autowired
	protected HomeController homeController;


}
