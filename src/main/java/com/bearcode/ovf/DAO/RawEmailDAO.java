package com.bearcode.ovf.DAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.email.RawEmailLogStatus;

@Repository
@SuppressWarnings("unchecked")
public class RawEmailDAO extends BearcodeDAO {
    @SuppressWarnings("serial")
    private static final List<RawEmailLogStatus> RETRY_STATUS = new ArrayList<RawEmailLogStatus>() {{
        add(RawEmailLogStatus.INITIAL);
        add(RawEmailLogStatus.RETRY);
    }};

    public List<RawEmail> findAllUnsentEmails(int emailBatchSize) {
        if (emailBatchSize <= 0) emailBatchSize = 1;
        Query query = getSession().createQuery( "from RawEmail where status in (:statuses) and (retryTime is null or retryTime < now() ) order by priority desc, rand() asc " )
                .setParameterList( "statuses", RETRY_STATUS )
                .setMaxResults( emailBatchSize );
        return query.list();
    }

    public int deleteOldEmails(int maxDaysToKeepLogs) {
        final Calendar NDaysAgo = new GregorianCalendar();
        NDaysAgo.add(Calendar.DAY_OF_MONTH, -maxDaysToKeepLogs);

        final Session session = getSession();
        final Criteria criteria = session.createCriteria( RawEmail.class )
                .add(Restrictions.eq("status", RawEmailLogStatus.SENT))
                .add(Restrictions.lt("updated", NDaysAgo.getTime()));

    	final List<RawEmail> oldEmails = criteria.list();
    	for (final RawEmail oldEmail : oldEmails) {
			session.delete( oldEmail );
		}

    	return oldEmails.size();
	}

    /**
     * Remove all old emails with any status which are older than twice days to keep
     * @param maxDaysToKeepLogs number of days to keep
     * @return number of deleted emails
     */
    public int hideOldErrorEmails(int maxDaysToKeepLogs) {
        final Calendar NDaysAgo = new GregorianCalendar();
        NDaysAgo.add( Calendar.DAY_OF_MONTH, -maxDaysToKeepLogs * 2 );

        final Session session = getSession();
        final Criteria criteria = session.createCriteria( RawEmail.class )
                .add(Restrictions.lt("updated", NDaysAgo.getTime()));

        final List<RawEmail> oldEmails = criteria.list();
        for (final RawEmail oldEmail : oldEmails) {
            session.delete( oldEmail );
        }

        return oldEmails.size();
    }


}
