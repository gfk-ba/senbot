package com.gfk.senbot.framework.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gfk.senbot.framework.BaseServiceHub;

/**
 * A generic service for logging
 * @author Gerhard Schuster
 *
 */
public class LoggingService extends BaseServiceHub {

    /**
     * Logs the HTML body of the application 
     */
    public void logHTMLBoddy() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String date = dateFormat.format(new Date());
        File file = new File("target/test-results/TestSite_" + date + ".html");

        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.append(getWebDriver().getPageSource());
            output.close();
        } catch (IOException e) {
            //ToDo: Deal with the exception
        }
    }
}
