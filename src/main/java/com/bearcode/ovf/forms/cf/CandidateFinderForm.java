package com.bearcode.ovf.forms.cf;

import com.bearcode.ovf.model.common.Address;

public class CandidateFinderForm {
	private Address address;
	private String district;
    private String email;
    private String confirmEmail;
    private long country;
    private boolean addToList;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public boolean isAddToList() {
        return addToList;
    }

    public void setAddToList(boolean addToList) {
        this.addToList = addToList;
    }

	public CandidateFinderForm() {
		this.address = new Address();
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

    public long getCountry() {
        return country;
    }

    public void setCountry(long country) {
        this.country = country;
    }


}
