package sk.seges.acris.mvp.shared.result;

import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.philbeaudoin.gwtp.dispatch.shared.Result;

public class AddUserResult implements Result {

	private static final long serialVersionUID = 1L;

	/**
	 * For serialization only.
	 */
	@SuppressWarnings("unused")
	private AddUserResult() {
	}

	private UserData user;

	public AddUserResult(UserData user) {
		this.user = user;
	}

	public UserData getUser() {
		return user;
	}
}