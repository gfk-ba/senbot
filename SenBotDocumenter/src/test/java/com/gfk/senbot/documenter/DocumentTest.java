package com.gfk.senbot.documenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class DocumentTest {
	
	@Test
	public void testDocumenter() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		SenBotDocumenter senBotDocumenter = new SenBotDocumenter();
		senBotDocumenter.generateDocumentation();
		
		senBotDocumenter.outputToFile();
	}

}
