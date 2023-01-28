package com.bearcode.ovf.service.email;

import com.bearcode.ovf.model.email.RawEmail;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author leonid.
 *
 * Manager to be used in spring AOP to prevent duplication of email sending.
 * uses aspect:around EmailService.sendEmail method to stop sending if email is already in sending state.
 */
@Component
public class EmailSynchroManager implements Ordered {

    @Value( "${emailSynchroManager.order:110}" )  // default 110.
    private int order;

    private static Set<Long> processing = new HashSet<Long>(); // storage for emails id which is sending

    public synchronized boolean checkId( long id ) {
        return processing.add( id );  // true if new element, false if already exists
    }

    public synchronized void freeId( long id ) {
        processing.remove( id );
    }

    public void setOrder( int order ) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void synchronizedSending( ProceedingJoinPoint pjp, RawEmail rawEmail ) throws Throwable {
        if ( checkId( rawEmail.getId() ) ) {
            try {
                pjp.proceed();
            } catch (Throwable throwable) {
                throw throwable;
            } finally {
                freeId( rawEmail.getId() );
            }
        }
    }
}
