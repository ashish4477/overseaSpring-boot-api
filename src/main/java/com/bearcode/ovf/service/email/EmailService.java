package com.bearcode.ovf.service.email;

import com.bearcode.commons.config.Environment;
import com.bearcode.ovf.DAO.RawEmailDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.email.RawEmail;
import com.bearcode.ovf.model.email.RawEmailLog;
import com.bearcode.ovf.model.email.RawEmailLogStatus;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.Officer;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.utils.FacConfigUtil;
import com.bearcode.ovf.utils.RateLimiter;
import com.bearcode.ovf.utils.RateLimiterFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Leo Date: Oct 11, 2007 Time: 10:38:02 PM
 *
 * @author Leonid Ginzburg
 */

@Service("mailService")
public class EmailService {

    private static final String EMAIL_TO_FIELD_SEPARATOR = "|";

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private RawEmailDAO reDAO;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    @Qualifier("sendEmailThreadPool")
    private TaskExecutor sendEmailThreadPool;

    @Autowired
    private OvfPropertyService propertyService;

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private FacesService facesService;

    @Autowired
    private LocalOfficialService localOfficialService;

    // parameters for using with commons email

    @Value("${mailService.hostName:127.0.0.1}")
    private String hostName;
    @Value("${mailService.hostLogin:info@system.overseasvotefoundation.org}")
    private String hostLogin;
    @Value("${mailService.hostPassword:1nf0a0vF}")
    private String hostPassword;
    private int hostPort;
    private String hostSslPort;
    private boolean useSsl;
    private boolean useTls;

    @Value("${mailService.fromEmail:info@system.overseasvotefoundation.org}")
    private String fromEmail;

    @Value("${mailService.skimmSESConfigset:skimm}")
    private String skimmSESConfigset;
    @Value("${mailService.vote411SESConfigset:vote411}")
    private String vote411SESConfigset;

    private String bccEmail;

    private boolean debug = false;


    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setBccEmail(String bccEmail) {
        this.bccEmail = bccEmail;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setHostLogin(String hostLogin) {
        this.hostLogin = hostLogin;
    }

    public void setHostPassword(String hostPassword) {
        this.hostPassword = hostPassword;
    }

    public boolean isUseSsl() {
        return useSsl;
    }

    public void setUseSsl(boolean useSsl) {
        this.useSsl = useSsl;
    }

    public String getHostSslPort() {
        return hostSslPort;
    }

    public void setHostSslPort(String hostSslPort) {
        this.hostSslPort = hostSslPort;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public boolean isUseTls() {
        return useTls;
    }

    public void setUseTls(boolean useTls) {
        this.useTls = useTls;
    }


    public int getMaxNumberOfAttempts() {
        return propertyService.getPropertyAsInt(OvfPropertyNames.EMAIL_SERVICE_NUMBER_OF_RETRY_ATTEMPT);
    }


    public int getMaxDaysToKeepLogs() {
        return propertyService.getPropertyAsInt(OvfPropertyNames.EMAIL_SERVICE_DAYS_TO_KEEP_LOG);
    }

    public int getRateLimitMaxEmails() {
        return propertyService.getPropertyAsInt(OvfPropertyNames.EMAIL_SERVICE_RATE_MAX_CALLS);
    }

    public long getRateLimitMaxTimeInSeconds() {
        return propertyService.getPropertyAsLong( OvfPropertyNames.EMAIL_SERVICE_RATE_PER_SECOND );
    }

    public long getMillisForDelay() {
        return propertyService.getPropertyAsLong(OvfPropertyNames.EMAIL_SERVICE_MILLIS_FOR_RETRY_DELAY);
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostLogin() {
        return hostLogin;
    }

    public String getHostPassword() {
        return hostPassword;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getBccEmail() {
        return bccEmail;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getEmailBatchSize() {
        return propertyService.getPropertyAsInt(OvfPropertyNames.EMAIL_SERVICE_BATCH_SIZE);
    }

    public String getSkimmSESConfigset() {
        return skimmSESConfigset;
    }
    public String getVote411SESConfigset() {
        return vote411SESConfigset;
    }

    public void setSkimmSESConfigset(String skimmSESConfigset) {
        this.skimmSESConfigset = skimmSESConfigset;
    }
    public void setVote411SESConfigset(String vote411SESConfigset) {
        this.vote411SESConfigset = vote411SESConfigset;
    }

    /**
     * Add an email to the email's queue
     *
     * @param email Email
     * @return <code>true</code> if email was successfully added to the queue otherwise <code>false</code>
     */

//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void queue(final Email email) throws EmailException {
        try {
            log.info("Add email " + email.getToAsString() + " to a message queue");
            final RawEmail rawEmail = this.createRawEmail(email);
            if (rawEmail == null) {
                log.error("Cannot queue email " + email.getToAsString() + " to a message queue, email address is invalid");
            }
            reDAO.makePersistent(rawEmail);

        } catch (Exception e) {
            log.error("Cannot add email " + email.getToAsString() + " to a message queue", e);
            throw new EmailException("Cannot queue email", e);
        }
    }

    /**
     * Delete all emails which were successfully SENT and older than <code>maxDaysToKeepLogs</code> days.
     */
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteOldEmails() {
        int deletedRows = reDAO.deleteOldEmails(getMaxDaysToKeepLogs());
        if (deletedRows > 0) {
            log.info("Deleted " + deletedRows + " old emails");
        }
    }

    /**
     * Delete all emails which were successfully SENT and older than <code>maxDaysToKeepLogs</code> days.
     */
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteOldErrorEmails() {
        int deletedRows = reDAO.hideOldErrorEmails(getMaxDaysToKeepLogs());
        if (deletedRows > 0) {
            log.info("Changed status for " + deletedRows + " old emails with errors");
        }
    }

    /**
     * Send all queued emails
     *
     * @return
     * @throws EmailException
     */
//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<RawEmail> findUnsentEmails() throws Exception {
        return reDAO.findAllUnsentEmails(getEmailBatchSize());
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void sendEmail(final RawEmail email) {
        final RateLimiter rateLimiter = RateLimiterFactory.getRateLimiter( "emailService",
                getRateLimitMaxEmails(), getRateLimitMaxTimeInSeconds() );

        try {
            final HtmlEmail htmlEmail = this.createHtmlEmail(email);
            log.info("Send email(id=" + email.getId() + ")");

            // we want to apply rate limits only to the send method, even if exception happens
            try {
                rateLimiter.checkRates();
                htmlEmail.send();
            } finally {
                rateLimiter.update();
            }

            email.setStatus(RawEmailLogStatus.SENT);
            reDAO.makePersistent(email);

            this.logEmail(email, RawEmailLogStatus.SENT);

        } catch (EmailException e) {
            log.error("Cannot send email(id=" + email.getId() + ")", e);
            // create log record
            this.logEmail(email, RawEmailLogStatus.ERROR, e);
            handleEmailException(email, e);
        }
    }

    private void handleEmailException(final RawEmail email, EmailException e) {
        boolean tryAgain = true;
        if (ExceptionUtils.indexOfThrowable(e, AddressException.class) != -1) {
            tryAgain = false;
        }

        // update mail status, attempt number and error message
        final int attempt = email.getAttempt();
        if (attempt + 1 < getMaxNumberOfAttempts() && tryAgain) {
            email.setAttempt(attempt + 1);
            email.setRetryTime(this.getNextRetryTime(attempt + 1));
            email.setStatus(RawEmailLogStatus.RETRY);
        } else {
            email.setStatus(RawEmailLogStatus.ERROR);
        }

        email.setError(ExceptionUtils.getMessage(e));
        reDAO.makePersistent(email);
    }

    private RawEmail createRawEmail(final Email email) throws EmailException {
        final RawEmail rawEmail = new RawEmail();
        rawEmail.setCreated(new Date());
        if (email.getModel().containsKey("priority")) {
            rawEmail.setPriorityLevel( ((RawEmail.Priority) email.getModel().get( "priority" )) );
        }

        final String templatePath = email.getTemplate();
        if (StringUtils.isNotEmpty(templatePath)) {
            final Environment xmlConfig = new Environment(templatePath.replaceAll("WEB-INF", ".."));

            final String sendFrom = xmlConfig.getStringProperty(Email.FROM, fromEmail);
            final String sendTo = xmlConfig.getCompoundStringProperty(Email.TO, "");
            final String replyTo = xmlConfig.getStringProperty(Email.REPLY_TO, "");
            final String subject = xmlConfig.getStringProperty(Email.SUBJECT, "");
            // hack to put spaces after commas back in, which are stripped by the environment XML parser
            final String template = xmlConfig.getCompoundStringProperty(Email.BODY_TEMPLATE, "").replaceAll(",", ", ");
            final String message = generateEmailMessage(template, email.getModel());

            rawEmail.setTemplatePath(templatePath);

            // set from field
            rawEmail.setFrom(sendFrom);

            // set reply-to field
            if (StringUtils.isNotBlank(replyTo)) {
                if (!EmailValidator.getInstance().isValid(replyTo.trim())) {
                    return null;
                }
                rawEmail.setReplyTo(replyTo.trim());
            }

            rawEmail.setBcc(email.isBcc());

            // add any To addresses in the template
            final List<String> tos = new ArrayList<String>();
            if (StringUtils.isNotBlank(sendTo)) {
                for (final String to : sendTo.split("\\s*,\\s*")) {
                    if (!EmailValidator.getInstance().isValid(to)) {
                        return null;
                    }
                    tos.add(to);
                }
            }

            // add any To addresses in the Email
            for (final String to : email.getTo()) {
                if (!EmailValidator.getInstance().isValid(to)) {
                    return null;
                }
                tos.add(to);
            }

            rawEmail.setTo(StringUtils.join(tos, EMAIL_TO_FIELD_SEPARATOR));
            rawEmail.setSubject(subject);
            rawEmail.setBody(message);
        } else {
            // set from field
            rawEmail.setFrom(email.getFrom());

            // set reply-to field
            if (StringUtils.isNotBlank(email.getReplyTo())) {
                if (!EmailValidator.getInstance().isValid(email.getReplyTo().trim())) {
                    return null;
                }
                rawEmail.setReplyTo(email.getReplyTo().trim());
            }

            rawEmail.setBcc(email.isBcc());

            final List<String> tos = new ArrayList<String>();
            for (final String to : email.getTo()) {
                if (!EmailValidator.getInstance().isValid(to)) {
                    return null;
                }
                tos.add(to);
            }

            rawEmail.setTo(StringUtils.join(tos, EMAIL_TO_FIELD_SEPARATOR));

            rawEmail.setSubject(email.getSubject());
            final String message = generateEmailMessage(email.getTemplateBody(), email.getModel());
            rawEmail.setBody(message);

        }

        return rawEmail;
    }


    private HtmlEmail createHtmlEmail(final RawEmail email) throws EmailException {
        final HtmlEmail htmlEmail = new HtmlEmail();
        setHostDetails(htmlEmail,email);

        if (hostPort > 0)
            htmlEmail.setSmtpPort(hostPort);

        htmlEmail.setStartTLSEnabled(useTls);
        htmlEmail.setSSLOnConnect(useSsl);
        htmlEmail.setSslSmtpPort(hostSslPort);

        // set from field
        htmlEmail.setFrom(email.getFrom());

        // set reply-to field
        if (StringUtils.isNotBlank(email.getReplyTo())) {
            htmlEmail.addReplyTo(email.getReplyTo());
        }

        // set bcc field
        if (email.isBcc() && bccEmail != null && StringUtils.isNotBlank(bccEmail)) {
            htmlEmail.addBcc(bccEmail);
        }

        if (StringUtils.isNotBlank(email.getTo())) {
            for (final String to : StringUtils.split(email.getTo().trim(), EMAIL_TO_FIELD_SEPARATOR)) {
                htmlEmail.addTo(to);
            }
        }

        htmlEmail.setSubject(email.getSubject());
        htmlEmail.setHtmlMsg(email.getBody());
        htmlEmail.setDebug(debug);

        return htmlEmail;
    }

    private void setHostDetails(HtmlEmail htmlEmail, RawEmail email) {
        htmlEmail.setHostName(hostName);
        if (StringUtils.isNotBlank(hostLogin) && StringUtils.isNotBlank(hostPassword)) {
            htmlEmail.setAuthentication(hostLogin, hostPassword);
        }
        if (FacConfigUtil.isSkimm(email.getTemplatePath())) {
            htmlEmail.addHeader("X-SES-CONFIGURATION-SET", skimmSESConfigset);
        } else if (FacConfigUtil.isVote411(email.getTemplatePath())) {
            htmlEmail.addHeader("X-SES-CONFIGURATION-SET", vote411SESConfigset);
        }
    }

    @SuppressWarnings("rawtypes")
    private String  generateEmailMessage(final String template, final Map model) throws EmailException {

        try {
            final StringWriter message = new StringWriter();
            final ToolManager toolManager = new ToolManager();
            final ToolContext toolContext = toolManager.createContext();
            final VelocityContext context = new VelocityContext(model, toolContext);

            final String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            context.internalPut("year", currentYear);
            context.internalPut("electionYear", currentYear);

            velocityEngine.evaluate(context, message, "make email body", template);
            return message.getBuffer().toString();

        } catch (Exception e) {
            throw new EmailException("Can't create email body", e);
        }
    }

    private void logEmail(RawEmail email, RawEmailLogStatus status, Exception e) {
        final RawEmailLog rawEmailLog = new RawEmailLog();
        rawEmailLog.setRawEmail( email );
        rawEmailLog.setStatus(status);
        if (e != null) {
            rawEmailLog.setError( ExceptionUtils.getStackTrace( e ) );
        }
        reDAO.makePersistent(rawEmailLog);
    }

    private void logEmail(RawEmail email, RawEmailLogStatus status) {
        this.logEmail(email, status, null);
    }

    private Date getNextRetryTime(int attemptNumber) {
        long waitTime = ((long) Math.pow(2, attemptNumber) * getMillisForDelay());
        return new Date(System.currentTimeMillis() + waitTime);
    }

    public void sendEmail(final WizardResults answers, final FaceConfig config ) throws EmailException {
        final String userName = answers.getUsername(); // userName is email
        if ( userName == null || userName.trim().isEmpty() ) {
            return;
        }

        final LocalOfficial leo = localOfficialService.findForRegion( answers.getVotingRegion() );
        Officer registrar;
        if ( leo == null || leo.getOfficers() == null || leo.getOfficers().size() == 0 ) {
            registrar = new Officer();
        }
        else {
            registrar = leo.getOfficers().get(0);
        }
        final String firstName = answers.getName().getFirstName();
        final Email email = Email.builder()
                .to( userName )
                .template( getMailTemplateName( answers.getFlowType(), config ) )
                .model( "firstName", firstName )
                .model( "user", answers.getUser() )
                .model( "leo", leo )
                .model( "registrar", registrar )
                .model( "priority", RawEmail.Priority.HIGHEST )
                .model("id",answers.getUuid())
                .model("url", config.getUrlPath())
                .build();
        queue( email );
    }

    private String getMailTemplateName(final FlowType flowType, final FaceConfig config ) {
        String filePath = EmailTemplates.getEmailTemplate( flowType );
        return facesService.getApprovedFileName( filePath, config.getRelativePrefix() );
    }
}
