package sk.seges.acris.theme.pap.specific;

import javax.lang.model.element.ExecutableElement;

import sk.seges.acris.widget.client.form.ImageCheckBox;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.user.client.ui.CheckBoxHelper;

public class ThemeImageCheckBoxProcessor extends AbstractComponentSpecificProcessor {

	@Override
	public void process(Statement statement, ThemeContext themeContext, FormattedPrintWriter pw) {

		switch (statement) {
			case CONSTRUCTOR:
				pw.println("component.parentElement.appendChild(component." + themeContext.getThemeSupport().elementName() + ");");
				pw.println("component.getElement().getChild(0).appendChild(getLabelElement());");
				break;

			case SUPER_CONSTRUCTOR_ARGS:
				pw.print(", component.resources, \"" + themeContext.getThemeName() + "-\"");
				break;
				
			case CLASS:
				pw.println("protected ", LabelElement.class, " getLabelElement() {");
				pw.println("return ", CheckBoxHelper.class, ".getLabelElement(this);");
				pw.println("}");
				pw.println("");
				break;

			default:
				break;
		}
	}

	@Override
	protected Class<?>[] getComponentClasses() {
		return new Class<?>[] { ImageCheckBox.class };
	}

	@Override
	public boolean isComponentMethod(ExecutableElement method) {
		return true;
	}
}