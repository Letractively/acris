/**
 * 
 */
package sk.seges.acris.generator.rpc.service;

import java.util.List;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author fat
 */
public interface IGeneratorServiceAsync {
	/**
	 * Offline content generator service
	 */
	void saveContent(GeneratorToken token,
			String contentText, AsyncCallback<Boolean> callback);

	void getLastProcessingToken(AsyncCallback<GeneratorToken> callback);

	void getOfflineContentHtml(String headerFileName, String content, GeneratorToken token, String currentServerURL, AsyncCallback<String> callback);

	/**
	 * File provider services
	 */
	void readHtmlBodyFromFile(String filename, AsyncCallback<String> callback);

	void writeTextToFile(String content, GeneratorToken token, AsyncCallback<Void> callback);

	/**
	 * Content provider services
	 */
	void getAvailableNiceurls(String language, String webId, AsyncCallback<List<String>> callback);
}