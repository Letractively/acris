package sk.seges.acris.security.client.session;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.security.rpc.session.ClientSession;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;

/**
 * {@link RemoteServiceProxy} extension for send current session id in request
 * payload. SessionId is obtained from {@link ClientSession} object which has to
 * be correctly initialized and assigned with the service.
 * 
 * @author fat
 */
public abstract class SessionAwareRemoteServiceProxy extends RemoteServiceProxy
		implements SessionServiceDefTarget {

	/**
	 * ClientSession for holding surrent sessionId. This sessionId will be part
	 * of request's payload.
	 */
	private ClientSession clientSession;

	private static long uniqueRequestId = 0;

	protected SessionAwareRemoteServiceProxy(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			Serializer serializer) {
		super(moduleBaseURL, remoteServiceRelativePath,
				serializationPolicyName, serializer);
	}

	protected ClientSession getSession() {
		return clientSession;
	}

	public void setSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}

	@Override
	protected <T> Request doInvoke(ResponseReader responseReader,
			String methodName, int invocationCount, String requestData,
			AsyncCallback<T> callback) {

		long lastUniqueRequestID = uniqueRequestId;
		uniqueRequestId++;

		if (callback instanceof TrackingAsyncCallback<?>) {
			((TrackingAsyncCallback<T>) callback)
					.setRequestId(lastUniqueRequestID);
		}

		String sessionID = "";

		if (getSession() != null) {
			sessionID = getSession().getSessionId();
		}

		if (sessionID == null) {
			sessionID = "";
		}

		if (sessionID.length() > 0) {
			final char sep = '\uffff';
			return super.doInvoke(responseReader, methodName, invocationCount,
					String.valueOf(sep) + sessionID + String.valueOf(sep)
							+ requestData, callback);
		}

		return super.doInvoke(responseReader, methodName, invocationCount,
				requestData, callback);
	}
}