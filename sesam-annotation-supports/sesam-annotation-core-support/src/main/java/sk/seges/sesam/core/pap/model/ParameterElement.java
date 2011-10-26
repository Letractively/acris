package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public class ParameterElement {

	private MutableTypeMirror type;
	private String name;
	private boolean isPropagated;
	
	public ParameterElement(MutableTypeMirror type, String name, boolean isPropagated) {
		this.type = type;
		this.name = name;
		this.isPropagated = isPropagated;
	}

	public MutableTypeMirror getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public boolean isConverter() {
		return false;
	}
	
	public boolean isPropagated() {
		return isPropagated;
	}
}