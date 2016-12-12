package eu.ewall.platform.commons.datamodel.profile;

import java.util.Date;

import eu.ewall.platform.commons.datamodel.location.Location;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * vCard is a file format standard for electronic business cards. vCard Format
 * Specification can be found at this link: @link <a href="https://tools.ietf.org/html/rfc6350">https://tools.ietf.org/html/rfc6350</a>
 * It gives VCard-based details and information about the User.
 * 
 * @author eandgrg
 */
public class VCardSubProfile extends UserSubProfile {

	/** The address. */
	private String address;

	/** The city. */
	private String city;

	/** The state. */
	private String state;

	/** The country. */
	private String country;

	/** The timezone id. */
	private String timezoneid;

	/** The postcode. */
	private String postcode;

	/** The anniversary. */
	private Date anniversary;

	/** The birthday. */
	private Date birthday;

	/** The age. */
	private int age;

	/** The birthplace. */
	private String birthplace;

	/** The email. */
	private String email;

	/** The gender. */
	private VCardGenderType gender;

	/** The mobile. */
	private String mobile;

	/** The nickname. */
	private String nickname;

	/** The telephone. */
	private String telephone;

	/** The location. */
	private Location location;

	/**
	 * The Constructor.
	 */
	public VCardSubProfile() {

	}

	/**
	 * Instantiates a new v card sub profile.
	 *
	 * @param nickname
	 *            the nickname
	 * @param address
	 *            the address
	 * @param city
	 *            the city
	 * @param state
	 *            the state
	 * @param country
	 *            the country
	 * @param timezoneid
	 *            the timezone id
	 * @param postcode
	 *            the postcode
	 * @param birthday
	 *            the birthday
	 * @param age
	 *            the age
	 * @param anniversary
	 *            the anniversary
	 * @param birthplace
	 *            the birthplace
	 * @param email
	 *            the email
	 * @param gender
	 *            the gender
	 * @param mobile
	 *            the mobile
	 * @param telephone
	 *            the telephone
	 * @param location
	 *            the location
	 */
	public VCardSubProfile(String nickname, String address, String city,
			String state, String country, String timezoneid, String postcode,
			Date birthday, int age, Date anniversary, String birthplace,
			String email, VCardGenderType gender, String mobile,
			String telephone, Location location) {
		this.nickname = nickname;
		this.address = address;
		this.city = city;
		this.state = state;
		this.country = country;
		this.timezoneid = timezoneid;
		this.postcode = postcode;
		this.birthday = birthday;
		this.age = age;
		this.anniversary = anniversary;
		this.birthplace = birthplace;
		this.email = email;
		this.gender = gender;
		this.mobile = mobile;
		this.telephone = telephone;
		this.location = location;
	}
	
	/**
	 * Instantiates a new v card sub profile.
	 *
	 * @param address the address
	 * @param city the city
	 * @param state the state
	 * @param country the country
	 * @param timezoneid the timezoneid
	 * @param postcode the postcode
	 * @param birthday the birthday
	 * @param birthplace the birthplace
	 * @param gender the gender
	 */
	public VCardSubProfile(String address, String city, String state, String country, String timezoneid,
		String postcode, Date birthday, String birthplace, VCardGenderType gender) {
	    super();
	    this.address = address;
	    this.city = city;
	    this.state = state;
	    this.country = country;
	    this.timezoneid = timezoneid;
	    this.postcode = postcode;
	    this.birthday = birthday;
	    this.birthplace = birthplace;
	    this.gender = gender;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address
	 *            the address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets the country.
	 *
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Gets the postcode.
	 *
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * Sets the postcode.
	 *
	 * @param postcode
	 *            the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/**
	 * Gets the anniversary.
	 *
	 * @return the anniversary
	 */
	public Date getAnniversary() {
		return anniversary;
	}

	/**
	 * Sets the anniversary.
	 *
	 * @param anniversary
	 *            the anniversary
	 */
	public void setAnniversary(Date anniversary) {
		this.anniversary = anniversary;
	}

	/**
	 * Gets the birthday.
	 *
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * Sets the birthday.
	 *
	 * @param birthday
	 *            the birthday
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * Gets the age.
	 *
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Sets the age.
	 *
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Gets the birthplace.
	 *
	 * @return the birthplace
	 */
	public String getBirthplace() {
		return birthplace;
	}

	/**
	 * Sets the birthplace.
	 *
	 * @param birthplace
	 *            the birthplace
	 */
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email
	 *            the email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public VCardGenderType getGender() {
		return gender;
	}

	/**
	 * Sets the gender.
	 *
	 * @param gender
	 *            the gender
	 */
	public void setGender(VCardGenderType gender) {
		this.gender = gender;
	}

	/**
	 * Gets the mobile.
	 *
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * Sets the mobile.
	 *
	 * @param mobile
	 *            the mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * Gets the nickname.
	 *
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Sets the nickname.
	 *
	 * @param nickname
	 *            the nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Gets the telephone.
	 *
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * Sets the telephone.
	 *
	 * @param telephone
	 *            the telephone
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("******VCardSubProfile start******\n");
		buffer.append("address: ");
		buffer.append(address);
		buffer.append("\n");
		buffer.append("city: ");
		buffer.append(city);
		buffer.append("\n");
		buffer.append("state: ");
		buffer.append(state);
		buffer.append("\n");
		buffer.append("country: ");
		buffer.append(country);
		buffer.append("\n");
		buffer.append("timezoneid: ");
		buffer.append(timezoneid);
		buffer.append("\n");
		buffer.append("postcode: ");
		buffer.append(postcode);
		buffer.append("\n");
		buffer.append("anniversary: ");
		buffer.append(anniversary);
		buffer.append("\n");
		buffer.append("birthday: ");
		buffer.append(birthday);
		buffer.append("\n");
		buffer.append("age: ");
		buffer.append(age);
		buffer.append("\n");
		buffer.append("birthplace: ");
		buffer.append(birthplace);
		buffer.append("\n");
		buffer.append("email: ");
		buffer.append(email);
		buffer.append("\n");
		buffer.append("gender: ");
		buffer.append(gender);
		buffer.append("\n");
		buffer.append("mobile: ");
		buffer.append(mobile);
		buffer.append("\n");
		buffer.append("nickname: ");
		buffer.append(nickname);
		buffer.append("\n");
		buffer.append("telephone: ");
		buffer.append(telephone);
		buffer.append("\n");
		buffer.append("location:\n");
		if (location != null)
			buffer.append(location.toString());
		else
			buffer.append("empty");
		buffer.append("\n");
		buffer.append("******VCardSubProfile end******\n");

		return buffer.toString();
	}

	/**
	 * Gets the timezone id.
	 *
	 * @return the timezone id
	 */
	public String getTimezoneid() {
		return timezoneid;
	}

	/**
	 * Sets the timezone id.
	 *
	 * @param timezoneid
	 *            the timezone id to set
	 */
	public void setTimezoneid(String timezoneid) {
		this.timezoneid = timezoneid;
	}

}