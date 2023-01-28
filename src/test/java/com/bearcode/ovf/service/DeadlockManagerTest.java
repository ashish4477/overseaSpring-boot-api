package com.bearcode.ovf.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.hibernate.StaleObjectStateException;
import org.hibernate.StaleStateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import static org.junit.Assert.*;

/**
 * Created by leonid on 05.09.16.
 */
public class DeadlockManagerTest extends EasyMockSupport {

    private DeadlockManager deadlockManager;

    @Before
    public void setup() {
        deadlockManager = new DeadlockManager();
        deadlockManager.setMaxRetries( 3 );
    }

    @Test
    public void oneRun_ok()  throws Throwable {
        ProceedingJoinPoint pjp = createMock( "joinPoint", ProceedingJoinPoint.class );
        EasyMock.expect( pjp.proceed() ).andReturn( null );
        replayAll();

        Object res = deadlockManager.handleDeadlock( pjp );

        assertNull( "Result should be NULL", res );
        verifyAll(); // no exception expected. returns null

    }

    @Test(expected = HibernateOptimisticLockingFailureException.class)
    public void threeRuns_exception() throws Throwable {
        ProceedingJoinPoint pjp = createMock( "joinPoint", ProceedingJoinPoint.class );
        EasyMock.expect( pjp.proceed() ).andThrow( new HibernateOptimisticLockingFailureException(
                        new StaleObjectStateException( "exception text", "serialazable" ) )
        ).times( 3 );
        replayAll();

        Object res = deadlockManager.handleDeadlock( pjp );
        verifyAll();  // exception expected
    }

    @Test
    public void twoRuns_exceptionAndNull() throws Throwable {
        ProceedingJoinPoint pjp = createMock( "joinPoint", ProceedingJoinPoint.class );
        EasyMock.expect( pjp.proceed() ).andThrow( new HibernateOptimisticLockingFailureException(
                        new StaleObjectStateException( "exception text", "serialazable" ) )
        ).once();
        EasyMock.expect( pjp.proceed() ).andReturn( null ).once();
        replayAll();

        Object res = deadlockManager.handleDeadlock( pjp );

        assertNull( "Result should be NULL", res );
        verifyAll();  // no exception expected. returns null
    }

    @Test( expected = RuntimeException.class )
    public void oneRun_anotherException() throws Throwable {
        ProceedingJoinPoint pjp = createMock( "joinPoint", ProceedingJoinPoint.class );
        EasyMock.expect( pjp.proceed() ).andThrow( new RuntimeException( "runtime exception text" ) ).once();
        replayAll();

        Object res = deadlockManager.handleDeadlock( pjp );
        verifyAll();  // exception expected

    }

}

