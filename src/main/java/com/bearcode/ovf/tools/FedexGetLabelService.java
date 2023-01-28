package com.bearcode.ovf.tools;

import com.bearcode.ovf.actions.express.forms.ExpressForm;
import com.bearcode.ovf.model.express.FedexLabel;
import com.fedex.ship.stub.*;
import org.apache.axis.types.NonNegativeInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 3, 2008
 * Time: 5:09:54 PM
 * @author Leonid Ginzburg
 */
public class FedexGetLabelService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String clientAccountNumber;
    private String clientMeterNumber;
    private String authenticationKey;
    private String authenticationPassword;
    private String webserviceAddress;
    private String labelsStorageDir;

    public void setClientAccountNumber(String clientAccountNumber) {
        this.clientAccountNumber = clientAccountNumber;
    }

    public void setClientMeterNumber(String clientMeterNumber) {
        this.clientMeterNumber = clientMeterNumber;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public void setAuthenticationPassword(String authenticationPassword) {
        this.authenticationPassword = authenticationPassword;
    }

    public void setLabelsStorageDir(String labelsStorageDir) {
        this.labelsStorageDir = labelsStorageDir;
    }

    public boolean getFedexLabel( ExpressForm form )  {
        boolean result = false;
        ProcessShipmentRequest fedexRequest = buildRequest( form ); // Build a request object
        // Initialize the service
        ShipServiceLocator service;
        ShipPortType port;
        //
        service = new ShipServiceLocator();
        service.setShipServicePortEndpointAddress(webserviceAddress);
        //updateEndPoint(service);
        try {
            port = service.getShipServicePort();
            //
            ProcessShipmentReply reply = port.processShipment(fedexRequest); // This is the call to the ship web service passing in a request object and returning a reply object
            //
            if (isResponseOk(reply.getHighestSeverity())) // check if the call was successful
            {
                FedexLabel fedexLabel = new FedexLabel();
                String message = null;
                try {
                    message = writeServiceOutput(reply, fedexLabel);
                    form.setFedexMessage( message );
                    form.setFedexLabel( fedexLabel );
                    result = true;
                } catch (IOException e) {
                    form.setFedexMessage( "Label file could not be created." );
                }

            }
            else {
                form.setFedexMessage( createNotifications(reply.getNotifications()) );
            }
        } catch (ServiceException e) {
        	logger.info( "ServiceException creating FedexLabel", e);
            form.setFedexMessage( "Error connecting to Remote Service." );
        } catch (RemoteException e) {
        	logger.info( "RemoteException creating FedexLabel", e);
            form.setFedexMessage( "Error connecting to Remote Service." );
        } catch (Exception e){
            logger.info( "Error creating FedexLabel", e);
            form.setFedexMessage( "Error connecting to Remote Service." );
        }
        //
        return result;
    }

    private ProcessShipmentRequest buildRequest( ExpressForm form )
    {
        ProcessShipmentRequest request = new ProcessShipmentRequest(); // Build a request object
        Localization  localization = new Localization("EN","");
        request.setClientDetail(createClientDetail(localization));
        request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        //
        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setCustomerTransactionId("International Ground Shipment"); // The client will get the same value back in the response
        transactionDetail.setLocalization(localization);
        request.setTransactionDetail(transactionDetail);

        //
        VersionId versionId = new VersionId("ship", 12, 0, 0);
        request.setVersion(versionId);

        //
        RequestedShipment requestedShipment = new RequestedShipment();
        requestedShipment.setShipTimestamp(Calendar.getInstance()); // Ship date and time
        requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP);
        requestedShipment.setServiceType(ServiceType.INTERNATIONAL_PRIORITY); // Service types are STANDARD_OVERNIGHT, PRIORITY_OVERNIGHT, FEDEX_GROUND ...
        requestedShipment.setPackagingType(PackagingType.FEDEX_ENVELOPE); // Packaging type FEDEX_BOK, FEDEX_PAK, FEDEX_TUBE, YOUR_PACKAGING, ...

        // email notification
        EMailNotificationRecipient emailReceipt = new EMailNotificationRecipient();
        emailReceipt.setEMailAddress(form.getNotificationEmail());
        emailReceipt.setLocalization(localization);
        EMailNotificationEventType[] notifications = new EMailNotificationEventType[] {
            EMailNotificationEventType.ON_DELIVERY,
            EMailNotificationEventType.ON_EXCEPTION,
            EMailNotificationEventType.ON_TENDER,
        };
        emailReceipt.setNotificationEventsRequested(notifications);
        emailReceipt.setFormat(EMailNotificationFormatType.TEXT);
        emailReceipt.setEMailNotificationRecipientType(EMailNotificationRecipientType.SHIPPER);
        EMailNotificationRecipient[] recipients = new EMailNotificationRecipient[]{emailReceipt};

        EMailNotificationDetail emailDetail = new EMailNotificationDetail();
        emailDetail.setRecipients(recipients);   
        emailDetail.setPersonalMessage("This email is regarding your Express Your Vote shipment.");

        ShipmentSpecialServicesRequested emailNotification = new ShipmentSpecialServicesRequested();
        emailNotification.setEMailNotificationDetail(emailDetail);
        
        ShipmentSpecialServiceType[] specialServiceTypes = new ShipmentSpecialServiceType[]{ ShipmentSpecialServiceType.EMAIL_NOTIFICATION };
        emailNotification.setSpecialServiceTypes(specialServiceTypes);
        requestedShipment.setSpecialServicesRequested(emailNotification);
        
        Party shipper = new Party();
        Contact contactShipper = new Contact();
        contactShipper.setCompanyName(form.getFullName());
        contactShipper.setPhoneNumber(form.getNotificationPhone());
        shipper.setContact(contactShipper);
        Address addressShip = new Address();
        addressShip.setStreetLines(new String[]{form.getPickUp().getStreet1(), form.getPickUp().getStreet2()});
        addressShip.setCity(form.getPickUp().getCity());
        addressShip.setStateOrProvinceCode(form.getPickUp().getState());
        addressShip.setPostalCode(form.getPickUp().getZip());
        addressShip.setCountryCode(form.getCountry().getCountryCode());
        shipper.setAddress(addressShip);
        requestedShipment.setShipper(shipper); // Sender information
        //
        Party recipient = new Party(); // Recipient information
        Contact contactRecip = new Contact();
        contactRecip.setCompanyName(form.getDestinationLeo().getPhysical().getAddressTo());
        contactRecip.setPhoneNumber(form.getDestinationLeo().getLeoPhone());
        recipient.setContact(contactRecip);
        //
        Address addressRecip = new Address();
        addressRecip.setStreetLines(new String[]{form.getDestinationLeo().getPhysical().getStreet1(), form.getDestinationLeo().getPhysical().getStreet2()});
        addressRecip.setCity(form.getDestinationLeo().getPhysical().getCity());
        addressRecip.setStateOrProvinceCode( form.getDestinationLeo().getPhysical().getState() );
        if ( form.getDestinationLeo().getPhysical().getZip4() != null && form.getDestinationLeo().getPhysical().getZip4().length() > 0 ) {
            addressRecip.setPostalCode( form.getDestinationLeo().getPhysical().getZip() + "-" + form.getDestinationLeo().getPhysical().getZip4() );
        }
        else {
            addressRecip.setPostalCode( form.getDestinationLeo().getPhysical().getZip() );
        }
        addressRecip.setCountryCode("US");
        addressRecip.setResidential(new Boolean(false));
        recipient.setAddress(addressRecip);
        requestedShipment.setRecipient(recipient);

        //
        Party responsibleParty = new Party();
        responsibleParty.setAccountNumber(getPayorAccountNumber(form));// get PAYOR ACCOUNT # based on the selected country
        responsibleParty.setContact(contactShipper);
        Payor payor = new Payor();
        payor.setResponsibleParty(responsibleParty);

        Payment scp = new Payment(); // Payment information
        scp.setPaymentType(PaymentType.THIRD_PARTY);    //SENDER
        scp.setPayor(payor);
        requestedShipment.setShippingChargesPayment(scp);
        //

        CustomsClearanceDetail customsClearanceDetail = new CustomsClearanceDetail();
        customsClearanceDetail.setDutiesPayment( new Payment() );
        customsClearanceDetail.getDutiesPayment().setPaymentType(PaymentType.THIRD_PARTY);  //SENDER
        customsClearanceDetail.getDutiesPayment().setPayor(payor);
        customsClearanceDetail.setCustomsValue(new Money());
        customsClearanceDetail.getCustomsValue().setAmount(new java.math.BigDecimal(1.0));
        customsClearanceDetail.getCustomsValue().setCurrency("USD");
        //
        Commodity[] commodities = new Commodity[] { new Commodity() }; // Commodity details
        commodities[0].setNumberOfPieces(new NonNegativeInteger("1"));
        commodities[0].setDescription("U.S. Overseas Absentee Ballot");
        commodities[0].setCountryOfManufacture("US");
        commodities[0].setWeight(new Weight());
        commodities[0].getWeight().setValue(new BigDecimal(1.0));
        commodities[0].getWeight().setUnits(WeightUnits.LB);
        commodities[0].setQuantity(new NonNegativeInteger("1"));
        commodities[0].setQuantityUnits("EA");
        commodities[0].setUnitPrice(new Money());
        commodities[0].getUnitPrice().setAmount(new java.math.BigDecimal(1.0));
        commodities[0].getUnitPrice().setCurrency("USD");
        commodities[0].setCustomsValue(new Money());
        commodities[0].getCustomsValue().setAmount(new java.math.BigDecimal(1.0));
        commodities[0].getCustomsValue().setCurrency("USD");
        customsClearanceDetail.setCommodities(commodities);
        requestedShipment.setCustomsClearanceDetail(customsClearanceDetail);
        //
        LabelSpecification labelSpecification = new LabelSpecification(); // Label specification
        CustomerSpecifiedLabelDetail customerSpecifiedLabelDetail = new CustomerSpecifiedLabelDetail(); // Customer Detail Specification
        LabelMaskableDataType[] maskedData = {LabelMaskableDataType.SHIPPER_ACCOUNT_NUMBER}; // from Michael Traexler email, 8/4/2008
        customerSpecifiedLabelDetail.setMaskedData(maskedData);
        customerSpecifiedLabelDetail.setTermsAndConditionsLocalization(localization);
        labelSpecification.setCustomerSpecifiedDetail(customerSpecifiedLabelDetail);
        
        labelSpecification.setImageType(ShippingDocumentImageType.PNG);// Image types PDF, PNG, DPL, ...
        labelSpecification.setLabelFormatType(LabelFormatType.COMMON2D);
        labelSpecification.setLabelStockType(LabelStockType.value2); // STOCK_4X6.75_LEADING_DOC_TAB
        labelSpecification.setLabelPrintingOrientation(LabelPrintingOrientationType.TOP_EDGE_OF_TEXT_FIRST);
        requestedShipment.setLabelSpecification(labelSpecification);
        //
        RateRequestType[] rrt= new RateRequestType[] { RateRequestType.ACCOUNT }; // Rate types requested LIST, MULTIWEIGHT, ...
        requestedShipment.setRateRequestTypes(rrt);
        requestedShipment.setPackageCount(new NonNegativeInteger("1"));
        //

        RequestedPackageLineItem rp = new RequestedPackageLineItem();
        rp.setWeight(new Weight()); // Package weight information
        rp.getWeight().setValue(new BigDecimal(1.0));
        rp.getWeight().setUnits(WeightUnits.LB);
        rp.setCustomerReferences(new CustomerReference[]{new CustomerReference()}); // Reference details
        rp.getCustomerReferences()[0].setCustomerReferenceType(CustomerReferenceType.CUSTOMER_REFERENCE);
        rp.getCustomerReferences()[0].setValue("TC007_07_PT1_ST01_PK01_SNDUS_R");

        requestedShipment.setRequestedPackageLineItems(new RequestedPackageLineItem[] {rp});
        requestedShipment.setPackagingType( PackagingType.YOUR_PACKAGING );
        request.setRequestedShipment(requestedShipment);
        //
        return request;
    }

    private String getPayorAccountNumber(ExpressForm form) {
    	//return "510087682";
    	return form.getCountry().getAccountNumber().replaceAll(" ","");
    }

    private boolean isResponseOk(NotificationSeverityType notificationSeverityType) {
        if (notificationSeverityType == null) {
            return false;
        }
        if (notificationSeverityType.equals(NotificationSeverityType.WARNING) ||
                notificationSeverityType.equals(NotificationSeverityType.NOTE)    ||
                notificationSeverityType.equals(NotificationSeverityType.SUCCESS)) {
            return true;
        }
        return false;
    }


    private ClientDetail createClientDetail(Localization loc) {
        ClientDetail clientDetail = new ClientDetail();
/*
        String accountNumber = System.getProperty("accountNumber");
        String meterNumber = System.getProperty("meterNumber");

        //
        // See if the accountNumber and meterNumber properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        if (accountNumber == null) {
        accountNumber = "438370048"; //"510087682"; // Replace "XXX" with clients account number
        }
        if (meterNumber == null) {
        meterNumber = "1230587"; // Replace "XXX" with clients meter number
        }
*/
        clientDetail.setAccountNumber( clientAccountNumber.replaceAll(" ","") );
        clientDetail.setMeterNumber( clientMeterNumber.replaceAll(" ","") );
        clientDetail.setLocalization(loc);
        return clientDetail;
    }

    private WebAuthenticationDetail createWebAuthenticationDetail() {
        WebAuthenticationCredential wac = new WebAuthenticationCredential();
/*
        String key = System.getProperty("key");
        String password = System.getProperty("password");

        //
        // See if the key and password properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        if (key == null) {
        key = "pkHyiWZwjcwiTZyH"; // Replace "XXX" with clients key
        }
        if (password == null) {
        password = "y5yzCK24L9dvE9NO9mAgqfD4G"; // Replace "XXX" with clients password
        }
*/
        wac.setKey( authenticationKey );
        wac.setPassword( authenticationPassword );
        return new WebAuthenticationDetail(wac);
    }

    private String  saveLabelToFile(ShippingDocument shippingDocument, String trackingNumber) throws IOException {
        ShippingDocumentPart[] sdparts = shippingDocument.getParts();
        //for (int a=0; a < sdparts.length; a++) {
        int a = 0;  // part should be only one!
        ShippingDocumentPart sdpart = sdparts[a];
        String defaultStorageDir = this.getClass().getClassLoader().getResource("/").getPath() +"fedex/";
        String labelsDirName = (labelsStorageDir==null || labelsStorageDir.trim().length()==0) ? defaultStorageDir : labelsStorageDir; 
        File labelsDir = new File( labelsDirName );
        if( !labelsDir.exists() ) {
            labelsDir.mkdir();
        }
        String labelFileName =  labelsDirName + trackingNumber + "." + a + ".png";


        File labelFile = new File(labelFileName);
        //this.getClass().getClassLoader().get
        FileOutputStream fos = new FileOutputStream( labelFile );
        fos.write(sdpart.getImage());
        fos.close();
        //}
        return labelFileName;
    }

    private String writeServiceOutput(ProcessShipmentReply reply, FedexLabel fedexLabel ) throws IOException {
        CompletedShipmentDetail csd = reply.getCompletedShipmentDetail();
        CompletedPackageDetail cpd[] = csd.getCompletedPackageDetails();
        StringBuffer serviceOutput = new StringBuffer();
        serviceOutput.append("Package details\n");
        //for (int i=0; i < cpd.length; i++) { // Package details / Rating information for each package
        int i = 0;  // package should be only one!
        String trackingNumber = cpd[i].getTrackingIds(i).getTrackingNumber();
        serviceOutput.append("Tracking #: " + trackingNumber
                + " Form ID: " + cpd[i].getTrackingIds(i).getFormId());
        //serviceOutput.append("\nRate details\n");
        //
        /*PackageRateDetail[] prd = cpd[i].getPackageRating().getPackageRateDetails();
        for(int j=0; j < prd.length; j++)
        {
            serviceOutput.append("Billing weight: " + prd[j].getBillingWeight().getValue()
                    + " " + prd[j].getBillingWeight().getUnits());
            serviceOutput.append("Base charge: " + prd[j].getBaseCharge().getAmount()
                    + " " + prd[j].getBaseCharge().getCurrency());
            serviceOutput.append("Net charge: " + prd[j].getNetCharge().getAmount()
                    + " " + prd[j].getBaseCharge().getCurrency());
            if (null != prd[j].getSurcharges())
            {
                Surcharge[] s = prd[j].getSurcharges();
                for(int k=0; k < s.length; k++)
                {
                    serviceOutput.append(s[k].getSurchargeType() + " surcharge " +
                            s[k].getAmount().getAmount() + " " + s[k].getAmount().getCurrency());
                }
            }
            serviceOutput.append("Total surcharge: " + prd[j].getTotalSurcharges().getAmount() + " " +
                    prd[j].getTotalSurcharges().getCurrency());
            serviceOutput.append("\nRouting details\n");
            serviceOutput.append("URSA prefix: " + csd.getRoutingDetail().getUrsaPrefixCode()
                    + " suffix: " + csd.getRoutingDetail().getUrsaSuffixCode());
            serviceOutput.append("Service commitment: " + csd.getRoutingDetail().getCommitDay()
                    + " Airport ID: " + csd.getRoutingDetail().getAirportId());
            serviceOutput.append("Delivery day: " + csd.getRoutingDetail().getDeliveryDay());

        }*/
        //	Write label buffer to file
        ShippingDocument sd = cpd[i].getLabel();
        String filename = saveLabelToFile(sd, trackingNumber);
        fedexLabel.setFileName( filename );
        fedexLabel.setTrackingNumber( trackingNumber );
        //}
        return serviceOutput.toString();
    }

    private String createNotifications(Notification[] notifications) {
        StringBuffer serviceOutput = new StringBuffer();
        serviceOutput.append("Notifications:");
        if (notifications == null || notifications.length == 0) {
            serviceOutput.append("  No notifications returned");
        }
        for (int i=0; i < notifications.length; i++){
            Notification n = notifications[i];
            if (n.getSeverity().equals(NotificationSeverityType.WARNING) ||
                    n.getSeverity().equals(NotificationSeverityType.NOTE)    ||
                    n.getSeverity().equals(NotificationSeverityType.SUCCESS)) {
                continue;
            }
            //serviceOutput.append("  Notification no. " + i + ": ");
            if (n == null) {
                serviceOutput.append("null");
                continue;
            } else {
                serviceOutput.append("");
            }
            NotificationSeverityType nst = n.getSeverity();

            //serviceOutput.append("    Severity: " + (nst == null ? "null" : nst.getValue()));
            serviceOutput.append("    Code: ").append(n.getCode());
            serviceOutput.append("    Message: ").append(n.getMessage()).append("<br/>");
            //serviceOutput.append("    Source: " + n.getSource());
        }
        return serviceOutput.toString();
    }

	public String getWebserviceAddress() {
		return webserviceAddress;
	}

	public void setWebserviceAddress(String webserviceAddress) {
		this.webserviceAddress = webserviceAddress;
	}

}



