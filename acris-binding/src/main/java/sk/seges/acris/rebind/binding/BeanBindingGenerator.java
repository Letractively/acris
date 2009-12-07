package sk.seges.acris.rebind.binding;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class BeanBindingGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		BeanBindingCreator creator = new BeanBindingCreator();
		return creator.generate(logger, context, typeName);
	}
}