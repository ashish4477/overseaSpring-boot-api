package com.bearcode.ovf.model.eod;

import com.bearcode.ovf.model.common.State;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Leonid Ginzburg
 */
public class StateSpecificDirectory extends AbstractStateSpecificDirectory {
    private State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
