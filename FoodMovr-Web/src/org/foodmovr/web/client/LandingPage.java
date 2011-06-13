package org.foodmovr.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class LandingPage extends UIObject {

	private static LandingPageUiBinder uiBinder = GWT
			.create(LandingPageUiBinder.class);

	interface LandingPageUiBinder extends UiBinder<Element, LandingPage> {
	}

	@UiField
	SpanElement nameSpan;

	public LandingPage(String firstName) {
		setElement(uiBinder.createAndBindUi(this));
		nameSpan.setInnerText(firstName);
	}

}
