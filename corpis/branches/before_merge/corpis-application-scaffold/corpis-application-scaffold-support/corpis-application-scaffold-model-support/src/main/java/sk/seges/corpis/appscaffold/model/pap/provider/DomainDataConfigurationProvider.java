package sk.seges.corpis.appscaffold.model.pap.provider;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import sk.seges.corpis.appscaffold.model.pap.model.DataConfigurationTypeElement;
import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;

public class DomainDataConfigurationProvider extends ClasspathConfigurationProvider {

	public DomainDataConfigurationProvider(ClassPathTypes classpathUtils, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(classpathUtils, processingEnv, roundEnv);
	}

	@Override
	protected ConfigurationTypeElement getConfigurationElement(MutableTypeMirror domainType, MutableTypeMirror dtoType,	Element annotatedElement) {
		return new DataConfigurationTypeElement((MutableDeclaredType)domainType, (MutableDeclaredType)dtoType, (TypeElement)annotatedElement, processingEnv, roundEnv);
	}
	
	@Override
	protected ConfigurationTypeElement getConfigurationElement(Element configurationElement, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		return new DataConfigurationTypeElement(configurationElement, processingEnv, roundEnv, this);
	}
}