package com.bearcode.ovf.actions.questionnaire.forms;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionContextStorage implements ContextStorage {
	
	private static final String ACTIVE_FLOW_KEY = String.format("%s.currentFlow", WizardContext.class.getName()); 
	
	private final HttpServletRequest request;
	private final WizardContext wizardContext;
	
	private SessionContextStorage(HttpServletRequest request, WizardContext wizardContext) {
		if (request == null) {
			throw new IllegalArgumentException("request");
		}

		this.request = request;
		this.wizardContext = wizardContext;
	}

	public static SessionContextStorage create(HttpServletRequest request, WizardContext wizardContext) {
		return new SessionContextStorage(request, wizardContext);
	}

	public static SessionContextStorage create(HttpServletRequest request) {
		return new SessionContextStorage(request, null);
	}

	@Override
	public void save() {
		if (wizardContext == null) {
			throw new IllegalArgumentException("wizardContext");
		}
		
		final HttpSession session = request.getSession();
		if (session == null) {
			throw new IllegalArgumentException("session");
		}

		FlowType flowType = wizardContext.getFlowType();
		session.setAttribute(ACTIVE_FLOW_KEY, flowType);
		
		session.setAttribute(flowKey(flowType), wizardContext);
	}

	@Override
	public WizardContext load(boolean checkUser) {
		final HttpSession session = request.getSession();
		if (session == null) {
			throw new IllegalArgumentException("session");
		}
		
		try {
			FlowType activeFlow = (FlowType) request.getSession().getAttribute(ACTIVE_FLOW_KEY);
			String reqFlow = MapUtils.getString(request.getParameterMap(), "flow", "");
			if (reqFlow.length() > 0)
				activeFlow = FlowType.valueOf(reqFlow);

			return load(activeFlow, checkUser);
		} catch(Exception e) {
			return null;
		}
	}
	
	@Override
	public WizardContext load() {
		return this.load(true);
	}
	
	@Override
	public WizardContext load(FlowType flowType, boolean checkUser) {
		final HttpSession session = request.getSession();
		if (session == null) throw new IllegalArgumentException("session");

		Object obj = session.getAttribute(flowKey(flowType));
		if (!(obj instanceof WizardContext))
			return null;
		
		WizardContext ctx = (WizardContext) obj;
		if (!checkUser)
			return ctx;
			
		// check user in the session and wizard
		// in case if we have 2 different users in session and in wizard results then 
		// delete current wizard context 
		OverseasUser currentUser = null;
		Object securityObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (securityObj instanceof OverseasUser) {
			currentUser = (OverseasUser) securityObj;
		}
		
		OverseasUser wizardUser = ctx.getWizardResults().getUser();
		if (wizardUser != currentUser) {
			this.delete(ctx.getFlowType());
			return null;
		}
		
		return ctx;
	}

	@Override
	public void delete() {
		if (wizardContext == null) throw new IllegalArgumentException("wizardContext");
		delete(wizardContext.getFlowType());
	}
	
	private void delete(FlowType flowType) {
		final HttpSession session = request.getSession();
		if (session == null) throw new IllegalArgumentException("session");

		session.removeAttribute(flowKey(flowType));		
	}

	@Override
	public void deleteAll() {
		HttpSession session = request.getSession();
		if (session == null) throw new IllegalArgumentException("session");

		for (FlowType flowType : FlowType.values())
			session.removeAttribute(flowKey(flowType));
		session.removeAttribute(ACTIVE_FLOW_KEY);
	}

	@Override
	public boolean activate(FlowType flowType) {
		if (flowType == null) throw new IllegalArgumentException("flowType");
		
		HttpSession session = request.getSession();
		if (session == null) throw new IllegalArgumentException("session");

		FlowType activeFlow = (FlowType) session.getAttribute(ACTIVE_FLOW_KEY);
		if (flowType == activeFlow)
			return false; // flowType already activated
		
		session.setAttribute(ACTIVE_FLOW_KEY, flowType);
		return true;
	}
	
	private String flowKey(FlowType flowType) {
		return String.format("%s.%s", WizardContext.class.getName(), flowType.name());	
	}
}
