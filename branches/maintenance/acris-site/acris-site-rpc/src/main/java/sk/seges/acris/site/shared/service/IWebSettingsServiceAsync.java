package sk.seges.acris.site.shared.service;

import sk.seges.acris.shared.model.dto.FTPWebSettingsDTO;
import sk.seges.acris.shared.model.dto.WebSettingsDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IWebSettingsServiceAsync {

	void getWebSettings(String webId, AsyncCallback<WebSettingsDTO> callback);

	void saveWebSettings(WebSettingsDTO webSettingsData, AsyncCallback<Void> callback);
	
	void saveFTPWebSettings(String webId, FTPWebSettingsDTO ftpWebSettings, AsyncCallback<Void> callback);
	
	void getFTPWebSettings(String webId, AsyncCallback<FTPWebSettingsDTO> callback);
}