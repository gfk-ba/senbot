package com.gfk.senbot.framework.context;

public class PropertyOverwritePlaceholder {
	
	private String overwriteProp;
	private final String defaultValue;

	public PropertyOverwritePlaceholder(String property, String... overwriteShortHand) {
		this.defaultValue = property;
		for(String overwrite : overwriteShortHand) {			
			overwriteProp = System.getProperty(overwrite);
			if(overwriteProp != null) {
				break;
			}
		}
	}
	
	public String getProperty() {
		return overwriteProp == null ? defaultValue : overwriteProp ;
	}

}
