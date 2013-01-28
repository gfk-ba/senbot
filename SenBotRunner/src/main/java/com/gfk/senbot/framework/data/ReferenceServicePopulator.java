package com.gfk.senbot.framework.data;

/**
 * 
 * This interface is meant for classes that want to fill the SenBotReferenceService with live 
 * 
 * @author joostschouten
 *
 */
public interface ReferenceServicePopulator {
	
	void populate(SenBotReferenceService referenceService);

}
