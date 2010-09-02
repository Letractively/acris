package sk.seges.acris.security.server.spring.user_management.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class SpringUserService implements UserDetailsService {

	private static final long serialVersionUID = 8634166450216274971L;

	protected IGenericUserDao<UserData> genericUserDao;

	public SpringUserService(IGenericUserDao<UserData> genericUserDao) {
		this.genericUserDao = genericUserDao;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		return (UserDetails) genericUserDao.findByUsername(userName);
	}
}