/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.service;

import java.io.File;
import com.codeexcursion.ant.util.PathsUtil;
import com.codeexcursion.cms.ApplicationEnum;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.tools.ant.BuildException;

/**
 *
 * @author chris
 */
public class GetConfiguration {

  private final Configuration configuration;

  public GetConfiguration() {
    Configurations configurations = new Configurations();
    File file = PathsUtil.getFile(ApplicationEnum.CONFIGURATION_FILE.toString());
    try {
      configuration = configurations.properties(file);
    } catch (ConfigurationException exception) {
      throw new BuildException(exception);
    }
  }

  public Configuration get() {
    if(configuration == null) {
      throw new BuildException("Was unable to load configuration.");
    }
    return configuration;
  }

}
