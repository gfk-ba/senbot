package com.gfk.senbot.framework.services.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.context.SenBotContext;

import cucumber.api.DataTable;

/**
 * A util class for all HTML table related selenium actions
 * 
 * @author joostschouten
 *
 */
public class TableService extends BaseServiceHub {

    private final ElementService seleniumElementService;

    /**
     * Constructor 
     * 
     * @param seleniumElementService A ElementService object
     */
    public TableService(ElementService seleniumElementService) {
        this.seleniumElementService = seleniumElementService;
    }

    /**
     * Compare method for asserting an expected cucumber {@link DataTable} to a found HTML table on a page
     * 
     * When dealing with colspan or rowspan, mark the cells which are missing in the HTML in your expected result with <code>&#60;colspan&#62;</code> 
     * and <code>&#60;rowspan&#62;</code> to indicate they need substituting
     * 
     * @param expectedContent a {@link ExpectedTableDefinition} wrapper around the {@link DataTable} constructed by Cucumber which allows some more
     * filter settings defining how to match the expected to the found
     * @param table the {@link WebElement} as found on the HTML page
     * @throws Throwable
     */
    public void compareTable(ExpectedTableDefinition expectedContent, WebElement table) throws Throwable {

        try {
            SenBotContext.getSeleniumDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
            List<List<String>> expectedRows = expectedContent.getExpected().raw();

            List<WebElement> foundRows = table.findElements(By.tagName("tr"));
            List<WebElement> filteredRows = new ArrayList<WebElement>();

            expectedContent.cacheIncludeAndIgnore(table);

            for (WebElement row : foundRows) {
                if ((expectedContent.getIncludeByMatches().isEmpty() || expectedContent.getIncludeByMatches().contains(row)) && !expectedContent.getIgnoreByMatches().contains(row)) {
                    filteredRows.add(row);
                }
            }

            assertEquals("The found table should have the expected number of rows", expectedRows.size(), filteredRows.size());

            LinkedHashMap<String, Integer> columnHeaders = new LinkedHashMap<String, Integer>();

            for (int rowCount = 0; rowCount < expectedRows.size(); rowCount++) {
                WebElement foundRow = filteredRows.get(rowCount);
                List<WebElement> foundCells = foundRow.findElements(By.tagName("td"));
                foundCells.addAll(foundRow.findElements(By.tagName("th")));
                List<String> expectedRow = expectedRows.get(rowCount);

                if (rowCount == 0) {
                    //capture potential column headers

                    //a map to maintain which header WebElement represents which index
                    List<WebElement> registeredColumnHeaders = new ArrayList<WebElement>();
                    for (int c = 0; c < expectedRow.size(); c++) {
                        String expectedHeader = expectedRow.get(c);
                        String expectedHeaderKey = expectedHeader + "_" + c;
                        Integer foundHeaderIndex = 0;
                        for (WebElement foundHeaderCell : foundCells) {

                            String foundHeaderText = foundHeaderCell.getText().trim();
                            //replace line breaks with spaces
                            foundHeaderText = foundHeaderText.replaceAll("\n", " ");
                            if (foundHeaderText.equals(expectedHeader) && !registeredColumnHeaders.contains(foundHeaderCell)) {
                                if (columnHeaders.get(expectedHeaderKey) != null && expectedContent.isMatchOnlyPassedInColumns()) {
                                    fail("The expected column header \"" + expectedHeader + "\" is not unique in the HTML table");
                                }
                                columnHeaders.put(expectedHeaderKey, foundHeaderIndex);
                                registeredColumnHeaders.add(foundHeaderCell);
                                break;
                            }
                            foundHeaderIndex++;
                        }
                        if (columnHeaders.get(expectedHeaderKey) == null) {
                            fail("The expected column header \"" + expectedHeaderKey + "\" was not found in the HTML table");
                        }
                    }
                }

                int cellCount = 0;
                int expectedSpanCells = 0;
                for (String columnHeaderKey : columnHeaders.keySet()) {

                    String expectedValue = getReferenceService().namespaceString(expectedRow.get(cellCount).trim());
                    if ("<rowspan>".equals(expectedValue) || "<colspan>".equals(expectedValue)) {
                        //a missing cell is expected here that exists due to a rowspan or colspan in an earlier row/column. ignore the cell
                        expectedSpanCells++;
                    } else {
                        Integer foundColumnIndex = columnHeaders.get(columnHeaderKey);

                        String foundValue = foundCells.get(foundColumnIndex - expectedSpanCells).getText();

                        foundValue = foundValue.trim().replaceAll("\n", " ");
                        assertEquals("Cell " + (cellCount + 1) + " (column: " + columnHeaderKey + ") of row " + (rowCount + 1) + " should be as expected ", expectedValue, foundValue);

                    }

                    cellCount++;

                }

                if (expectedContent.isMatchOnlyPassedInColumns()) {
                    assertEquals("Found row " + rowCount + " should have the expected number of cells", expectedRow.size(), columnHeaders.size());
                } else {
                    assertEquals("Found row " + rowCount + " should have the expected number of cells", expectedRow.size(), foundCells.size() + expectedSpanCells);
                }
            }
        } finally {
            SenBotContext.getSeleniumDriver().manage().timeouts().implicitlyWait(SenBotContext.getSenBotContext().getSeleniumManager().getTimeout(), TimeUnit.MILLISECONDS);
        }
    }

}
