package com.bearcode.ovf.service;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.zoho.api.authenticator.OAuthToken;
import com.zoho.api.authenticator.Token;
import com.zoho.api.authenticator.store.DBStore;
import com.zoho.api.authenticator.store.TokenStore;
import com.zoho.api.logger.Logger;
import com.zoho.crm.api.HeaderMap;
import com.zoho.crm.api.Initializer;
import com.zoho.crm.api.ParameterMap;
import com.zoho.crm.api.SDKConfig;
import com.zoho.crm.api.UserSignature;
import com.zoho.crm.api.dc.DataCenter;
import com.zoho.crm.api.dc.USDataCenter;
import com.zoho.crm.api.exception.SDKException;
import com.zoho.crm.api.record.APIException;
import com.zoho.crm.api.record.ActionHandler;
import com.zoho.crm.api.record.ActionResponse;
import com.zoho.crm.api.record.ActionWrapper;
import com.zoho.crm.api.record.BodyWrapper;
import com.zoho.crm.api.record.Record;
import com.zoho.crm.api.record.RecordOperations;
import com.zoho.crm.api.record.ResponseHandler;
import com.zoho.crm.api.record.ResponseWrapper;
import com.zoho.crm.api.record.SuccessResponse;
import com.zoho.crm.api.util.APIResponse;
import com.zoho.crm.api.util.Choice;
import com.zoho.crm.api.util.Model;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@SuppressWarnings("Duplicates")
@Service
public class ZohoService {

    private static final String CONTACT_MODULE = "Contacts";
    @Autowired
    private OvfPropertyService propertyService;
    protected org.slf4j.Logger zohoLogger = LoggerFactory.getLogger(ZohoService.class);

    private void initialization() {
        try {
            Logger logger = new Logger.Builder()
                    .level(Logger.Levels.INFO)
                    .filePath(System.getenv("ZOHO_LOG_PATH"))
                    .build();

            UserSignature user = new UserSignature(System.getenv("ZOHO_USER"));
            DataCenter.Environment environment = USDataCenter.PRODUCTION;
            String grant_token = propertyService.getProperty(OvfPropertyNames.GRANT_TOKEN);
            zohoLogger.info("ZOHO_CLIENT_ID: " + System.getenv("ZOHO_CLIENT_ID"));
            zohoLogger.info("ZOHO_CLIENT_SECRET: " + System.getenv("ZOHO_CLIENT_SECRET"));
            Token token = new OAuthToken.Builder()
                    .clientID(System.getenv("ZOHO_CLIENT_ID"))
                    .clientSecret(System.getenv("ZOHO_CLIENT_SECRET"))
                    .grantToken(grant_token)
                    .redirectURL("https://www.usvotefoundation.org/")
                    .build();

            String dbUrl = System.getenv("DB_URL");
            String cleanURI = dbUrl.substring(5);
            URI uri = URI.create(cleanURI);
            String host = uri.getHost();
            String path = uri.getPath();
            String database = StringUtils.substringBefore(StringUtils.substringAfterLast(path, "/"), "?");
            TokenStore tokenstore = new DBStore.Builder()
                    .host(host)
                    .databaseName(database)
                    .tableName("zoho_token")
                    .userName(System.getenv("DB_USER"))
                    .password(System.getenv("DB_PASS"))
                    .build();

            SDKConfig sdkConfig = new SDKConfig.Builder()
                    .autoRefreshFields(false)
                    .pickListValidation(true)
                    .build();

            String resourcePath = System.getenv("ZOHO_RESOURCE_PATH");
            new Initializer.Builder()
                    .user(user)
                    .environment(environment)
                    .token(token)
                    .store(tokenstore)
                    .resourcePath(resourcePath)
                    .logger(logger)
                    .SDKConfig(sdkConfig)
                    .initialize();
        } catch (Exception e) {
            zohoLogger.error("Exception occurred: {}", e.getMessage());
        }

    }

    // Add the WHOLE user data
    private List<Record> populateOverseasUserData(OverseasUser user) {
        List<Record> records = new ArrayList();
        Record record = new Record();
        record.addKeyValue("Title", user.getName().getTitle());
        record.addKeyValue("First_Name", user.getName().getFirstName());
        record.addKeyValue("Middle_Name", user.getName().getMiddleName());
        record.addKeyValue("Last_Name", ((user.getName().getLastName() != null && !user.getName().getLastName().isEmpty()) ? user.getName().getLastName() : user.getName().getFirstName()));
        record.addKeyValue("Suffix", user.getName().getSuffix());
        record.addKeyValue("Email", user.getUsername());
        record.addKeyValue("Date_Added", LocalDate.now());
        if (user.getBirthYear() > 1900) {
            record.addKeyValue("Date_of_Birth", LocalDate.of(user.getBirthYear(), user.getBirthMonth(), user.getBirthDate()));
        }
        if (user.getCurrentAddress() != null) {
            record.addKeyValue("Current_Street", user.getCurrentAddress().getFullStreet());
            record.addKeyValue("Current_City", user.getCurrentAddress().getCity());
            record.addKeyValue("Current_State", user.getCurrentAddress().getState());
            record.addKeyValue("Current_Zip", user.getCurrentAddress().getZip());
            record.addKeyValue("Current_County", user.getCurrentAddress().getCounty());
        }
        record.addKeyValue("Secondary_Email", user.getAlternateEmail());
        record.addKeyValue("Email_Opt_Out", user.isEmailOptOut());
        record.addKeyValue("Description", user.getAlternateEmail());
        if (user.getVotingAddress() != null) {
            record.addKeyValue("FIPS_City", user.getVotingAddress().getCity());
            record.addKeyValue("Single_Line_12", user.getVotingAddress().getCounty());
            record.addKeyValue("Voting_State1", new Choice(user.getVotingAddress().getState()));
            record.addKeyValue("Voting_Zip", user.getVotingAddress().getZip());
        }
        record.addKeyValue("Voting_Region", user.getEodRegionName());
        record.addKeyValue("Voter_Alert_Only", user.isVoterAlertOnly());
        records.add(record);
        return records;
    }

    // Add the Voter_Type, Primary_Voting_Method, and Political_Party fields
    private List<Record> populateVoterInformationUserData(String voterType, String votingMethod, String politicalParty) {
        List<Record> records = new ArrayList();
        Record record = new Record();
        if (!StringUtils.isEmpty(voterType)) {
            record.addKeyValue("Voter_Type_Test", new Choice(voterType));
        }
        if (!StringUtils.isEmpty(votingMethod)) {
            record.addKeyValue("Primary_Voting_Method", new Choice(votingMethod));
        }
        if (!StringUtils.isEmpty(politicalParty)) {
            record.addKeyValue("Political_Party", new Choice(politicalParty));
        }
        records.add(record);
        return records;
    }

    // Analyze the response
    private void analyzeResponse(APIResponse response) {
        if (response != null) {
            zohoLogger.debug("Status Code: {}", response.getStatusCode());
            if (response.isExpected()) {
                ActionHandler actionHandler = (ActionHandler) response.getObject();
                if (actionHandler instanceof ActionWrapper) {
                    ActionWrapper actionWrapper = (ActionWrapper) actionHandler;
                    List<ActionResponse> actionResponses = actionWrapper.getData();

                    for (ActionResponse actionResponse : actionResponses) {
                        if (actionResponse instanceof SuccessResponse) {
                            SuccessResponse successResponse = (SuccessResponse) actionResponse;
                            zohoLogger.debug("Status: " + successResponse.getStatus().getValue());
                            zohoLogger.debug("Code: " + successResponse.getCode().getValue());
                            zohoLogger.debug("Details: ");

                            //Get the details map
                            for (Map.Entry entry : successResponse.getDetails().entrySet()) {
                                zohoLogger.debug(entry.getKey() + ": " + entry.getValue());
                            }
                            zohoLogger.debug("Message: " + successResponse.getMessage().getValue());
                        }
                        //Check if the request returned an exception
                        else if (actionResponse instanceof APIException) {
                            APIException exception = (APIException) actionResponse;
                            zohoLogger.debug("Status: " + exception.getStatus().getValue());
                            zohoLogger.debug("Code: " + exception.getCode().getValue());
                            zohoLogger.debug("Details: ");
                            for (Map.Entry entry : exception.getDetails().entrySet()) {
                                zohoLogger.debug(entry.getKey() + ": " + entry.getValue());
                            }
                            zohoLogger.debug("Message: " + exception.getMessage().getValue());
                        }
                    }
                }
                //Check if the request returned an exception
                else if (actionHandler instanceof APIException) {
                    APIException exception = (APIException) actionHandler;
                    zohoLogger.debug("Status: " + exception.getStatus().getValue());
                    zohoLogger.debug("Code: " + exception.getCode().getValue());
                    zohoLogger.debug("Details: ");
                    for (Map.Entry entry : exception.getDetails().entrySet()) {
                        zohoLogger.debug(entry.getKey() + ": " + entry.getValue());
                    }
                    zohoLogger.debug("Message: " + exception.getMessage().getValue());
                }
            } else {
                Model responseObject = response.getModel();
                Class<? extends Model> clazz = responseObject.getClass();
                java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    try {
                        zohoLogger.debug(field.getName() + ":" + field.get(responseObject));
                    } catch (IllegalAccessException e) {
                        zohoLogger.error("Exception occurred: {}", e.getMessage());
                    }
                }
            }
        }
    }

    private BodyWrapper prepareBodyWrapper(List<Record> records) {
        BodyWrapper bodyWrapper = new BodyWrapper();
        bodyWrapper.setData(records);
        List trigger = new ArrayList();
        trigger.add("approval");
        trigger.add("workflow");
        trigger.add("blueprint");
        bodyWrapper.setTrigger(trigger);
        bodyWrapper.setLarId("34770610087515");
        return bodyWrapper;
    }

    // Create the Records in Zoho
    public void sendContactToZoho(OverseasUser user) {
        initialization();
        List<Record> records = populateOverseasUserData(user);
        BodyWrapper bodyWrapper = prepareBodyWrapper(records);
        HeaderMap headerInstance = new HeaderMap();
        RecordOperations recordOperations = new RecordOperations();
        APIResponse response = null;
        try {
            response = recordOperations.createRecords(CONTACT_MODULE, bodyWrapper, headerInstance);
        } catch (Exception e) {
            zohoLogger.error("Exception occurred: {}", e.getMessage());
        }
        analyzeResponse(response);
    }

    // Get the contact with the email
    Record fetchContactFromZoho(String email) {

        RecordOperations recordOperations = new RecordOperations();
        ParameterMap paramInstance = new ParameterMap();
        APIResponse response = null;
        try {
            paramInstance.add(RecordOperations.SearchRecordsParam.EMAIL, email);
            HeaderMap headerInstance = new HeaderMap();
            response = recordOperations.searchRecords(CONTACT_MODULE, paramInstance, headerInstance);
        } catch (Exception e) {
            zohoLogger.error("Exception occurred: {}", e.getMessage());
        }
        if (response != null) {
            if (Arrays.asList(204, 304).contains(response.getStatusCode())) {
                zohoLogger.debug(response.getStatusCode() == 204 ? "No Content" : "Not Modified");
                return null;
            }

            if (response.isExpected()) {
                ResponseHandler responseHandler = (ResponseHandler) response.getObject();
                if (responseHandler instanceof ResponseWrapper) {
                    ResponseWrapper responseWrapper = (ResponseWrapper) responseHandler;
                    return (responseWrapper.getData().isEmpty() ? null : responseWrapper.getData().get(0));
                } else if (responseHandler instanceof APIException) {
                    APIException exception = (APIException) responseHandler;
                    zohoLogger.debug("Status: " + exception.getStatus().getValue());
                    zohoLogger.debug("Code: " + exception.getCode().getValue());
                    zohoLogger.debug("Details: ");
                    for (Map.Entry entry : exception.getDetails().entrySet()) {
                        zohoLogger.debug(entry.getKey() + ": " + entry.getValue());
                    }
                    zohoLogger.debug("Message: " + exception.getMessage().getValue());
                }
            } else {
                Model responseObject = response.getModel();
                Class<? extends Model> clazz = responseObject.getClass();
                java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    try {
                        zohoLogger.debug(field.getName() + ":" + field.get(responseObject));
                    } catch (IllegalAccessException e) {
                        zohoLogger.error("Exception occurred: {}", e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    // Update the contact in Zoho
    public void updateContactToZoho(OverseasUser user) {
        initialization();
        Record record = fetchContactFromZoho(user.getUsername());
        if (record != null) {
            RecordOperations recordOperations = new RecordOperations();
            List<Record> records = populateOverseasUserData(user);
            BodyWrapper request = prepareBodyWrapper(records);
            HeaderMap headerInstance = new HeaderMap();
            APIResponse response = null;
            try {
                response = recordOperations.updateRecord(record.getId(), CONTACT_MODULE, request, headerInstance);
            } catch (Exception e) {
                zohoLogger.error("Exception occurred: {}", e.getMessage());
            }
            analyzeResponse(response);
        }
    }

    public void voterAlertOptInToZoho(String username) {
        initialization();
        Record record = fetchContactFromZoho(username);
        if (record != null) {
            RecordOperations recordOperations = new RecordOperations();
            List<Record> records = new ArrayList();
            Record data = new Record();
            data.addKeyValue("Email_Opt_Out", true);
            records.add(data);
            BodyWrapper request = prepareBodyWrapper(records);
            HeaderMap headerInstance = new HeaderMap();
            APIResponse response = null;
            try {
                response = recordOperations.updateRecord(record.getId(), CONTACT_MODULE, request, headerInstance);
            } catch (Exception e) {
                zohoLogger.error("Exception occurred: {}", e.getMessage());
            }
            analyzeResponse(response);
        }
    }

    public void updateVoterInformationToZoho(String email, String voterType, String votingMethod, String politicalParty) {
        initialization();
        Record record = fetchContactFromZoho(email);
        if (record != null) {
            RecordOperations recordOperations = new RecordOperations();
            String zohoVoterType = VoterType.UNSPECIFIED.getZohoValue();
            if (StringUtils.isNotEmpty(voterType)) {
                try {
                    zohoVoterType = VoterType.valueOf(voterType).getZohoValue();
                } catch (IllegalArgumentException e) {
                    zohoLogger.error("Exception occurred: {}", e.getMessage());
                }
            }

            List<Record> records = populateVoterInformationUserData(zohoVoterType, votingMethod, politicalParty);
            BodyWrapper request = prepareBodyWrapper(records);
            HeaderMap headerInstance = new HeaderMap();
            APIResponse response = null;
            try {
                response = recordOperations.updateRecord(record.getId(), CONTACT_MODULE, request, headerInstance);
            } catch (Exception e) {
                zohoLogger.error("Exception occurred: {}", e.getMessage());
            }
            analyzeResponse(response);
        }
    }

    public OvfPropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService(OvfPropertyService propertyService) {
        this.propertyService = propertyService;
    }
}
