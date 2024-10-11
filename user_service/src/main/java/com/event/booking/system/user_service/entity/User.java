package com.event.booking.system.user_service.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "iam_user")
@Entity
@SQLRestriction(value = "is_deleted = 0")
public class User {

	/** The pk user id. */
	@Column(name = "pk_user_id")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long pkUserId;

	@Column(name = "title")
	private String title;

	/** The first name. */
	@Column(name = "first_name")
	private String firstName;

	/** The last name. */
	@Column(name = "last_name")
	private String lastName;

	/** The user name. */
	@Column(name = "user_name")
	private String userName;

	/** The email. */
	@Column(name = "email")
	private String email;

	/** The phone number. */
	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "credit")
	private BigDecimal credit;

	/** The is deleted. */
	@Column(name = "is_deleted")
	@Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
	private boolean isDeleted = false;

	public Long getPkUserId() {
		return pkUserId;
	}

	public void setPkUserId(Long pkUserId) {
		this.pkUserId = pkUserId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

}