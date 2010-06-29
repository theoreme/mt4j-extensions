package de.molokoid.extensions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;

import de.molokoid.css.parserConnector;
import de.molokoid.data.CSSStyle;
import de.molokoid.data.CSSStyleHierarchy;
import de.molokoid.data.CSSStyleManager;

import processing.core.PApplet;

public class MTCSSRectangle extends MTRectangle {
	
	List<CSSStyle> privateStyleSheets = new ArrayList<CSSStyle>();
	List<CSSStyleHierarchy> sheets = new ArrayList<CSSStyleHierarchy>();
	CSSStyle virtualStyleSheet = null;
	CSSStyleManager cssStyleManager;
	MTApplication app;
	
	public MTCSSRectangle(CSSStyle style, float x, float y,	MTApplication mta, CSSStyleManager csm) {
		super(x, y, style.getWidth(), style.getHeight(), mta);
		this.privateStyleSheets.add(style);
		this.cssStyleManager = csm;
		this.app = mta;
		applyStylesheet();
	}
	
	public MTCSSRectangle(String uri, float x, float y, MTApplication mta, CSSStyleManager csm) {
		super(x,y,0,0, mta);
		parserConnector pc = new parserConnector(uri, mta);
		privateStyleSheets = pc.getCssh().getStyles();
		this.cssStyleManager = csm;
		this.app = mta;
		applyStylesheet();
		
		
		
	}
	
	
	


	public List<CSSStyle> getPrivateStyleSheets() {
		return privateStyleSheets;
	}

	public void setPrivateStyleSheets(List<CSSStyle> privateStyleSheets) {
		this.privateStyleSheets = privateStyleSheets;
	}

	public List<CSSStyleHierarchy> getSheets() {
		return sheets;
	}

	public void setSheets(List<CSSStyleHierarchy> sheets) {
		this.sheets = sheets;
	}
	
	public void setStyleSheet(CSSStyle sheet) {
		this.privateStyleSheets.add(sheet);
	}

	@SuppressWarnings("unchecked")
	public void evaluateStylesheets() {
		sheets = cssStyleManager.getRelevantStyles(this);
		Collections.sort(sheets);
		virtualStyleSheet = new CSSStyle(app);
		for (CSSStyleHierarchy h: sheets) {
			virtualStyleSheet.addStyleSheet(h.getStyle());
		}
		for (CSSStyle s: privateStyleSheets) {
			virtualStyleSheet.addStyleSheet(s);
		}

	}
	

	
	public void applyStylesheet() {
		evaluateStylesheets();
		this.setSizeLocal(virtualStyleSheet.getWidth(), virtualStyleSheet.getHeight());
		this.setFillColor(virtualStyleSheet.getBackgroundColor());
		this.setStrokeColor(virtualStyleSheet.getBorderColor());
		this.setStrokeWeight(virtualStyleSheet.getBorderWidth());
		this.setVisible(virtualStyleSheet.isVisibility());
	}

}
