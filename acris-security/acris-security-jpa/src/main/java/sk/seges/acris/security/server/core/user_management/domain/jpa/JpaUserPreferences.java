package sk.seges.acris.security.server.core.user_management.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import sk.seges.acris.security.shared.user_management.domain.dto.UserPreferencesDTO;

@Table
@Entity
public class JpaUserPreferences extends UserPreferencesDTO {

	private static final long serialVersionUID = -2671953566571056404L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}

	@Column
	public String getLocale() {
		return super.getLocale();
	}
}