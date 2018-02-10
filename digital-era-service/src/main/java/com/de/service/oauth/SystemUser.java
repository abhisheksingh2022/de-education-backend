package com.de.service.oauth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.de.model.entity.UserProfile;

public class SystemUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	private UserProfile userProfile;
	private boolean hasExpired;

	public SystemUser() {
	}

	public SystemUser(UserProfile userProfile, boolean hasExpired) {
		this.userProfile = userProfile;
		this.hasExpired = hasExpired;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

		grantedAuthorities.add(new GrantedAuthority() {

			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return userProfile.getUserRole().getUserRole();
			}
		});

		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return "dummyPassword";
	}

	@Override
	public String getUsername() {
		if (null != userProfile.getUserMail()) {
			return userProfile.getUserMail();
		} else {
			return String.format("Anonymous[%d]", userProfile.getId());
		}
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !hasExpired;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object otherObject) {
		if (null == otherObject) {
			return false;
		}

		if (!otherObject.getClass().isAssignableFrom(UserProfile.class)) {
			return false;
		}

		return getUsername().equals(((UserProfile) otherObject).getUserMail());
	}

	@Override
	public int hashCode() {
		return userProfile.getId().hashCode();
	}

	public UserProfile getUser() {
		return userProfile;
	}

}
