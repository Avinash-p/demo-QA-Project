package com.demoqa;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;

public class BaseTest 
{
    WebDriver driver; 
    Map<String,String> testData;
    @BeforeClass
    public void start(){
        driver = new ChromeDriver(new ChromeOptions());
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/text-box");
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }

    @DataProvider(name = "Test Data Provider")
    public Map<String, String> getTestData(){
        Map<String,String> temp = new HashMap<String, String>();
        String dataFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\TestData.xlsx";
        FileInputStream file;
        XSSFWorkbook excelFile;
        XSSFSheet excelSheet;
        try {
            dataFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\TestData.xlsx";
            file = new FileInputStream(dataFilePath);
            excelFile = new XSSFWorkbook(file);
            excelSheet = excelFile.getSheetAt(0);
            XSSFRow currentRow = excelSheet.getRow(0);
            int lastRow = excelSheet.getLastRowNum();
            for (int i=0; i<=lastRow;i++){
                currentRow = excelSheet.getRow(i);
                temp.put(getValue(currentRow.getCell(0)), getValue(currentRow.getCell(1)));
            }
            file.close();
            return temp;
        } catch (Exception e) {
            printOut("Execption found");
        }
        return null;
    }

    public String getValue(XSSFCell x){
        if (x.getCellType() == CellType.NUMERIC){
            return Double.toString(x.getNumericCellValue());
        } else if (x.getCellType() == CellType.STRING){
            return x.getStringCellValue();
        }
        return null;
    }

    @Test
    public void shouldAnswerWithTrue()
    {
        testData = getTestData();
        String testDataString = testData.toString();
        Assert.assertTrue( true );
        By textArea = By.xpath("(//textarea)[1]");
        driver.findElement(textArea).sendKeys(testDataString);
    }

    public void printOut(String x){
        By textArea = By.xpath("(//textarea)[1]");
        driver.findElement(textArea).sendKeys(x);
    }

    @Test
    public void shouldAnswerWithFalse(){
        Assert.assertFalse(false);
        System.out.println("hi");
    }
}