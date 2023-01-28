/**
 * 
 */
package com.bearcode.ovf.dbunittest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the data from the data area should be used for an OVF integration test.
 * 
 * @author IanBrown
 * 
 * @since Apr 13, 2012
 * @version Apr 13, 2012
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface OVFDBUnitUseData {

}
