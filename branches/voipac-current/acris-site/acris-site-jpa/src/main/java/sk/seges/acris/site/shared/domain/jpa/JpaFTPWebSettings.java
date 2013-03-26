package sk.seges.acris.site.shared.domain.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import sk.seges.acris.domain.shared.domain.ftp.server.model.base.FTPWebSettingsBase;

@Entity
@Table(name = "web_settings_ftp")
public class JpaFTPWebSettings extends FTPWebSettingsBase {
	private static final long serialVersionUID = 3183120505024621796L;

	public JpaFTPWebSettings() {}
	
	@Id
	@GeneratedValue/*(strategy = GenerationType.IDENTITY)*/
	@Override
	public Long getId() {
		return super.getId();
	}

	@Override
	public String getFtpHost() {
		return super.getFtpHost();
	}
	
	@Override
	public String getFtpUserName() {
		return super.getFtpUserName();
	}
	
	@Override
	public String getFtpUserPwd() {
		return super.getFtpUserPwd();
	}
	
	@Override
	public String getFtpRootDir() {
		return super.getFtpRootDir();
	}
}
