package com.demoqa;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LandingPage extends BaseTest{
    @Test
    public void checkTitle(){
        reportLog("Google web page title = " + driver.getTitle());
        Assert.assertFalse(driver.getTitle().equals("Google"));
    }
}
