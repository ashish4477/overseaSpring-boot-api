package com.bearcode.ovf.service;

import com.sleepycat.je.txn.DummyLockManager;
import org.apache.commons.lang.math.RandomUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import java.util.ConcurrentModificationException;

/**
 * Created by leonid on 01.09.16.
 *
 * Class to take care of Hibernate Exceptions caused by deadlocks.
 */
public class DeadlockManager implements Ordered {
    private final Logger logger = LoggerFactory.getLogger( DummyLockManager.class );

    private static final int DEFAULT_MAX_RETRIES = 2;

    private int order = 1;

    private int maxRetries = DEFAULT_MAX_RETRIES;


    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder( int order ) {
        this.order = order;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries( int maxRetries ) {
        this.maxRetries = maxRetries;
    }

    public Object handleDeadlock( ProceedingJoinPoint pjp ) throws Throwable {
        Exception exception;
        int attempt = 0;
        do {
            attempt++;
            try {
                return pjp.proceed();
            }
            catch (HibernateOptimisticLockingFailureException ex) {
                exception = ex;
            }
            catch (CannotAcquireLockException ex) {
                exception = ex;
            }
            catch ( ConcurrentModificationException ex ) {
                exception = ex;
            }
            int timeout = RandomUtils.nextInt( 1500 );
            Thread.sleep(timeout);
        }
        while ( attempt < maxRetries );
        logger.error( String.format( "Tried to deal with %s. Number of attempts %d.", exception.getClass().getSimpleName(), maxRetries ) );
        throw exception;
    }
}
