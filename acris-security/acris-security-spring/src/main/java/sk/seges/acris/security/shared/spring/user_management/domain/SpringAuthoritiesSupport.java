package sk.seges.acris.security.shared.spring.user_management.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import sk.seges.acris.security.shared.spring.authority.GrantedAuthorityImpl;
import sk.seges.acris.security.user_management.server.model.data.UserData;

public class SpringAuthoritiesSupport implements Serializable {

	private static final long serialVersionUID = -274900627165199081L;
	private UserData user;

	public SpringAuthoritiesSupport() {
	}

	public void setUser(UserData user) {
		this.user = user;
	}

	public UserData getUser() {
		return user;
	}

	public SpringAuthoritiesSupport(UserData user) {
		this.user = user;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (user.getUserAuthorities() == null) {
			return new ArrayList<GrantedAuthority>();
		}

		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

		for (String authority : user.getUserAuthorities()) {
			GrantedAuthorityImpl lazyGrantedAuthority = new GrantedAuthorityImpl();
			lazyGrantedAuthority.setAuthority(authority);
			grantedAuthorities.add(lazyGrantedAuthority);
		}

		return grantedAuthorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		List<String> result = new ArrayList<String>();

		if (authorities != null) {
			for (GrantedAuthority authority : authorities) {
				result.add(authority.getAuthority());
			}
		}

		user.setUserAuthorities(result);
	}
}