package sk.seges.acris.security.server.acl.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.server.acl.dao.IAclObjectIdentityDao;
import sk.seges.acris.security.server.acl.domain.JpaAclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentityBeanWrapper;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

public class HibernateAclSecuredObjectIdentityDao extends AbstractHibernateCRUD<JpaAclSecuredObjectIdentity> implements
		IAclObjectIdentityDao<JpaAclSecuredObjectIdentity> {

	public HibernateAclSecuredObjectIdentityDao() {
		super(JpaAclSecuredObjectIdentity.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	public JpaAclSecuredObjectIdentity findByObjectId(long objectIdClass, long objectIdIdentity) {

		DetachedCriteria criteria = createCriteria();

		criteria.add(Restrictions.eq(AclSecuredObjectIdentityBeanWrapper.OBJECT_ID_CLASS.THIS + ".id", objectIdClass));
		criteria.add(Restrictions.eq(AclSecuredObjectIdentityBeanWrapper.OBJECT_ID_IDENTITY, objectIdIdentity));

		List<JpaAclSecuredObjectIdentity> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));

		if (entries.size() == 0) {
			return null;
		}

		if (entries.size() == 1) {
			return entries.get(0);
		}

		throw new IllegalArgumentException("More than one unique records was found in database");
	}

	@Override
	public AclSecuredObjectIdentity findById(long id) {
		return entityManager.find(clazz, id);
	}

	@Override
	public AclSecuredObjectIdentity createDefaultEntity() {
		return new JpaAclSecuredObjectIdentity();
	}
}
