package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.model.common.*;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;


/**
 * Created by IntelliJ IDEA.
 * User: dhughes
 * Date: Mar 28, 2011
 * Time: 5:44:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PdfAnswersTest {


	/**
	 * Test PdfAnswers.clone() method
	 */
	@Test
	public void testClone() {

		/* Construct fixtures */

		FlowType type = FlowType.RAVA;
		String GENERIC_STRING_VAL1 = "test1";
		String GENERIC_STRING_VAL2 = "test2";
		String GENERIC_STRING_VAL3 = "test3";
		String ENTERED_STRING_VAl = "test4";
		String ENTERED_DATE_VAl = "01/02/2003";
		long QUESTION_FIELD_1_ID = 1;
		long QUESTION_FIELD_2_ID = 2;
		long QUESTION_FIELD_3_ID = 3;

		// question 1 with options
		QuestionField qf1 = new QuestionField();

		FieldTypeGenericDictionary genericDictionary = new FieldTypeGenericDictionary();
		genericDictionary.setFixedOptions(new ArrayList<FieldDictionaryItem>());
		GenericStringItem opt1 = (GenericStringItem)genericDictionary.createGenericItem();
		opt1.setId(10);
		opt1.setValue(GENERIC_STRING_VAL1);
		opt1.setForField(qf1);
		genericDictionary.getFixedOptions().add(opt1);
		GenericStringItem opt2 = (GenericStringItem)genericDictionary.createGenericItem();
		opt2.setId(11);
		opt2.setValue(GENERIC_STRING_VAL2);
		opt2.setForField(qf1);
		genericDictionary.getFixedOptions().add(opt2);
		GenericStringItem opt3 = (GenericStringItem)genericDictionary.createGenericItem();
		opt3.setId(12);
		opt3.setValue(GENERIC_STRING_VAL3);
		opt3.setForField(qf1);
		genericDictionary.getFixedOptions().add(opt3);

		qf1.setId(QUESTION_FIELD_1_ID);
		qf1.setType(genericDictionary);
		//qf1.setGenericOptions(genericDictionary.getFixedOptions());

		//answer 1
		PredefinedAnswer a1 = (PredefinedAnswer)genericDictionary.createAnswerOfType();
		a1.setField(qf1);
		a1.setValue("11");


		// question 2 entered value
		FieldTypeSingleValue singeValue = new FieldTypeSingleValue();

		QuestionField qf2 = new QuestionField();
		qf2.setId(QUESTION_FIELD_2_ID);
		qf2.setType(singeValue);

		//answer 2
		EnteredAnswer a2 = (EnteredAnswer)singeValue.createAnswerOfType();
		a2.setField(qf2);
		a2.setValue(ENTERED_STRING_VAl);

		// question 3 entered date value
		FieldTypeDate dateValue = new FieldTypeDate();

		QuestionField qf3 = new QuestionField();
		qf3.setId(QUESTION_FIELD_3_ID);
		qf3.setType(dateValue);

		//answer 2
		EnteredDateAnswer a3 = (EnteredDateAnswer)dateValue.createAnswerOfType();
		a3.setField(qf3);
		a3.setValue(ENTERED_DATE_VAl);

		// user
		OverseasUser user = new OverseasUser();
		user.setUsername("testUser");

		WizardResults orig = new WizardResults(type);
		orig.setId(1);
		orig.setUser(user);
		orig.putAnswer(a1);
		orig.putAnswer(a2);
		orig.putAnswer(a3);

		/* Do the clone */
		WizardResults clone = orig.createTemporary();

		/* Test */
		Collection<Answer> answers = clone.getAnswers();
		Assert.assertNotSame("PdfAnswers are the same instance",orig,clone);
		Assert.assertEquals("User Names are not the same ",orig.getUsername(),clone.getUsername());
		Assert.assertEquals("Unexpected answer count",orig.getAnswers().size(),answers.size());
		Assert.assertNotSame("Answer collections are the same instance",orig.getAnswers(),answers);

		/* look at individual answers*/ 
		Answer ans1 = null, ans2 = null, ans3 = null;
		for(Answer a: answers){
			if(a.getField().getId() == QUESTION_FIELD_1_ID ){
				ans1 = a;
			}
			if(a.getField().getId() == QUESTION_FIELD_2_ID ){
				ans2 = a;
			}
			if(a.getField().getId() == QUESTION_FIELD_3_ID ){
				ans3 = a;
			}
		}

		Assert.assertTrue("Answer 1 is not a PredefinedAnswer", a1 instanceof PredefinedAnswer);
		Assert.assertTrue("Answer 1 clone is not a PredefinedAnswer", ans1 instanceof PredefinedAnswer);
		Assert.assertNotSame("Answers are the same instance",a1, ans1);
		Assert.assertSame("QuestionField is not the same instance",a1.getField(), ans1.getField());
		Assert.assertEquals("Answer 1  has unexpected value", GENERIC_STRING_VAL2, ans1.getValue());

		Assert.assertTrue("Answer 2 is not a EnteredAnswer", a2 instanceof EnteredAnswer);
		Assert.assertTrue("Answer 2 clone is not a PredefinedAnswer", ans2 instanceof EnteredAnswer);
		Assert.assertNotSame("Answers are the same instance",a2, ans2);
		Assert.assertSame("QuestionField is not the same instance",a2.getField(), ans2.getField());
		Assert.assertEquals("Answer 2  has unexpected value", ENTERED_STRING_VAl, ans2.getValue());

		Assert.assertTrue("Answer 3 is not a EnteredDateAnswer", a3 instanceof EnteredDateAnswer);
		Assert.assertTrue("Answer 3 clone is not a EnteredDateAnswer", ans3 instanceof EnteredDateAnswer);
		Assert.assertNotSame("Answers are the same instance",a3, ans3);
		Assert.assertSame("QuestionField is not the same instance",a3.getField(), ans3.getField());
		Assert.assertEquals("Answer 3  has unexpected value", ENTERED_DATE_VAl, ans3.getValue());

		Date d = ((EnteredDateAnswer) ans3).getDate();
		Date expectedDate = null;
		try{
			expectedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(ENTERED_DATE_VAl);
		}
		catch(ParseException e){
			// ignore
		}
		Assert.assertEquals("Answer 3 has unexpected date", expectedDate,d);
	}

	/**
	 * Test that the setUser() method sets all fields
	 */
	@Test
	public void testPopulateFromUser() {

		// set up objects
		OverseasUser user = new OverseasUser();
		user.setUsername("testusername");
		user.getName().setFirstName("testfirstname");
		user.getName().setLastName("testlastname");
		user.getName().setMiddleName("testmiddlename");
		user.getName().setSuffix("testsuffix");
		user.setPhone("testphone");
		user.setVoterType(VoterType.OVERSEAS_VOTER);
		user.setVoterHistory(VoterHistory.OVERSEAS_AND_DOMESTIC_VOTER);
		user.setBallotPref("testballotpref");
		user.setRace("testrace");
		user.setEthnicity("testethnicity");
		user.setBirthYear(1234);
		user.setBirthMonth(12);
		user.setParty("testparty");

		user.setEodRegionId( "region22" );

		UserAddress addr = new UserAddress();
		addr.setStreet1("testvst1");
		addr.setStreet2("testvst2");
		addr.setCity("testvcity");
		addr.setState("testvstate");
		addr.setZip("testvzip");
		addr.setZip4("testvzip4");
		addr.setCountry("testvcountry");
		user.setVotingAddress(addr);

		UserAddress addr2 = new UserAddress();
		addr2.setStreet1("testcst1");
		addr2.setStreet2("testcst2");
		addr2.setCity("testccity");
		addr2.setState("testcstate");
		addr2.setZip("testczip");
		addr2.setZip4("testczip4");
		addr2.setCountry("testccountry");
		user.setCurrentAddress(addr2);

		UserAddress addr3 = new UserAddress();
		addr3.setStreet1("testfst1");
		addr3.setStreet2("testfst2");
		addr3.setCity("testfcity");
		addr3.setState("testfstate");
		addr3.setZip("testfzip");
		addr3.setZip4("testfzip4");
		addr3.setCountry("testfcountry");
		user.setForwardingAddress(addr3);

		// run the method
		WizardResults pdfAnswers = new WizardResults(FlowType.RAVA);
		pdfAnswers.populateFromUser(user);

		// Test
		Assert.assertEquals("Unexpected username", user.getUsername(), pdfAnswers.getUsername());
		Assert.assertEquals("Unexpected firstname", user.getName().getFirstName(), pdfAnswers.getName().getFirstName());
		Assert.assertEquals("Unexpected lastname", user.getName().getLastName(), pdfAnswers.getName().getLastName());
		Assert.assertEquals("Unexpected middlename", user.getName().getMiddleName(), pdfAnswers.getName().getMiddleName());
		Assert.assertEquals("Unexpected previousname", user.getPreviousName().getFirstName(), pdfAnswers.getPreviousName().getFirstName());
		Assert.assertEquals("Unexpected suffix", user.getName().getSuffix(), pdfAnswers.getName().getSuffix());
		Assert.assertEquals("Unexpected Phone", user.getPhone(), pdfAnswers.getPhone());

		Assert.assertEquals("Unexpected voterType", user.getVoterType().getName(), pdfAnswers.getVoterType());
		Assert.assertEquals("Unexpected voterHistory", user.getVoterHistory().getName(), pdfAnswers.getVoterHistory());
		Assert.assertEquals("Unexpected BallotPref", user.getBallotPref(), pdfAnswers.getBallotPref());
		Assert.assertEquals("Unexpected Voting Region ID", user.getEodRegionId(), pdfAnswers.getEodRegionId() );

		Assert.assertEquals("Unexpected BirthYear", user.getBirthYear(), pdfAnswers.getBirthYear());
		Assert.assertEquals("Unexpected BirthMonth", user.getBirthMonth(), pdfAnswers.getBirthMonth());
		Assert.assertEquals("Unexpected Race", user.getRace(), pdfAnswers.getRace());
		Assert.assertEquals("Unexpected Gender", user.getGender(), pdfAnswers.getGender());
		Assert.assertEquals("Unexpected Ethnicity", user.getEthnicity(), pdfAnswers.getEthnicity());
		Assert.assertEquals("Unexpected Party", user.getParty(), pdfAnswers.getParty());

		Assert.assertEquals("Unexpected street1", user.getVotingAddress().getStreet1(), pdfAnswers.getVotingAddress().getStreet1());
		Assert.assertEquals("Unexpected street2", user.getVotingAddress().getStreet2(), pdfAnswers.getVotingAddress().getStreet2());
		Assert.assertEquals("Unexpected city", user.getVotingAddress().getCity(), pdfAnswers.getVotingAddress().getCity());
		Assert.assertEquals("Unexpected state", user.getVotingAddress().getState(), pdfAnswers.getVotingAddress().getState());
		Assert.assertEquals("Unexpected zip", user.getVotingAddress().getZip(), pdfAnswers.getVotingAddress().getZip());
		Assert.assertEquals("Unexpected zip4", user.getVotingAddress().getZip4(), pdfAnswers.getVotingAddress().getZip4());
		Assert.assertEquals("Unexpected country", user.getVotingAddress().getCountry(), pdfAnswers.getVotingAddress().getCountry());

		Assert.assertEquals("Unexpected street1", user.getCurrentAddress().getStreet1(), pdfAnswers.getCurrentAddress().getStreet1());
		Assert.assertEquals("Unexpected street2", user.getCurrentAddress().getStreet2(), pdfAnswers.getCurrentAddress().getStreet2());
		Assert.assertEquals("Unexpected city", user.getCurrentAddress().getCity(), pdfAnswers.getCurrentAddress().getCity());
		Assert.assertEquals("Unexpected state", user.getCurrentAddress().getState(), pdfAnswers.getCurrentAddress().getState());
		Assert.assertEquals("Unexpected zip", user.getCurrentAddress().getZip(), pdfAnswers.getCurrentAddress().getZip());
		Assert.assertEquals("Unexpected zip4", user.getCurrentAddress().getZip4(), pdfAnswers.getCurrentAddress().getZip4());
		Assert.assertEquals("Unexpected country", user.getCurrentAddress().getCountry(), pdfAnswers.getCurrentAddress().getCountry());

		Assert.assertEquals("Unexpected street1", user.getForwardingAddress().getStreet1(), pdfAnswers.getForwardingAddress().getStreet1());
		Assert.assertEquals("Unexpected street2", user.getForwardingAddress().getStreet2(), pdfAnswers.getForwardingAddress().getStreet2());
		Assert.assertEquals("Unexpected city", user.getForwardingAddress().getCity(), pdfAnswers.getForwardingAddress().getCity());
		Assert.assertEquals("Unexpected state", user.getForwardingAddress().getState(), pdfAnswers.getForwardingAddress().getState());
		Assert.assertEquals("Unexpected zip", user.getForwardingAddress().getZip(), pdfAnswers.getForwardingAddress().getZip());
		Assert.assertEquals("Unexpected zip4", user.getForwardingAddress().getZip4(), pdfAnswers.getForwardingAddress().getZip4());
		Assert.assertEquals("Unexpected country", user.getForwardingAddress().getCountry(), pdfAnswers.getForwardingAddress().getCountry());
		
	}

}
