package sk.seges.acris.mvp.server.actionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sk.seges.acris.mvp.server.actionhandler.core.DefaultActionHandler;
import sk.seges.acris.mvp.server.service.IUserMaintenanceService;
import sk.seges.acris.mvp.shared.action.FetchUsersAction;
import sk.seges.acris.mvp.shared.result.FetchUsersResult;
import sk.seges.sesam.dao.Page;

import com.philbeaudoin.gwtp.dispatch.server.ExecutionContext;
import com.philbeaudoin.gwtp.dispatch.shared.ActionException;

@Component
public class FetchUsersActionHandler extends DefaultActionHandler<FetchUsersAction, FetchUsersResult> {

	@Autowired
	private IUserMaintenanceService userMaintenanceService;

	@Override
	public Class<FetchUsersAction> getActionType() {
		return FetchUsersAction.class;
	}

	@Override
	public FetchUsersResult execute(FetchUsersAction action, ExecutionContext context) throws ActionException {
		return new FetchUsersResult(userMaintenanceService.findAll(Page.ALL_RESULTS_PAGE));
	}

	@Override
	public void undo(FetchUsersAction action, FetchUsersResult result, ExecutionContext context) throws ActionException {
	}
}