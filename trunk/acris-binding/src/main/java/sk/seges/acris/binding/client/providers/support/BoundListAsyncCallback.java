/**
 * 
 */
package sk.seges.acris.binding.client.providers.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.observablecollections.client.ObservableCollections;

import sk.seges.acris.binding.client.holder.BindingHolder;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.sesam.dao.ICallback;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Callback responsible for initializing a list of values bound to a list box.
 * List is fed using a loader.
 * 
 * @author eldzi
 * 
 * @param <T>
 *            Type of domain object contained in the list.
 */
public abstract class BoundListAsyncCallback<T extends Serializable> implements
		ICallback<PagedResult<List<T>>> {
	private final BindingHolder<T> bindingHolder;
	private final String sourceProperty;
	private final ListBox targetWidget;
	private final String itemTextProperty;
	private final String itemValueProperty;

	private final boolean wasEnabled;
	
	private List<T> listOfValues;

	public BoundListAsyncCallback(BindingHolder<T> bindingHolder, String sourceProperty,
			ListBox targetWidget, String itemTextProperty, String itemValueProperty) {
		super();
		this.bindingHolder = bindingHolder;
		this.sourceProperty = sourceProperty;
		this.targetWidget = targetWidget;
		this.itemTextProperty = itemTextProperty;
		this.itemValueProperty = itemValueProperty;
		
		wasEnabled = targetWidget.isEnabled();
	}

	@Override
	public void onFailure(Throwable arg0) {
		GWT.log("Exception in bound list callback", arg0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(PagedResult<List<T>> arg0) {
		if (listOfValues != null && listOfValues.size() > 0) {
			// listOfValues.clear();
		}
		List<T> listOfWrappers = null;
		if (listOfValues == null) {
			listOfWrappers = new ArrayList<T>();
		}
		BeanWrapper<T> wrapper;
		for (T result : arg0.getResult()) {
			wrapper = createWrapper();
			wrapper.setContent(result);
			if (listOfValues == null) {
				listOfWrappers.add((T) wrapper);
			} else {
				listOfValues.add((T) wrapper);
			}
		}
		if (listOfValues == null) {
			listOfValues = ObservableCollections.observableList(listOfWrappers);
		}

		bindingHolder.addListBoxBinding(listOfValues, targetWidget, itemTextProperty, itemValueProperty);
		bindingHolder.addSelectedItemBinding(sourceProperty, targetWidget);
		if(wasEnabled) {
			targetWidget.setEnabled(true);
		}
	}

	protected abstract BeanWrapper<T> createWrapper();
}