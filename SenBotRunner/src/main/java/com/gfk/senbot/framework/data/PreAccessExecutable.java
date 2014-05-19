package com.gfk.senbot.framework.data;

/**
 * Allow for a pre access call so that you can instantiate certain model obejcts when needed in stead of all during setup
 * 
 * @author joostschouten
 *
 */
public interface PreAccessExecutable {

	void preAccess();
}
