/**
 * 
 */
package sk.seges.acris.binding.bind;

import sk.seges.sesam.domain.IObservableObject;

/**
 * <p>BeanWrapper wraps existing bean and ensures that we can provide dynamic accessing
 * of the properties via getAttribute and setAttribute methods. All property changes
 * also fires propertyChange listeners. See {@link IObservableObject} for further 
 * details.</p>
 * <p>Bean wrapper is mostly used transparently by generators in binding mechanism in the
 * way that whenever user sets binding bean to the binding form, it is automatically
 * wrapped by bean wrapper and used by beans-binding.</p>
 * <p>Provides transparent reflection mechanism in GWT and is part of the 
 * {@link Introspection} while resolving beanProperty in the "runtime"</p>
 *  
 * @author eldzi
 */
public interface BeanWrapper<T> extends IObservableObject {
	
	/**
	 * Used for setting wrapped content. BeanWrapper delegates all get and
	 * set method requests to this instance. If no value is set, than 
	 * {@link NullPointerException} are thrown because bean wrapper over
	 * the null object is not valid.
	 */
	void setContent(T content);

	/**
	 * User can obtain original bean from bean wrapper with set values using this method.
	 */
	T getContent();

	/**
	 * Method will delegate request to read method for appropriate property in
	 * wrapped bean
	 * <pre> {@code getAttribute("name")}</pre> 
	 * will call: 
	 * <pre> {@code content.getName();}</pre> 
	 */
	Object getAttribute(String attr);

	/**
	 * Method will delegate request to write method for appropriate property in
	 * wrapped bean.
	 * <pre> {@code setAttribute("name","test")}</pre> 
	 * will call: 
	 * <pre> {@code content.setName("test");}</pre> 
	 */
	void setAttribute(String attr, Object value);
}
