package com.bearcode.commons.filter;

//import com.mysql.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import java.beans.Introspector;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Intended to do some work on application start up and destroy.
 * It's used within Spring Framework Context
 * instead of other listeners used to be within Tomcat context ( and described in web.xml ) 
 *
 * @author Leonid Ginzburg
 */
public class ApplicationLifetimeListener implements  ApplicationListener {

    protected static Logger log = LoggerFactory.getLogger(ApplicationLifetimeListener.class);

    public void onApplicationEvent(ApplicationEvent event) {
        if ( event instanceof ContextRefreshedEvent) {
            // it's time to initialize all beans and static variables
        }

        if ( event instanceof ContextClosedEvent) {
            // flush caches
            try {
                Introspector.flushCaches();
            } catch (Throwable e) {
                log.error("Error Introspector.flushCaches {}", e);
            }

            // Deregister SQL driver
            final Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {

                Driver driver = null;
                try {
                    driver = drivers.nextElement();
                    if (driver.getClass().getClassLoader() == this.getClass().getClassLoader()) {
                        log.info("Deregistering JDBC driver {}", driver);
                        DriverManager.deregisterDriver(driver);
                    }
                } catch (SQLException ex) {
                    log.error("Error deregistering JDBC driver {}", driver, ex);
                }
            }

            // shutdown MySQL Cleanup Thread
      /*      try {
                log.info("Calling MySQL AbandonedConnectionCleanupThread shutdown...");
                com.mysql.jdbc.AbandonedConnectionCleanupThread.shutdown();
            } catch (InterruptedException e) {
                log.error("Error AbandonedConnectionCleanupThread.shutdown {}", e);
            }*/
        }
    }
}
