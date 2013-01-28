package com.gfk.senbot.framework.services.selenium;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.context.SenBotContext;

import cucumber.api.DataTable;

/**
 * A wrapper around the cucumber {@link DataTable} defining how the passed expected content should match with the 
 * table element on the page
 * 
 * @author joostschouten
 *
 */
public class ExpectedTableDefinition {

    private DataTable        expected;
    private boolean          matchOnlyPassedInColumns = false;
    private List<By>         ignoreRowsMatching       = new ArrayList<By>();
    private List<WebElement> ignoreByMatches          = null;
    private List<By>         includeOnlyRowsMatching  = new ArrayList<By>();
    private List<WebElement> includeByMatches         = null;

    /**
     * Constructor
     * @param expectedTableTable The table how it should look like
     */
    public ExpectedTableDefinition(DataTable expectedTableTable) {
        expected = expectedTableTable;
    }

    /**
     * @return The expected result 
     */
    public DataTable getExpected() {
        return expected;
    }

    /**
     * The expected result 
     */
    public void setExpected(DataTable expected) {
        this.expected = expected;
    }

    /**
     * @return ignore all columns that are not passed in. Columns will be recognized by the column header meaning that
     * the first row found will be used as the column headers
     */
    public boolean isMatchOnlyPassedInColumns() {
        return matchOnlyPassedInColumns;
    }

    /**
     * ignore all columns that are not passed in. Columns will be recognized by the column header meaning that
     * the first row found will be used as the column headers
     * 
     * @param matchOnlyPassedInColumns 
     */
    public void setMatchOnlyPassedInColumns(boolean matchOnlyPassedInColumns) {
        this.matchOnlyPassedInColumns = matchOnlyPassedInColumns;
    }

    /**
     * @return Exclude all HTML rows that match this list of selenium {@link By}'s 
     */
    public List<By> getIgnoreRowsMatching() {
        return ignoreRowsMatching;
    }

    /**
     * @param Exclude all HTML rows that match this list of selenium {@link By}'s
     */
    public void setIgnoreRowsMatching(List<By> ignoreRowsMatching) {
        this.ignoreRowsMatching = ignoreRowsMatching;
    }

    /**
     * @return Include only HTML rows that match this list of selenium {@link By}'s
     */
    public List<By> getIncludeOnlyRowsMatching() {
        return includeOnlyRowsMatching;
    }

    /**
     * Include only HTML rows that match this list of selenium {@link By}'s
     * 
     * @param includeOnlyRowsMatching
     */
    public void setIncludeOnlyRowsMatching(List<By> includeOnlyRowsMatching) {
        this.includeOnlyRowsMatching = includeOnlyRowsMatching;
    }

    /**
     * Does the table comparison
     * 
     * @param table
     */
    public void cacheIncludeAndIgnore(WebElement table) {
        if (getIgnoreByMatches() == null) {
            setIgnoreByMatches(new ArrayList<WebElement>());
            for (By by : getIgnoreRowsMatching()) {
                getIgnoreByMatches().addAll(table.findElements(by));
            }
        }
        if (getIncludeByMatches() == null) {
            setIncludeByMatches(new ArrayList<WebElement>());
            for (By by : getIncludeOnlyRowsMatching()) {
                getIncludeByMatches().addAll(table.findElements(by));
            }
        }
    }

    /**
     * @return A cache list to store all elements matching
     */
    public List<WebElement> getIncludeByMatches() {
        return includeByMatches;
    }

    /**
     * A cache list to store all elements matching
     * 
     * @param includeByMatches 
     */
    public void setIncludeByMatches(List<WebElement> includeByMatches) {
        this.includeByMatches = includeByMatches;
    }

    /**
     * @return A cache list to store all elements matching ignores
     */
    public List<WebElement> getIgnoreByMatches() {
        return ignoreByMatches;
    }

    /**
     * A cache list to store all elements matching ignores
     * 
     * @param ignoreByMatches
     */
    public void setIgnoreByMatches(List<WebElement> ignoreByMatches) {
        this.ignoreByMatches = ignoreByMatches;
    }

}