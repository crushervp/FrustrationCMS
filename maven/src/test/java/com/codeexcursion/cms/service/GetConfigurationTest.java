package com.codeexcursion.cms.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.configuration2.Configuration;

public class GetConfigurationTest {

@Before
public void setPwd() {
  File baseDir = Paths.get("src/test/artifacts").toFile();
  String path = baseDir.getAbsolutePath();
  System.setProperty("user.dir", baseDir.getAbsolutePath());
}

@Test
public void getTest() {
  Configuration configuration = new GetConfiguration().get();
  Assert.assertEquals("Title should be \"Test Configuration\"", "Test Configuration", configuration.getString("title"));
}


}