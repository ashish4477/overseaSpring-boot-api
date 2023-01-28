package com.bearcode.ovf.eodcommands;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 25, 2007
 * Time: 7:40:36 PM
 * @author Leonid Ginzburg
 */
public class ExcelPortException extends Exception {

    public ExcelPortException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ExcelPortException(String string) {
        super(string);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ExcelPortException(String string, Throwable throwable) {
        super(string, throwable);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ExcelPortException(Throwable throwable) {
        super(throwable);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
