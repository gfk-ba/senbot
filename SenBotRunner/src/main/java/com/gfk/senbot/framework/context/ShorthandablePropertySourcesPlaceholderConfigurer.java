package com.gfk.senbot.framework.context;

import java.util.Map;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class ShorthandablePropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {
	
	private Map<String, String> shortHandOverwriteOptions;
	
	public ShorthandablePropertySourcesPlaceholderConfigurer() {
		super();
	}

	public Map<String, String> getShortHandOverwriteOptions() {
		return shortHandOverwriteOptions;
	}

	public void setShortHandOverwriteOptions(Map<String, String> shortHandOverwriteOptions) {
		this.shortHandOverwriteOptions = shortHandOverwriteOptions;
	}
	
	

}
