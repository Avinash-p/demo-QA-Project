package com.demoqa;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LandingPage extends BaseTest{
    @Test
    public void checkTitle(){
        System.out.println(driver.getTitle());
        Assert.assertTrue(driver.getTitle().equals("Google"));
    }
}