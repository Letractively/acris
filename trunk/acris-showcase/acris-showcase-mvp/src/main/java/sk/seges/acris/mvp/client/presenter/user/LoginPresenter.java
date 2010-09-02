package sk.seges.acris.mvp.client.presenter.user;

import sk.seges.acris.mvp.client.action.ActionManager;
import sk.seges.acris.mvp.client.action.DefaultAsyncCallback;
import sk.seges.acris.mvp.client.configuration.NameTokens;
import sk.seges.acris.mvp.client.event.LoginEvent;
import sk.seges.acris.mvp.client.event.LoginEvent.LoginEventHandler;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter.LoginDisplay;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter.LoginProxy;
import sk.seges.acris.mvp.shared.action.user.LoginAction;
import sk.seges.acris.mvp.shared.result.user.LoginResult;
import sk.seges.acris.security.shared.session.ClientSession;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.philbeaudoin.gwtp.mvp.client.EventBus;
import com.philbeaudoin.gwtp.mvp.client.PresenterImpl;
import com.philbeaudoin.gwtp.mvp.client.View;
import com.philbeaudoin.gwtp.mvp.client.annotations.NameToken;
import com.philbeaudoin.gwtp.mvp.client.annotations.ProxyStandard;
import com.philbeaudoin.gwtp.mvp.client.proxy.Place;
import com.philbeaudoin.gwtp.mvp.client.proxy.PlaceManager;
import com.philbeaudoin.gwtp.mvp.client.proxy.PlaceRequest;
import com.philbeaudoin.gwtp.mvp.client.proxy.Proxy;
import com.philbeaudoin.gwtp.mvp.client.proxy.RevealRootContentEvent;

public class LoginPresenter extends PresenterImpl<LoginDisplay, LoginProxy> implements LoginEventHandler {

	@ProxyStandard
	@NameToken(NameTokens.LOGIN_PAGE)
	public interface LoginProxy extends Proxy<LoginPresenter>, Place {}

	public interface LoginDisplay extends View {
		HandlerRegistration addLoginHandler(LoginEventHandler handler);
	}

	private final ActionManager actionManager;
	private final ClientSession clientSession;
	private final PlaceManager placeManager;
	
	@Inject
	public LoginPresenter(ClientSession clientSession, ActionManager actionManager, PlaceManager placeManager, EventBus eventBus, LoginDisplay view, LoginProxy proxy) {
		super(eventBus, view, proxy);
		this.actionManager = actionManager;
		this.clientSession = clientSession;
		this.placeManager = placeManager;
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().addLoginHandler(this));
	}
	
	protected void doLogin() {
		
	}
	
	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(eventBus, this);
	}

	@Override
	public void onLogin(LoginEvent event) {
		actionManager.execute(new LoginAction(event.getUsername(), event.getPassword()), new DefaultAsyncCallback<LoginResult>() {

			@Override
			public void onSuccess(LoginResult result) {
				clientSession.setUser(result.getClientSession().getUser());
				clientSession.setSessionId(result.getClientSession().getSessionId());
				LoginPresenter.this.placeManager.revealPlace(new PlaceRequest(NameTokens.USER_MAINTENANCE_PAGE));
			}
			
		});
	}
}