package sk.seges.sesam.pap.service.printer;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.model.ServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class ConverterParameterFieldPrinter extends AbstractParameterCollectorPrinter {
		
	public ConverterParameterFieldPrinter(TransferObjectProcessingEnvironment processingEnv, ServiceConverterParametersFilter parametersFilter, ConverterConstructorParametersResolver parametersResolver, FormattedPrintWriter pw) {
		super(processingEnv, parametersFilter, parametersResolver, pw);
	}

	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
		for (ConverterParameter converterParameter: converterParameters) {
			pw.println("protected ", converterParameter.getType(), " " + converterParameter.getName() + ";");
			pw.println();
		}
	}	
}