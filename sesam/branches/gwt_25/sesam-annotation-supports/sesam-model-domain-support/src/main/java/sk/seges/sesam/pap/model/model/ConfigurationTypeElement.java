package sk.seges.sesam.pap.model.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;

public class ConfigurationTypeElement extends TomBaseType {

	private DtoDeclared dtoDeclaredType;
	private boolean dtoTypeElementInitialized = false;

	private DomainDeclaredType domainDeclaredType;
	private boolean domainTypeElementInitialized = false;
	
	private DomainDeclaredType instantiableDomainType;
	private boolean instantiableDomainTypeInitialized = false;
	
	private ConfigurationTypeElement delegateConfigurationTypeElement;
	private boolean delegateConfigurationTypeElementInitialized = false;

	private ConverterTypeElement converterTypeElement;
	private boolean converterTypeElementInitialized = false;

	private final Element configurationElement;
	protected final TransferObjectMappingAccessor transferObjectConfiguration;

	private final MutableDeclaredType domainType;
	protected final MutableDeclaredType dtoType;

	private final String canonicalName;

	protected final ConfigurationContext configurationContext;
	
	public ConfigurationTypeElement(MutableDeclaredType domainType, MutableDeclaredType dtoType, Element configurationElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext);

		this.configurationContext = configurationContext;
		this.configurationElement = configurationElement;
		this.domainType = domainType;
		this.dtoType = dtoType;
		
		if (configurationElement.getKind().equals(ElementKind.METHOD)) {
			this.canonicalName = configurationElement.getSimpleName().toString();
		} else {
			this.canonicalName = ((TypeElement)configurationElement).getQualifiedName().toString();
		}
		
		this.transferObjectConfiguration = new TransferObjectMappingAccessor(configurationElement, envContext.getProcessingEnv());
		if (this.transferObjectConfiguration.getReferenceMapping() == null) {
			this.transferObjectConfiguration.setReferenceMapping(transferObjectConfiguration.getMappingForDto(dtoType));
		}
	}
	
	public ConfigurationTypeElement(ExecutableElement configurationElementMethod, DomainDeclaredType returnType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		
		super(envContext);

		this.configurationContext = configurationContext;
		this.configurationElement = configurationElementMethod;
		this.domainType = returnType;
		if (configurationElementMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
			this.dtoType = (MutableDeclaredType) getTypeUtils().toMutableType(configurationElementMethod.getReturnType()); 
		} else {
			this.dtoType = returnType;
		}
		this.canonicalName = configurationElement.getSimpleName().toString();

		this.transferObjectConfiguration = new TransferObjectMappingAccessor(configurationElement, envContext.getProcessingEnv());
	}

	public ConfigurationTypeElement(Element configurationElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		
		super(envContext);

		this.configurationContext = configurationContext;
		this.configurationElement = configurationElement;
		this.domainType = null;
		this.dtoType = null;

		this.canonicalName = configurationElement.getSimpleName().toString();

		this.transferObjectConfiguration = new TransferObjectMappingAccessor(configurationElement, envContext.getProcessingEnv());
	}

	@Override
	protected MutableTypeMirror getDelegate() {
		if (configurationElement.getKind().equals(ElementKind.METHOD)) {
			return envContext.getProcessingEnv().getElementUtils().toMutableElement((ExecutableElement) configurationElement).asType();
		}
		return getTypeUtils().toMutableType((DeclaredType)configurationElement.asType());
	}

	public boolean isValid() {
		if (transferObjectConfiguration == null) {
			return false;
		}
		
		return transferObjectConfiguration.isValid();
	}
	
	private ConverterTypeElement getConverter(TransferObjectMappingAccessor transferObjectConfiguration) {
		
		if (!transferObjectConfiguration.isValid()) {
			return null;
		}
		
		TypeElement converter = transferObjectConfiguration.getConverter();
		if (converter != null && transferObjectConfiguration.isConverterGenerated()) {
			return new ConverterTypeElement(this, converter, envContext);
		}
				
		Element configurationElement = asConfigurationElement();
		
		if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED) || !transferObjectConfiguration.isConverterGenerated()) {
			return null;
		}

		return new ConverterTypeElement(this, envContext);
	}

	public DomainDeclaredType getRawDomain() {
		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getRawDomain();
		}

		if (transferObjectConfiguration.isValid()) {
			TypeElement domainInterface = transferObjectConfiguration.getDomainInterface();
			if (domainInterface != null) {
				DomainDeclared result = new DomainDeclared((MutableDeclaredType) getTypeUtils().toMutableType(domainInterface.asType()), dtoType, envContext, configurationContext);
				this.domainDeclaredType.setKind(MutableTypeKind.INTERFACE);
				return result;
			}
		}
		
		return getDomain();
	}

	private ConfigurationContext getConfigurationContextForDto(MutableDeclaredType dtoType) {
		ConfigurationContext configurationContext = new ConfigurationContext(envContext.getConfigurationEnv());
		List<ConfigurationTypeElement> configurations = envContext.getConfigurationEnv().getConfigurations(dtoType);
		if (configurations == null || configurations.size() == 0) {
			return null;
		}
		configurationContext.setConfigurations(configurations);
		return configurationContext;
	}
	
	public DomainDeclaredType getDomain() {
		
		if (!this.domainTypeElementInitialized) {
			MutableDeclaredType dtoType = getDtoType();
			
			if (dtoType != null) {
				ConfigurationContext configurationContextForDto = getConfigurationContextForDto(dtoType);
				if (configurationContextForDto != null) {
					this.domainDeclaredType = getDomain(configurationContextForDto.getDelegateDomainDefinitionConfiguration());
				} else {
					this.domainDeclaredType = getDomain(this);
				}
			} else {
				this.domainDeclaredType = getDomain(this);
			}
			this.domainTypeElementInitialized = true;
		}
		
		return domainDeclaredType;
	}

	public DomainDeclaredType getInstantiableDomain() {
		if (!instantiableDomainTypeInitialized) {
			MutableDeclaredType dtoType = getDtoType();
			if (dtoType != null) {
				ConfigurationContext configurationContextForDto = getConfigurationContextForDto(dtoType);
				if (configurationContextForDto != null && configurationContextForDto.getConverterDefinitionConfiguration() != null) {
					this.instantiableDomainType = getDomain(configurationContextForDto.getConverterDefinitionConfiguration());
				} else {
					this.instantiableDomainType = getDomain(this);
				}
			} else {
				this.instantiableDomainType = getDomain(this);
			}
//			this.instantiableDomainType = getDomain(this);
			this.instantiableDomainTypeInitialized = true;	
		}
		return instantiableDomainType;
	}
	
	private DomainDeclared getDomain(ConfigurationTypeElement configuration) {

		DomainDeclared domainDeclaredType = null;
		
		MutableDeclaredType domainType = null;
		
		if (configuration.domainType != null) {
			domainType = configuration.domainType;
		} else {
			TypeElement domainTypeElement = configuration.transferObjectConfiguration.getEvaluatedDomainType();
			if (domainTypeElement != null) {
				domainType = getTypeUtils().toMutableType((DeclaredType) domainTypeElement.asType());
			}
		}

		if (domainType != null) {
			domainDeclaredType = new DomainDeclared(domainType, configuration.dtoType, configuration.envContext, configuration.configurationContext);
			domainDeclaredType.prefixTypeParameter(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX);
		} else {
			if (configuration.domainType == null) {
				TypeElement domainInterface = configuration.transferObjectConfiguration.getDomainInterface();
				if (domainInterface != null) {
					if (configuration.dtoType != null && getTypeUtils().implementsType(configuration.dtoType, getTypeUtils().toMutableType(domainInterface.asType()))) {
						domainDeclaredType = new DomainDeclared(null, configuration.dtoType, configuration.envContext, configuration.configurationContext);
					} else {
						domainDeclaredType = new DomainDeclared((MutableDeclaredType) getTypeUtils().toMutableType(domainInterface.asType()), 
								configuration.dtoType, configuration.envContext, configuration.configurationContext);
						domainDeclaredType.setKind(MutableTypeKind.INTERFACE);
					}
				}
			}
		}

		if (domainDeclaredType == null) {
			configuration.dtoTypeElementInitialized = true;
			configuration.dtoDeclaredType = null;
		}
		
		return domainDeclaredType;
	}

	boolean hasGeneratedDto() {
		return transferObjectConfiguration.isDtoGenerated() != false && !hasDtoSpecified();
	}

	boolean hasGeneratedConverter() {
		return transferObjectConfiguration.isConverterGenerated() != false && !hasConverterSpecified();
	}
	
	boolean hasConverterSpecified() {
		return transferObjectConfiguration.getConverter() != null;
	}

	boolean hasDomainSpecified() {
		return transferObjectConfiguration.getDomain() != null || transferObjectConfiguration.getDomainInterface() != null;
	}
	
	boolean hasDtoSpecified() {
		return transferObjectConfiguration.getDto() != null || transferObjectConfiguration.getDtoInterface() != null;
	}
	
	public DtoDeclaredType getRawDto() {
		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getRawDto();
		}

		if (transferObjectConfiguration.isValid()) {
			TypeElement dtoInterface = transferObjectConfiguration.getDtoInterface();
			if (dtoInterface != null) {
				DtoDeclared result = new DtoDeclared(getTypeUtils().toMutableType((DeclaredType) dtoInterface.asType()), envContext, configurationContext);
				result.setInterface(true);
				return result;
			}
		}
		
		return getDto();
	}
	
	private Set<MutableTypeMirror> getDtoBounds(Set<? extends MutableTypeMirror> bounds) {
	
		Set<MutableTypeMirror> baseBounds = new HashSet<MutableTypeMirror>();
		
		for (MutableTypeMirror lowerBound : bounds) {
			baseBounds.add(envContext.getProcessingEnv().getTransferObjectUtils().getDomainType(lowerBound).getDto());
		}
		
		return baseBounds;
	}

	private MutableDeclaredType getDtoType() {
		
		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getDtoType();
		}

		if (this.dtoType != null) {
			return this.dtoType;
		}

		if (transferObjectConfiguration.isValid()) {
			TypeElement dtoTypeElement = transferObjectConfiguration.getDto();
			if (dtoTypeElement != null) {
				return getTypeUtils().toMutableType((DeclaredType) dtoTypeElement.asType());
			}

			if (dtoDeclaredType == null) {
				TypeElement dtoInterface = transferObjectConfiguration.getDtoInterface();
				if (dtoInterface != null) {
					if (domainType != null && getTypeUtils().implementsType(domainType, getTypeUtils().toMutableType(dtoInterface.asType()))) {
						MutableDeclaredType domain = domainType.clone();
						for (MutableTypeVariable typeVariable: domain.getTypeVariables()) {
							typeVariable.setLowerBounds(getDtoBounds(typeVariable.getLowerBounds()));
							typeVariable.setUpperBounds(getDtoBounds(typeVariable.getUpperBounds()));
						}
						return domain;
					}
					
					return getTypeUtils().toMutableType((DeclaredType) dtoInterface.asType());
				}
			}
		}

		if (this.dtoDeclaredType == null) {
			Element configurationElement = asConfigurationElement();
			
			if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
				return null;
			}

			return new DtoDeclared(envContext, new ConfigurationContext(envContext.getConfigurationEnv()).addConfiguration(this));
		}

		return dtoDeclaredType;
	}

	public DtoDeclaredType getDto() {
		
		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getDto();
		}

		if (!dtoTypeElementInitialized) {
			
			MutableDeclaredType dtoType = null;
			
			if (this.dtoType != null) {
				dtoType = this.dtoType;
			} else {
				if (transferObjectConfiguration.isValid()) {
					TypeElement dtoTypeElement = transferObjectConfiguration.getDto();
					if (dtoTypeElement != null) {
						dtoType = getTypeUtils().toMutableType((DeclaredType) dtoTypeElement.asType());
					}
				}
			}
			
			if (dtoType != null) {
				this.dtoDeclaredType = new DtoDeclared(dtoType, envContext, configurationContext);
			} else {
				if (this.dtoType == null && transferObjectConfiguration.isValid()) {
					TypeElement dtoInterface = transferObjectConfiguration.getDtoInterface();
					if (dtoInterface != null) {
						if (domainType != null && getTypeUtils().implementsType(domainType, getTypeUtils().toMutableType(dtoInterface.asType()))) {
							this.dtoDeclaredType = new DtoDeclared(domainType, envContext, configurationContext);
						} else {
							this.dtoDeclaredType = new DtoDeclared(getTypeUtils().toMutableType((DeclaredType) dtoInterface.asType()), envContext, configurationContext);
							this.dtoDeclaredType.setInterface(true);
						}
					}
				}

				if (this.dtoDeclaredType == null) {
					Element configurationElement = asConfigurationElement();
					
					if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
						return null;
					}

					this.dtoDeclaredType = new DtoDeclared(envContext, configurationContext);
					this.dtoDeclaredType.setup();
				}
			}

			this.dtoTypeElementInitialized = true;
		}
		
		return dtoDeclaredType;
	}
	
	public ConverterTypeElement getConverter() {

		if (!converterTypeElementInitialized || getDomain().hasTypeParameters()) {
			this.converterTypeElement = getConverter(transferObjectConfiguration);
			this.converterTypeElementInitialized = true;
		}
		
		if (converterTypeElement != null) {
			if (converterTypeElement.isGenerated()) {
				if (getDelegateConfigurationTypeElement() != null) {
					ConverterTypeElement delegateConverterTypeElement = getDelegateConfigurationTypeElement().getConverter();
					if (delegateConverterTypeElement != null) {
						return delegateConverterTypeElement;
					}
				}
			}
			return converterTypeElement;
		}

		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getConverter();
		}
		
		return null;
	}
	
	public Element asConfigurationElement() {
		return configurationElement;
	}
	
	public ConfigurationTypeElement getDelegateConfigurationTypeElement() {
		if (!delegateConfigurationTypeElementInitialized) {
			if (transferObjectConfiguration.getConfiguration() != null) {
				this.delegateConfigurationTypeElement = new ConfigurationTypeElement(transferObjectConfiguration.getConfiguration(), envContext, configurationContext);
			} else {
				this.delegateConfigurationTypeElement = null;
			}

			this.delegateConfigurationTypeElementInitialized = true;
		}
		return delegateConfigurationTypeElement;
	}
	
	public boolean appliesForDomainType(MutableTypeMirror domainType) {
		
		//Configuration should be applied only for declared types like classes or interfaces
		if (!domainType.getKind().equals(MutableTypeKind.CLASS) &&
			!domainType.getKind().equals(MutableTypeKind.INTERFACE)) {
			return false;
		}
		
		return transferObjectConfiguration.getMappingForDomain((MutableDeclaredType)domainType) != null;
	}

	public boolean appliesForDtoType(MutableTypeMirror dtoType) {

		//Configuration should be applied only for declared types like classes or interfaces
		if (!dtoType.getKind().equals(MutableTypeKind.CLASS) &&
			!dtoType.getKind().equals(MutableTypeKind.INTERFACE)) {
				return false;
		}
		
		TransferObjectMapping mappingForDto = transferObjectConfiguration.getMappingForDto((MutableDeclaredType)dtoType);
		
		if (mappingForDto != null) {
			return true;
		}

		if (this.dtoType == null) {
			String dtoClassName = getDtoType().getCanonicalName();
			return (dtoClassName != null && dtoClassName.equals(dtoType.toString(ClassSerializer.CANONICAL, false)));
		}
		
		return false;
	}

	public String getCanonicalName() {
		return canonicalName;
	}
	
	public MappingType getMappingType() {
		MappingType mappingType = MappingType.AUTOMATIC;
		Mapping mapping =  configurationElement.getAnnotation(Mapping.class);

		if (mapping != null) {
			mappingType = mapping.value();
		}
		
		return mappingType;
	}

	boolean isFieldIgnored(String field) {

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(asConfigurationElement().getEnclosedElements());

		for (ExecutableElement overridenMethod : overridenMethods) {

			if (MethodHelper.toField(overridenMethod).equals(field)) {
				Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
				if (ignoreAnnotation != null) {
					return true;
				}
			}
		}

		return false;
	}
}