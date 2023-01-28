package com.bearcode.ovf.model.common;

/**
 * Date: 06.01.12
 * Time: 19:29
 *
 * @author Leonid Ginzburg
 */
public class WizardResultAddress extends UserAddress {
    private static final long serialVersionUID = -3227536245911518317L;

    public WizardResultAddress() {
    }

    public WizardResultAddress( AddressType type ) {
        super( type );
    }

    protected WizardResultAddress( UserAddress userAddress ) {
        super( userAddress );
    }

    public static WizardResultAddress create( UserAddress userAddress ) {
    	return new WizardResultAddress( userAddress );
    }
}
