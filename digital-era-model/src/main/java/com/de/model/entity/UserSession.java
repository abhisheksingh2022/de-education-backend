package com.de.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "USER_SESSION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserSession implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TOKEN_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer token_id;
	
	@Column(name="TOKEN")
	private String token;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRY_DATETIME", nullable = false)
	private Date expiryDateTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false)
	private UserProfile userId;

	public Integer getToken_id() {
		return token_id;
	}

	public void setToken_id(Integer token_id) {
		this.token_id = token_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiryDateTime() {
		return expiryDateTime;
	}

	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}

	public UserProfile getUserId() {
		return userId;
	}

	public void setUserId(UserProfile userId) {
		this.userId = userId;
	}

}
