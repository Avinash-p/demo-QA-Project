package com.demoqa;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;

import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ExtentReportListener extends TestListenerAdapter {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static ExtentSparkReporter htmlReporter;
    public static ExtentReports extent;
    public static ExtentTest test;
    
    // Getter and setter for the driver (if needed)
    public static ThreadLocal<WebDriver> getDriver() {
    return driver;
    }

    public static void setDriver(WebDriver driver) {
        ExtentReportListener.getDriver().set(driver);
    }
    @Override
    public void onStart(ITestContext testContext) {
        String timeStamp = new Date().toString().replace(":", "_").replace(" ", "_");
        String repName = "TestReport_" + timeStamp + ".html";

        htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/" + repName);//specify location of the report
        htmlReporter.config().setDocumentTitle("DemoQA Automation Report"); // Title of report
        htmlReporter.config().setReportName("DemoQA Functional Testing"); // name of the report
        htmlReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Host name", "localhost");
        extent.setSystemInfo("Environemnt", "QA");
        extent.setSystemInfo("user", "pavan");
    }


    @Override
    public void onTestSuccess(ITestResult tr) {
        test = extent.createTest(tr.getName()); // create new entry in the report
        test.log(Status.PASS, MarkupHelper.
        createLabel(tr.getName(), ExtentColor.GREEN)); // send the passed information to the report with GREEN status
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        test = extent.createTest(tr.
        getName()); // create new entry in the report

        test.log(Status.FAIL, MarkupHelper.createLabel(tr.getName(), ExtentColor.RED)); 
        test.fail(tr.getThrowable());

        // Capture and attach screenshot ONLY on failure
        try {
            String screenshotPath = captureScreenshot(tr.getName());
            test.addScreenCaptureFromPath(screenshotPath); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        test = extent.createTest(tr.getName()); // create new entry in the report
        test.log(Status.SKIP, MarkupHelper.createLabel(tr.getName(), ExtentColor.ORANGE));
    }

    @Override
    public void onFinish(ITestContext testContext) {
        extent.flush();
    }

    // Method to capture screenshot
    public static String captureScreenshot(String testName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver; // Cast driver
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/screenshots/" + testName + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        return destination; 
    }
    
    public static void log(ITestResult result, Status status, String message) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName()); // Get the current test
        test.log(status, message);
    }

    public static void fail(ITestResult result, String message) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName()); // Get the current test
        test.fail(message);
    }

}