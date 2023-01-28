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
 * Specifies the DBUnit data set to use for an OVF test.
 * 
 * @author IanBrown
 * 
 * @since Dec 15, 2011
 * @version Dec 15, 2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface OVFDBUnitDataSet {

	/**
	 * The list of flat XML data set files to load.
	 * 
	 * @author IanBrown
	 * @return the data set list.
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	String[] dataSetList();
}
