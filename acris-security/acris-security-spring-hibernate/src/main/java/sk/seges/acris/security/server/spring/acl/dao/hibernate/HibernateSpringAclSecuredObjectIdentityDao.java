package sk.seges.acris.security.server.spring.acl.dao.hibernate;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.dao.hibernate.HibernateAclSecuredObjectIdentityDao;
import sk.seges.acris.security.server.acl.domain.JpaAclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;

@Component
public class HibernateSpringAclSecuredObjectIdentityDao extends HibernateAclSecuredObjectIdentityDao {

	@Override
	@Transactional
    public JpaAclSecuredObjectIdentity findByObjectId(long objectIdClass, long objectIdIdentity) {
		return super.findByObjectId(objectIdClass, objectIdIdentity);
    }

	@Override
	@Transactional
	public AclSecuredObjectIdentity findById(long id) {
		return super.findById(id);
	}
}
