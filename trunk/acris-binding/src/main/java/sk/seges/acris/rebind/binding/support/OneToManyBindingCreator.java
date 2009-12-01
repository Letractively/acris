package sk.seges.acris.rebind.binding.support;

import java.beans.IntrospectionException;

import org.apache.commons.lang.ArrayUtils;
import org.gwt.beansbinding.core.client.BeanProperty;
import org.gwt.beansbinding.core.client.BindingGroup;

import sk.seges.acris.bind.annotations.BindingField;
import sk.seges.acris.bind.annotations.FieldSpecLoader;
import sk.seges.acris.bind.providers.annotations.OneToMany;
import sk.seges.acris.rebind.GeneratorException;
import sk.seges.acris.rebind.bean.PropertyResolver;
import sk.seges.acris.rebind.binding.BindingProxyWraperCreator;
import sk.seges.acris.rebind.loader.DefaultLoaderCreatorFactory;
import sk.seges.acris.rebind.loader.FieldSpecLoaderCreator;
import sk.seges.acris.rebind.loader.ILoaderCreator;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;

class OneToManyBindingCreator extends AbstractBindingCreator<OneToMany> implements IBindingCreator<OneToMany> {

	private PropertyResolver propertyResolver;
	
	@Override
	public Class<OneToMany> getSupportedClass() {
		return OneToMany.class;
	}

	@Override
	public String getPropertyValue(OneToMany annotation) {
		return annotation.value();
	}
	
	@Override
	protected String[] generateImports(JField field, BindingField bindingFieldAnnotation) throws IntrospectionException {

		PropertyResolver propertyResolver = new PropertyResolver(bindingFieldAnnotation.value());
		JClassType classType = propertyResolver.resolveBeanPropertyClassType(parentBeanClassType);
		
		ILoaderCreator loaderCreator = getLoaderCreator(field);

		String[] loaderImports = loaderCreator.getImports();
		
		return (String[])ArrayUtils.addAll(loaderImports, new String[] {
				classType.getQualifiedSourceName(),
				BeanProperty.class.getCanonicalName(),
				BindingGroup.class.getCanonicalName()
		});
	}
	
	@Override
	public boolean generateBinding(JField field, BindingField bindingFieldAnnotation)
			throws UnableToCompleteException {

		JClassType classType;
		PropertyResolver propertyResolver;
		
		try {
			propertyResolver = new PropertyResolver(bindingFieldAnnotation.value());
			classType = propertyResolver.resolveBeanPropertyClassType(parentBeanClassType);
		} catch (IntrospectionException e) {
			logger.log(Type.ERROR, e.getMessage());
			throw new UnableToCompleteException();
		}

		sourceWriter.println(classType.getSimpleSourceName() + " " + classType.getSimpleSourceName().toLowerCase() + " = (" + 
				classType.getSimpleSourceName() + ")" + BeanProperty.class.getSimpleName() + ".create(\"" + propertyResolver.getBeanProperty() + "\").getValue(getBeanWrapper());");
		sourceWriter.println("");
		
		String propertyTuple = propertyResolver.getPropertyTuple(parentBeanClassType);
		String beanProxyWrapper = propertyTuple + BindingProxyWraperCreator.WRAPPER_SUFFIX;
		
		sourceWriter.println(getFirstLowerCase(beanProxyWrapper) + ".setTargetWrapper(getBeanWrapper());");
		sourceWriter.println(getFirstLowerCase(beanProxyWrapper) + ".setContent(" + classType.getSimpleSourceName().toLowerCase() + ");");
		sourceWriter.println("");
		sourceWriter.println("final BindingGroup bg" + propertyTuple + " = " + formPanelField + ".getContainerWidget().addBindingGroup(\"" + propertyResolver.getBeanPropertyReference() + 
				"\", " + field.getName() + ", \"" + getWidgetBindingAdapterProperty(field) + "\", " + getFirstLowerCase(beanProxyWrapper) + ");");
		sourceWriter.println("");
		
		try {
			ILoaderCreator loaderCreator = getLoaderCreator(field);
			loaderCreator.generateLoader(sourceWriter, classType, propertyResolver.getBeanPropertyReference(), 
							field.getName(), beanProxyWrapper, "bg" + propertyTuple);
		} catch (GeneratorException e) {
			logger.log(Type.ERROR, e.getMessage());
			throw new UnableToCompleteException();
		} catch (IntrospectionException e) {
			logger.log(Type.ERROR, e.getMessage());
			throw new UnableToCompleteException();
		}

		return true;
	}

	private ILoaderCreator getLoaderCreator(JField field) throws IntrospectionException {
		FieldSpecLoader fieldSpecLoader = field.getAnnotation(FieldSpecLoader.class);
		
		if (fieldSpecLoader == null || fieldSpecLoader.value() == null) {
			try {
				return DefaultLoaderCreatorFactory.getLoaderCreator();
			} catch (GeneratorException e) {
				throw new IntrospectionException(e.getMessage());
			}
		}

		FieldSpecLoaderCreator fieldSpecLoaderCreator = new FieldSpecLoaderCreator();
		fieldSpecLoaderCreator.setDataLoaderCreatorClass(fieldSpecLoader.value());
		
		return fieldSpecLoaderCreator;

	}
	
	@Override
	public boolean generateFields(JField field, BindingField bindingFieldAnnotation)
			throws UnableToCompleteException {

		try {
			propertyResolver = new PropertyResolver(bindingFieldAnnotation.value());
		} catch (IntrospectionException e) {
			logger.log(Type.ERROR, e.getMessage());
			throw new UnableToCompleteException();
		}

		String defaultValue = bindingFieldAnnotation.defaultValue();
		
		if (defaultValue.equals("null")) {
			defaultValue = null;
		}
/*		Object defaultValueConverted = null;

		if (defaultValue != null) {
			Class<?> converterClass = bindingFieldAnnotation.getDefaultValueConverter();
			
			if (converterClass != null) {
				Constructor<?> constructor;
				try {
					constructor = converterClass.getDeclaredConstructor();
				} catch (Exception e) {
					logger.log(Type.ERROR, "Unable to obtain default constructor of convertor " + converterClass);
					throw new UnableToCompleteException();
				}
			    constructor.setAccessible(true);

			    Object converterInstance;
			    try {
					converterInstance = constructor.newInstance();
				} catch (Exception e) {
					logger.log(Type.ERROR, "Unable to create instance of convertor " + converterClass);
					throw new UnableToCompleteException();
				}
				
				if (converterInstance instanceof Converter) {
					defaultValueConverted = ((Converter)converterInstance).convertForward(defaultValue);
				} else {
					logger.log(Type.ERROR, "Unable to create instance of convertor " + converterClass + ". Converter should extends " + Converter.class.getCanonicalName());
					throw new UnableToCompleteException();
				}
			} else {
				defaultValueConverted = defaultValue;
			}
		}
	*/	
		BindingProxyWraperCreator bindingProxyWraperCreator = new BindingProxyWraperCreator(context, logger, defaultValue);
		String beanProxyWrapper = bindingProxyWraperCreator.generate(packageName, parentBeanClassType, propertyResolver);

		sourceWriter.println("private final " + beanProxyWrapper + " " + getFirstLowerCase(beanProxyWrapper) + " = new " + beanProxyWrapper + "();");

		return false;
	}
	
	private String getFirstLowerCase(String text) {
		return text.substring(0, 1).toLowerCase() + text.substring(1);
	}
}