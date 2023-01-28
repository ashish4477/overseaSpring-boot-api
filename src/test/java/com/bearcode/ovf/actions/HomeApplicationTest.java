
package com.bearcode.ovf.actions;

import com.bearcode.ovf.AbstractApplicationTest;
import com.bearcode.ovf.model.common.State;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

@Ignore
public class HomeApplicationTest extends AbstractApplicationTest {

	//@Test
	//public void testBogus(){
		// otherwise if all tests are disabled there is an exception
	//}

	public void _testGetUsers() throws Exception {

        ModelMap model = new ModelMap();
		//ModelAndView mav =
		homeController.showPage( (HttpServletRequest) null, model );

		//Map m = mav.getModel();
		Object states = model.get("states");

		assertNotNull(states);
		assertTrue(states instanceof Collection);
		Collection stateCol = (Collection<State>) states;
		assertEquals(54, stateCol.size());		
		//assertEquals(mav.getViewName(), "userList");
		
	}

}
