package sk.seges.acris.site.shared.service;

import sk.seges.acris.domain.shared.domain.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IWebSettingsServiceAsync {

	void getWebSettings(String webId, AsyncCallback<WebSettingsData> callback);

	void saveWebSettings(WebSettingsData webSettingsData, AsyncCallback<Void> callback);
	
	void saveFTPWebSettings(String webId, FTPWebSettingsData ftpWebSettings, AsyncCallback<Void> callback);
	
	void getFTPWebSettings(String webId, AsyncCallback<FTPWebSettingsData> callback);
}