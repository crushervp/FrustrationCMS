package com.codeexcursion.cms.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.configuration2.Configuration;

public class GetConfigurationTest {
  private String origWd;
  private static final String USER_DIR = "user.dir";
@Before
public void setCwd() {
  origWd = System.getProperty(USER_DIR);  
  File baseDir = Paths.get("src/test/artifacts").toFile();
  String path = baseDir.getAbsolutePath();
  System.setProperty(USER_DIR, baseDir.getAbsolutePath());
}

@Test
public void getTest() {
  Configuration configuration = new GetConfiguration().get();
  Assert.assertEquals("Title should be \"Test Configuration\"", "Test Configuration", configuration.getString("title"));
}

@After
public void resetCwd() {  
  System.setProperty(USER_DIR, origWd);
}

}