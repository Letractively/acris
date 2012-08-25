package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import sk.seges.corpis.server.domain.invoice.base.UnitBase;
import sk.seges.corpis.shared.domain.invoice.api.UnitType;

/**
 * @author eldzi
 */
@MappedSuperclass
public abstract class JpaUnitBase extends UnitBase<Integer> {
	private static final long serialVersionUID = -6693153258172598222L;
	
	private static final short LABEL_KEY_LENGTH = 250;

	@Column(length = LABEL_KEY_LENGTH)
	public String getLabelKey() {
		return super.getLabelKey();
	}

	@Column
	public UnitType getType() {
		return super.getType();
	}
}