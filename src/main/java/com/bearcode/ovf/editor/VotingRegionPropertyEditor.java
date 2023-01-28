package com.bearcode.ovf.editor;

import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

/**
 * Created by IntelliJ IDEA.
 * User: dhughes
 * Date: May 10, 2011
 * Time: 4:09:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class VotingRegionPropertyEditor extends PropertyEditorSupport {

	@Autowired
	private StateService stateService;

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
        setValue(stateService.findRegion(Long.parseLong(text)));
    }

    @Override
    public String getAsText() {
        if ( getValue() != null && getValue() instanceof VotingRegion )
            return String.valueOf( ((VotingRegion)getValue()).getId() );
        return null;
    }
}