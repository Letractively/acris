package sk.seges.acris.security.server.user_management.dao.permission.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaSecurityRole;
import sk.seges.acris.security.server.user_management.dao.permission.ISecurityRoleDao;
import sk.seges.acris.security.server.utils.CastUtils;
import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

public class HibernateSecurityRoleDao extends AbstractHibernateCRUD<JpaSecurityRole> implements ISecurityRoleDao<JpaSecurityRole> {

	@Override
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}

	protected HibernateSecurityRoleDao() {
		super(JpaSecurityRole.class);
	}

	private static final String FIND_USER_AUTHORITIES_SQL = "select selectedauthorities_element from role_selectedauthorities where role_id=:roleId";

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findSelectedPermissions(Integer roleId) {
		Session hibernateSession = (Session) entityManager.getDelegate();
		SQLQuery query = hibernateSession.createSQLQuery(FIND_USER_AUTHORITIES_SQL);
		query.setParameter("roleId", roleId);
		return query.list();
	}

	@Override
	public RoleData findByName(String name) {
		DetachedCriteria criteria = createCriteria().add(Restrictions.eq(JpaSecurityRole.A_NAME, name));
		return findUniqueResultByCriteria(criteria);
	}

	@Override
	public RoleData findUnique(Page page) {
		List<RoleData> result = CastUtils.castList(findByCriteria(createCriteria(), page), RoleData.class);
		
		if (result == null || result.size() == 0) {
			throw new RuntimeException("Unable to obtain unique result. No results found!");
		}
		
		if (result.size() > 1) {
			throw new RuntimeException("Unable to obtain unique result. More than 1 result found!");
		}
		
		return result.get(0);
	}
}