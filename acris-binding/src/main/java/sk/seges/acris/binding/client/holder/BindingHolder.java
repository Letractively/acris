package sk.seges.acris.binding.client.holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwt.beansbinding.core.client.AutoBinding;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.BindingGroup;
import org.gwt.beansbinding.core.client.Bindings;
import org.gwt.beansbinding.core.client.Converter;
import org.gwt.beansbinding.core.client.Validator;
import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;
import org.gwt.beansbinding.ui.client.GWTBindings;
import org.gwt.beansbinding.ui.client.ListBoxBinding;

import sk.seges.acris.binding.client.annotations.BindingFieldInfo;
import sk.seges.acris.binding.client.wrappers.BeanProxyWrapper;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class BindingHolder<T> implements IBindingHolder<T> {
	
	protected List<BindingGroup> asyncbindingGroups = new ArrayList<BindingGroup>();
	protected BindingGroup rootBinding = new BindingGroup();
	
    private UpdateStrategy updateStrategy;
    private BeanWrapper<T> beanWrapper;
    
    private List<Binding> selectionGroup = new ArrayList<Binding>();
    
    protected List<BindingFieldInfo> bindingFieldInfos = new ArrayList<BindingFieldInfo>();
    
    private List<BindingHolderListener<T>> listeners = new ArrayList<BindingHolderListener<T>>();
    
    private Map<String, Set<BindingFieldInfo>> tmpCheckBoxGroups = new HashMap<String, Set<BindingFieldInfo>>();
    
    public T getBean() {
    	return beanWrapper.getBeanWrapperContent();
    }
    
    public BindingHolder(UpdateStrategy updateStrategy, BeanWrapper<T> beanWrapper) {
    	this.updateStrategy = updateStrategy;
    	this.beanWrapper = beanWrapper;
    }

    public BindingGroup addBindingGroup(String sourceProperty, Widget targetWidget, String targetProperty, BeanProxyWrapper<? extends IDomainObject<?>, ? extends IDomainObject<?>> sourceObject) {
        return addBindingGroupForObject(sourceProperty, targetWidget, targetProperty, sourceObject);
    }
    
    public BindingGroup addBindingGroup(String sourceProperty, Widget targetWidget, String targetProperty, BeanWrapper<? extends IDomainObject<?>> sourceObject) {
        return addBindingGroupForObject(sourceProperty, targetWidget, targetProperty, sourceObject);
    }
    
    private BindingGroup addBindingGroupForObject(String sourceProperty, Widget targetWidget, String targetProperty, Object sourceObject) {
    	BindingFieldInfo bindingFieldInfo = new BindingFieldInfo();
    	bindingFieldInfo.setSourceProperty(sourceProperty);
    	bindingFieldInfo.setTargetProperty(targetProperty);
    	bindingFieldInfo.setTargetWidget(targetWidget);
    	bindingFieldInfo.setSourceObject(sourceObject);
    	bindingFieldInfos.add(bindingFieldInfo);

	    BindingGroup bindingGroup = new BindingGroup();
	    bindingGroup.addBinding(Bindings.createAutoBinding(updateStrategy, sourceObject,
	    		BeanProperty.create(sourceProperty), targetWidget, BeanProperty.create(targetProperty)));
	    asyncbindingGroups.add(bindingGroup);
	    return bindingGroup; 
    }
        
    /**
     * Add one-to-one binding to the root binding group. Be sure you are not add x-to-many binding using
     * this method. For x-to-many binding you should you use addBindingGroup
     */
    @Override
    public Binding addBinding(String sourceProperty, Object targetWidget, String targetProperty) {
    	return addBinding(sourceProperty, targetWidget, targetProperty, null, null);
    }
    
    public Binding addBinding(String sourceProperty, Object targetWidget, String targetProperty, Converter<?, ?> converter, Validator<?> validator) {
    	if(targetWidget instanceof CheckBox) {
    		String group = ((CheckBox)targetWidget).getName();
    		Set<BindingFieldInfo> set = tmpCheckBoxGroups.get(group);
    		if(set == null) {
    			set = new HashSet<BindingFieldInfo>();
    			tmpCheckBoxGroups.put(group, set);
    		}
    		set.add(addBindingFieldInfo(sourceProperty, targetWidget, targetProperty));
    		
    		return null;
    	}
    	
    	addBindingFieldInfo(sourceProperty, targetWidget, targetProperty);
    	
    	AutoBinding binding = createBinding(sourceProperty, targetWidget, targetProperty);
    	if(converter != null) {
    		binding.setConverter(converter);
    	}
    	if(validator != null) {
    		binding.setValidator(validator);
    	}
		rootBinding.addBinding(binding);
		return binding;
	}
    
    public ListBoxBinding addListBoxBinding(List<?> list, ListBox targetWidget, String itemTextProperty, String itemValueProperty) {
    	ListBoxBinding ab = GWTBindings.createListBoxBinding(updateStrategy, list, targetWidget);
    	if(itemTextProperty != null) {
    		ab.setItemTextBinding(BeanProperty.create(itemTextProperty));
    	}
    	if(itemValueProperty != null) {
    		ab.setItemValueBinding(BeanProperty.create(itemValueProperty));	
    	}
		ab.bind();
		return ab;
    }
    
    @SuppressWarnings("unchecked")
	public Binding addSelectedItemBinding(String sourceProperty, Object targetWidget, Converter<?, ?> converter) {
    	addBindingFieldInfo(sourceProperty, targetWidget, "selectedItem");
    	
    	final Binding selectionBinding = createBinding(sourceProperty, targetWidget, "selectedItem");
    	selectionBinding.setConverter(converter);
    	selectionBinding.bind();
    	selectionGroup.add(selectionBinding);
    	return selectionBinding;
    }
    
    @SuppressWarnings("unchecked")
	public void removeSelectedItemBinding(Binding selectionBinding) {
    	selectionGroup.remove(selectionBinding);
    }

	@SuppressWarnings("unchecked")
	private AutoBinding createBinding(String sourceProperty, Object targetWidget,
			String targetProperty) {
		return Bindings.createAutoBinding(updateStrategy, beanWrapper, BeanProperty.create(sourceProperty),
				targetWidget, BeanProperty.create(targetProperty));
	}
    
    private BindingFieldInfo addBindingFieldInfo(String sourceProperty, Object targetWidget, String targetProperty) {
    	BindingFieldInfo bindingFieldInfo = new BindingFieldInfo();
    	bindingFieldInfo.setSourceProperty(sourceProperty);
    	bindingFieldInfo.setTargetProperty(targetProperty);
    	bindingFieldInfo.setTargetWidget(targetWidget);
    	bindingFieldInfos.add(bindingFieldInfo);
	    
	    return bindingFieldInfo;
    }

    public void bind() {
    	rootBinding.bind();
    	for(BindingHolderListener<T> listener : listeners) {
    		listener.bindingBecameBound(this);
    	}
    }
    
    public void rebind() {
		if (bindingFieldInfos.size() > 0) {
			rootBinding.unbind();
			rootBinding.bind();
			
			for (BindingGroup binding : asyncbindingGroups) {
				binding.unbind();
				binding.bind();
			}
		}
    }

	public void setBean(T bean) {
		boolean reloadBindings = false;
		if (bindingFieldInfos.size() > 0) {
			rootBinding.unbind();
			for (BindingGroup binding : asyncbindingGroups) {
				binding.unbind();
			}
			for(Binding binding : selectionGroup) {
				binding.unbind();
			}
			reloadBindings = true;
		}
		
		beanWrapper.setBeanWrapperContent(bean);

		if (reloadBindings) {
			bind();
			for (BindingGroup binding : asyncbindingGroups) {
				binding.bind();
			}
		}
	}
	
	public void addListener(BindingHolderListener listener) {
		listeners.add(listener);
	}
	
	public static interface BindingHolderListener<B> {
		void bindingBecameBound(BindingHolder<B> holder);
	}
}
