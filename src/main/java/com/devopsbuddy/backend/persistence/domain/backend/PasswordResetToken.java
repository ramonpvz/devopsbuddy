package com.devopsbuddy.backend.persistence.domain.backend;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devopsbuddy.backend.persistence.converters.LocalDateTimeAttributeConverter;

@Entity
public class PasswordResetToken implements Serializable {

	private static final Logger LOG = LoggerFactory.getLogger(PasswordResetToken.class);
	
	/** The Serial Version UID for Serializable classes. **/
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_TOKEN_LENGTH_IN_MINUES = 120;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true)
	private String token;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;

	@Column(name = "expiry_date")
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime expiryDate;

	/**
	 * Default constructor
	 */
	public PasswordResetToken() {
		
	}
	
	/**
	 * Full constructor
	 * @param token The user token. It must not be null.
	 * @param user The user for which the token should be created It must not be null.
	 * @param creationDateTime The date time when this request was created. It must not be null.
	 * @param expirationInMinutes The length, in minutes, for which the token will be valid. If zero, it will be 
	 * assigned a default value of 120 (2 hours)
	 * 
	 * @Throws IllegalArgumentException If the token, user or creation date time are null
	 * 
	 */
	public PasswordResetToken(String token, User user, LocalDateTime creationDateTime, int expirationInMinutes) {
		if ((null == token) || (null == user) || (null == creationDateTime)) {
			throw new IllegalArgumentException("token, user and creation date time can't be null");
		}
		if (expirationInMinutes == 0) {
			LOG.warn("The token expiration length in minutes is zero. Assigning the default value {} ", DEFAULT_TOKEN_LENGTH_IN_MINUES);
			expirationInMinutes = DEFAULT_TOKEN_LENGTH_IN_MINUES;
		}
		this.token = token;
		this.user = user;
		expiryDate = creationDateTime.plusMinutes(expirationInMinutes);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PasswordResetToken other = (PasswordResetToken) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
