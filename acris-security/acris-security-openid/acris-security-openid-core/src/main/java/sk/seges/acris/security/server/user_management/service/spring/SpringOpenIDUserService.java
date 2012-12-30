package sk.seges.acris.security.server.user_management.service.spring;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.server.model.data.OpenIDUserData;
import sk.seges.acris.security.server.user_management.service.OpenIDUserService;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;
import sk.seges.acris.security.user_management.server.model.data.UserData;

public class SpringOpenIDUserService extends OpenIDUserService {

	public SpringOpenIDUserService() {
		super(null, null);
	}

	public SpringOpenIDUserService(IGenericUserDao<UserData> genericUserDao,
			IOpenIDUserDao<OpenIDUserData> openIDUserDao) {
		super(genericUserDao, openIDUserDao);
	}

	@Transactional
	@Override
	public GenericUserDTO getUserByOpenIDIdentifier(String identifier) {
		return super.getUserByOpenIDIdentifier(identifier);
	}

	@Transactional
	@Override
	public List<OpenIDProvider> findProvidersByUserName(String userName) {
		return super.findProvidersByUserName(userName);
	}

	@Transactional
	@Override
	public void saveUserByIdentifiers(String userName, Map<String, Object> identifiers) {
		super.saveUserByIdentifiers(userName, identifiers);
	}
}