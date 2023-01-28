package com.bearcode.ovf.model.common;

public enum VoterClassificationType {
  CITIZEN_OVERSEAS_INDEFINITELY("I am a U.S. citizen living outside the country, and my return is uncertain"),
  CITIZEN_OVERSEAS_TEMPORARILY("I am a U.S. citizen living outside the country, and I intend to return"),
  CITIZEN_NEVER_RESIDED("I am a U.S. citizen living outside the country, and I have never lived in the United States"),
  UNIFORMED_SERVICE_MEMBER("I am on active duty in the Uniformed Services or Merchant Marine"),
  SPOUSE_OR_DEPENDENT("I am an eligible spouse or dependent");
  // todo: remove completely when military types will be collapsed into one type
  //NATIONAL_GUARD_MEMBER("I am an activated National Guard member on State orders");

	private String value;

	VoterClassificationType(String stringVal){
		this.value = stringVal;
	}

	public String getValue(){
		return value;
	}

	public String getName(){
		return name();
	}
	
}
