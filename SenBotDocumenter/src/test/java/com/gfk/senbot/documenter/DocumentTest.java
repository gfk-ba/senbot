package com.gfk.senbot.documenter;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class DocumentTest {
	
	@Test
	public void testDocumenter() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		SenBotDocumenter.generateDocumentation();
	}

}
