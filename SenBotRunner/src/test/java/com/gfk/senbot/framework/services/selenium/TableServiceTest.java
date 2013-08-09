package com.gfk.senbot.framework.services.selenium;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.data.MockReferenceDatePopulator;
import com.gfk.senbot.framework.data.SenBotReferenceService;

import cucumber.api.DataTable;

public class TableServiceTest extends AbstractSenbotServiceTest {

    private TableService seleniumTableService;

    @Before
    public void setup() {
        seleniumTableService = new TableService(seleniumElementService);
    }

    @Test
    public void testCompareTable_fullMatch() throws Throwable {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement table = seleniumElementService.translateLocatorToWebElement("Table locator");

        List<List<String>> expectedRows = new ArrayList<List<String>>();
        final List<String> header = Arrays.asList(new String[]{"Table header 1", "Table header 2"});
        final List<String> row1 = Arrays.asList(new String[]{"Table cell 1", "Table cell 2"});
        final List<String> row2 = Arrays.asList(new String[]{"Table cell 3", "Table cell 4"});
        final List<String> row3 = Arrays.asList(new String[]{"Table cell 5", "Table cell 6"});

        expectedRows = new ArrayList<List<String>>() {
            {
                add(header);
                add(row1);
                add(row2);
                add(row3);
            }
        };

        DataTable expectedContent = mock(DataTable.class);
        when(expectedContent.raw()).thenReturn(expectedRows);

        ExpectedTableDefinition expectedTableDefinition = new ExpectedTableDefinition(expectedContent);

        seleniumTableService.compareTable(expectedTableDefinition, table);
    }

    @Test
    public void testCompareTable_fullMatch_complex() throws Throwable {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.COMPLEX_TABLE_TEST_PAGE_URL);
        WebElement table = seleniumElementService.translateLocatorToWebElement("Table locator");

        List<List<String>> expectedRows = new ArrayList<List<String>>();
        final List<String> header = Arrays.asList(new String[]{"Table header 1", "Table header", "", "", "Table header 5", "Table header"});
        final List<String> row1 = Arrays.asList(new String[]{"Table cell 1_1", "Table cell 1_2", "Table cell 1_3", "Table cell 1_4", "Table cell 1_5", "Table cell 1_6"});
        final List<String> row2 = Arrays.asList(new String[]{"<rowspan>", "Table cell 2_2", "Table cell 2_3", "Table cell 2_4", "<colspan>", "<colspan>"});
        final List<String> row3 = Arrays.asList(new String[]{"Table cell 3_1", "Table cell 3_2", "Table cell 3_3", "Table cell 3_4", "Table cell 3_5", "Table cell 3_6"});
        final List<String> row4 = Arrays.asList(new String[]{"Table cell 4_1", "Table cell 4_2", "Table cell 4_3", "Table cell 4_4", "Table cell 4_5", "Table cell 4_6"});

        expectedRows = new ArrayList<List<String>>() {
            {
                add(header);
                add(row1);
                add(row2);
                add(row3);
                add(row4);
            }
        };

        DataTable expectedContent = mock(DataTable.class);
        when(expectedContent.raw()).thenReturn(expectedRows);

        ExpectedTableDefinition expectedTableDefinition = new ExpectedTableDefinition(expectedContent);

        seleniumTableService.compareTable(expectedTableDefinition, table);

        this.toString();
    }

    @Test(expected = AssertionError.class)
    public void testCompareTable_mismatch() throws Throwable {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement table = seleniumElementService.translateLocatorToWebElement("Table locator");

        List<List<String>> expectedRows = new ArrayList<List<String>>();
        final List<String> header = Arrays.asList(new String[]{"Table header 1", "Table header 2"});
        final List<String> row1 = Arrays.asList(new String[]{"Table cell 1.1", "Table cell 2"});
        final List<String> row2 = Arrays.asList(new String[]{"Table cell 3", "Table cell 4"});
        final List<String> row3 = Arrays.asList(new String[]{"Table cell 5", "Table cell 6"});

        expectedRows = new ArrayList<List<String>>() {
            {
                add(header);
                add(row1);
                add(row2);
                add(row3);
            }
        };

        DataTable expectedContent = mock(DataTable.class);
        when(expectedContent.raw()).thenReturn(expectedRows);

        ExpectedTableDefinition expectedTableDefinition = new ExpectedTableDefinition(expectedContent);

        seleniumTableService.compareTable(expectedTableDefinition, table);
    }

    @Test
    public void testCompareTable_rowIgnore() throws Throwable {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement table = seleniumElementService.translateLocatorToWebElement("Table locator");

        List<List<String>> expectedRows = new ArrayList<List<String>>();
        final List<String> row1 = Arrays.asList(new String[]{"Table cell 1", "Table cell 2"});
        final List<String> row3 = Arrays.asList(new String[]{"Table cell 5", "Table cell 6"});

        expectedRows = new ArrayList<List<String>>() {
            {
                add(row1);
                add(row3);
            }
        };

        DataTable expectedContent = mock(DataTable.class);
        when(expectedContent.raw()).thenReturn(expectedRows);

        ExpectedTableDefinition expectedTableDefinition = new ExpectedTableDefinition(expectedContent);
        expectedTableDefinition.getIgnoreRowsMatching().add(By.className("even"));
        expectedTableDefinition.getIgnoreRowsMatching().add(By.id("headerRow"));

        seleniumTableService.compareTable(expectedTableDefinition, table);
    }

    @Test
    public void testCompareTable_rowIncludeAndIgnore() throws Throwable {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement table = seleniumElementService.translateLocatorToWebElement("Table locator");

        List<List<String>> expectedRows = new ArrayList<List<String>>();
        final List<String> row3 = Arrays.asList(new String[]{"Table cell 5", "Table cell 6"});

        expectedRows = new ArrayList<List<String>>() {
            {
                add(row3);
            }
        };

        DataTable expectedContent = mock(DataTable.class);
        when(expectedContent.raw()).thenReturn(expectedRows);

        ExpectedTableDefinition expectedTableDefinition = new ExpectedTableDefinition(expectedContent);
        expectedTableDefinition.getIncludeOnlyRowsMatching().add(By.className("odd"));
        expectedTableDefinition.getIgnoreRowsMatching().add(By.id("row1"));

        seleniumTableService.compareTable(expectedTableDefinition, table);
    }

    @Test
    public void testCompareTable_columnSelection() throws Throwable {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement table = seleniumElementService.translateLocatorToWebElement("Table locator");

        List<List<String>> expectedRows = new ArrayList<List<String>>();
        final List<String> header = Arrays.asList(new String[]{"Table header 2"});
        final List<String> row1 = Arrays.asList(new String[]{"Table cell 2"});
        final List<String> row2 = Arrays.asList(new String[]{"Table cell 4"});
        final List<String> row3 = Arrays.asList(new String[]{"Table cell 6"});

        expectedRows = new ArrayList<List<String>>() {
            {
                add(header);
                add(row1);
                add(row2);
                add(row3);
            }
        };

        DataTable expectedContent = mock(DataTable.class);
        when(expectedContent.raw()).thenReturn(expectedRows);

        ExpectedTableDefinition expectedTableDefinition = new ExpectedTableDefinition(expectedContent);
        expectedTableDefinition.setMatchOnlyPassedInColumns(true);

        seleniumTableService.compareTable(expectedTableDefinition, table);
    }

    @Test(expected = AssertionError.class)
    public void testCompareTable_columnSelectionWithNonUniqueColuns() throws Throwable {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.COMPLEX_TABLE_TEST_PAGE_URL);
        WebElement table = seleniumElementService.translateLocatorToWebElement("Table locator");

        List<List<String>> expectedRows = new ArrayList<List<String>>();
        final List<String> header = Arrays.asList(new String[]{"Table header 1", "Table header", "", "Table header 5"});
        final List<String> row1 = Arrays.asList(new String[]{"Table cell 1_1", "Table cell 1_2", "Table cell 1_4", "Table cell 1_5"});
        final List<String> row2 = Arrays.asList(new String[]{"Table cell 2_1", "Table cell 2_2", "Table cell 2_4", "Table cell 2_5"});
        final List<String> row3 = Arrays.asList(new String[]{"Table cell 3_1", "Table cell 3_2", "Table cell 3_4", "Table cell 3_5"});
        final List<String> row4 = Arrays.asList(new String[]{"Table cell 4_1", "Table cell 4_2", "Table cell 4_4", "Table cell 4_5"});

        expectedRows = new ArrayList<List<String>>() {
            {
                add(header);
                add(row1);
                add(row2);
                add(row3);
                add(row4);
            }
        };

        DataTable expectedContent = mock(DataTable.class);
        when(expectedContent.raw()).thenReturn(expectedRows);

        ExpectedTableDefinition expectedTableDefinition = new ExpectedTableDefinition(expectedContent);
        expectedTableDefinition.setMatchOnlyPassedInColumns(true);

        seleniumTableService.compareTable(expectedTableDefinition, table);
    }

    @Test
    public void testCompareTable_columnSelectionWithNonUniqueColunsOutsideMatch() throws Throwable {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.COMPLEX_TABLE_TEST_PAGE_URL);
        WebElement table = seleniumElementService.translateLocatorToWebElement("Table locator");

        List<List<String>> expectedRows = new ArrayList<List<String>>();
        final List<String> header = Arrays.asList(new String[]{"Table header 1", "Table header 5"});
        final List<String> row1 = Arrays.asList(new String[]{"Table cell 1_1", "Table cell 1_5"});
        final List<String> row2 = Arrays.asList(new String[]{"<rowspan>", "<colspan>"});
        final List<String> row3 = Arrays.asList(new String[]{"Table cell 3_1", "Table cell 3_5"});
        final List<String> row4 = Arrays.asList(new String[]{"Table cell 4_1", "Table cell 4_5"});

        expectedRows = new ArrayList<List<String>>() {
            {
                add(header);
                add(row1);
                add(row2);
                add(row3);
                add(row4);
            }
        };

        DataTable expectedContent = mock(DataTable.class);
        when(expectedContent.raw()).thenReturn(expectedRows);

        ExpectedTableDefinition expectedTableDefinition = new ExpectedTableDefinition(expectedContent);
        expectedTableDefinition.setMatchOnlyPassedInColumns(true);

        seleniumTableService.compareTable(expectedTableDefinition, table);
    }

    @Test
    public void testCompareTable_withNameSpacing() throws Throwable {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_NAMESPACE_TEST_PAGE_URL);

        String unNamespacenizedString = SenBotReferenceService.NAME_SPACE_PREFIX + "Table cell 3";
        String namespacenizedString = SenBotContext.getSenBotContext().getReferenceService().namespaceString(unNamespacenizedString);

        List<List<String>> expectedRows = new ArrayList<List<String>>();
        final List<String> header = Arrays.asList(new String[]{"Table header 1", "Table header 2"});
        final List<String> row1 = Arrays.asList(new String[]{"Table cell 1", "Table cell 2"});
        final List<String> row2 = Arrays.asList(new String[]{unNamespacenizedString, "Table cell 4"});
        final List<String> row3 = Arrays.asList(new String[]{"Table cell 5", "Table cell 6"});

        expectedRows = new ArrayList<List<String>>() {
            {
                add(header);
                add(row1);
                add(row2);
                add(row3);
            }
        };

        DataTable expectedContent = mock(DataTable.class);
        when(expectedContent.raw()).thenReturn(expectedRows);

        ExpectedTableDefinition expectedTableDefinition = new ExpectedTableDefinition(expectedContent);
        expectedTableDefinition.setMatchOnlyPassedInColumns(true);

        WebDriver driver = SenBotContext.getSeleniumDriver();

        WebElement inputField = driver.findElement(By.id("textField"));
        inputField.sendKeys(namespacenizedString);
        seleniumElementService.findExpectedElement(By.xpath(".//*[@id='button']")).click();

        WebElement table = driver.findElement(By.id("exampleTable"));

        seleniumTableService.compareTable(expectedTableDefinition, table);

        this.toString();
    }

}
