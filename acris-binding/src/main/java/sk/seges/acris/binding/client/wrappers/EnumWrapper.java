/**
 * 
 */
package sk.seges.acris.binding.client.wrappers;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * @author eldzi
 */
public class EnumWrapper implements BeanWrapper<Enum<?>>, Serializable {
	private static final long serialVersionUID = 3855213515234440503L;
	private Enum<?> __content;
	
	/* (non-Javadoc)
	 * @see sk.seges.acris.binding.client.wrappers.BeanWrapper#getAttribute(java.lang.String)
	 */
	@Override
	public Object getBeanAttribute(String attr) {
		return null;
	}

	/* (non-Javadoc)
	 * @see sk.seges.acris.binding.client.wrappers.BeanWrapper#getBeanWrapperContent()
	 */
	@Override
	public Enum<?> getBeanWrapperContent() {
		return __content;
	}

	/* (non-Javadoc)
	 * @see sk.seges.acris.binding.client.wrappers.BeanWrapper#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setBeanAttribute(String attr, Object value) {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see sk.seges.acris.binding.client.wrappers.BeanWrapper#setBeanWrapperContent(java.lang.Object)
	 */
	@Override
	public void setBeanWrapperContent(Enum<?> content) {
		__content = content;
	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.domain.IObservableObject#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {

	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.domain.IObservableObject#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {

	}

	@Override
	public String toString() {
		return __content.toString();
	}
}
