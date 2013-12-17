package com.gfk.senbot.framework.context;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class SpringPropertiesExposer extends PropertySourcesPlaceholderConfigurer {

    private static Map<String, String> propertiesMap = new HashMap<String, String>();
    
    public SpringPropertiesExposer() {
    	super();
    }
    
    @Override
    protected void loadProperties(Properties props) throws IOException {
    	super.loadProperties(props);    	
    	for (Object key : props.keySet()) {
    		String keyStr = key.toString();
    		String valueStr = props.getProperty(keyStr);
    		propertiesMap.put(keyStr, valueStr);
    	}
    }
    
    

    public static String getProperty(String name) {
        return propertiesMap.get(name);
    }

}
