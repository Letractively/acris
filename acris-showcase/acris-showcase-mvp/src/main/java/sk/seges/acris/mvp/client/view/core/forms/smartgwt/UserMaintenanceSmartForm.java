package sk.seges.acris.mvp.client.view.core.forms.smartgwt;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import sk.seges.acris.binding.client.annotations.BindingField;
import sk.seges.acris.binding.client.annotations.BindingFieldsBase;
import sk.seges.acris.binding.client.annotations.ValidationStrategy;
import sk.seges.acris.mvp.client.i18n.UserMessages;
import sk.seges.acris.mvp.client.view.core.forms.ExampleHighlighter;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.acris.security.rpc.user_management.domain.GenericUserBeanWrapper;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.fields.TextItem;

@BindingFieldsBase(updateStrategy = UpdateStrategy.READ_WRITE, validationStrategy = ValidationStrategy.ON_SUBMIT, validationHighlighter = ExampleHighlighter.class)
public class UserMaintenanceSmartForm extends SmartGWTLayoutForm<GenericUser> {

	@BindingField(GenericUserBeanWrapper.USERNAME)
	private final TextItem userName = GWT.create(TextItem.class);

	@BindingField(GenericUserBeanWrapper.PASSWORD)
	private final TextItem password = GWT.create(TextItem.class);

	private UserMessages userMessages = GWT.create(UserMessages.class);
	
	public UserMaintenanceSmartForm() {
		super(2);
	}

	@Override
	protected void prepareFields() {
		addFormRow(true, withLabel(userMessages.userName(), userName));
		addFormRow(true, withLabel(userMessages.password(), password));
	}
}