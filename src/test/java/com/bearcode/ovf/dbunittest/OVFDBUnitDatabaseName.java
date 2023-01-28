/**
 * 
 */
package com.bearcode.ovf.dbunittest;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Specifies the name of the database for OVF DBUnit tests.
 * 
 * @author IanBrown
 * 
 * @since Dec 15, 2011
 * @version Dec 15, 2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OVFDBUnitDatabaseName {

	/**
	 * The name of the database.
	 * 
	 * @author IanBrown
	 * @return the name of the database.
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	String databaseName();
}
