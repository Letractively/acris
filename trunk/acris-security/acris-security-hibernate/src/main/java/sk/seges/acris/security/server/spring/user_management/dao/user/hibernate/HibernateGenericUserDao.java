package sk.seges.acris.security.server.spring.user_management.dao.user.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataMetaModel;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public class HibernateGenericUserDao extends AbstractHibernateCRUD<UserData> implements IGenericUserDao<UserData> {

	public HibernateGenericUserDao() {
		super(HibernateGenericUser.class);
	}
	
	public <T extends UserData> HibernateGenericUserDao(Class<T> clazz) {
		super(clazz);
	}
	
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Override
	public UserData collectUserAuthorities(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		List<String> authorities = new ArrayList<String>();
		
		if (addMode && user.getUserAuthorities() != null) {
			for (String authority : user.getUserAuthorities()) {
				authorities.add(authority);
			}
		}
		
		authorities.addAll(extractAuthorities(authoritiesHolder.getUserPermissions()));

		user.setUserAuthorities(authorities);
		
		return user;
	}

	@Override
	@Transactional
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, false));
	}
	
	@Override
	@Transactional
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, addMode));
	}

	protected List<String> extractAuthorities(Collection<?> userPermissions) {
		if (!Hibernate.isInitialized(userPermissions)) {
			Hibernate.initialize(userPermissions);
		}

		List<String> userAuthorities = new ArrayList<String>();
		
		for (Object userPermission : userPermissions) {
			if (userPermission instanceof String) {
				userAuthorities.add((String)userPermission);
			} else if (userPermission instanceof UserPermission) {
				userAuthorities.add(((UserPermission)userPermission).name());
			}
		}
		
		return userAuthorities;
	}

	@Transactional
	@Override
	public UserData findByUsername(String username) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(UserDataMetaModel.USERNAME, username));
		return findUniqueResultByCriteria(criteria);
	}

	@Override
	public UserData getEntityInstance() {
		return new HibernateGenericUser();
	}
}