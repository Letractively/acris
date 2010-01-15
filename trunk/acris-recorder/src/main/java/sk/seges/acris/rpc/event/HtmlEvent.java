package sk.seges.acris.rpc.event;

import sk.seges.acris.rpc.bean.IAccessibleBean;
import sk.seges.acris.rpc.event.generic.AbstractGenericTargetableEvent;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

public class HtmlEvent extends AbstractGenericTargetableEvent implements IAccessibleBean {

	public HtmlEvent() {
	}
	
	public HtmlEvent(Event event) {
		super(event);
	}

	public static boolean isCorrectEvent(Event event) {
		int type = DOM.eventGetType(event);
		return isCorrectEvent(type);
	}

	private static boolean isCorrectEvent(int type) {
		switch (type) {
		case Event.ONBLUR:
			return true;
		case Event.ONCHANGE:
			return true;
		case Event.ONCONTEXTMENU:
			return true;
		case Event.ONERROR:
			return true;
		case Event.ONFOCUS:
			return true;
		case Event.ONLOAD:
			return true;
		case Event.ONSCROLL:
			return true;
		}
		return false;
	}

	protected NativeEvent createEvent(Element el) {
		return Document.get().createHtmlEvent(type, canBubble, cancelable);
	}

	public String toString(boolean pretty, boolean detailed) {
		if (pretty) {
			if (!detailed) {
				return type;
			} else {
				return type;
			}
		} else {
			if (!detailed) {
				return "HtmlEvent [type=" + type + "]";
			} else {
				return "HtmlEvent [type=" + type + "]";
			}
		}
	}

	public int getTypeInt() {
		if (BlurEvent.getType().getName().equals(type)) {
			return 0;
		} else if (ChangeEvent.getType().getName().equals(type)) {
			return 1;
		} else if (ContextMenuEvent.getType().getName().equals(type)) {
			return 2;
		} else if (ErrorEvent.getType().getName().equals(type)) {
			return 3;
		} else if (FocusEvent.getType().getName().equals(type)) {
			return 4;
		} else if (LoadEvent.getType().getName().equals(type)) {
			return 5;
		} else if (ScrollEvent.getType().getName().equals(type)) {
			return 6;
		}
		
		throw new IllegalArgumentException("Unknown event type");
	}

	@Override
	public void setTypeInt(int type) {
		switch (type) {
		case 0:
			this.type = BlurEvent.getType().getName();
			return;
		case 1:
			this.type = ChangeEvent.getType().getName();
			return;
		case 2:
			this.type = ContextMenuEvent.getType().getName();
			return;
		case 3:
			this.type = ErrorEvent.getType().getName();
			return;
		case 4:
			this.type = FocusEvent.getType().getName();
			return;
		case 5:
			this.type = LoadEvent.getType().getName();
			return;
		case 6:
			this.type = ScrollEvent.getType().getName();
			return;
		}
		
		throw new IllegalArgumentException("Unknown event type: " + type);
	}
}