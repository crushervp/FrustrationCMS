/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.excursion.cms;

import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public abstract class Chore {
  private final Project project;
  private final Configuration configuration;
  
  public Chore(
    Project project,
    Configuration configuration
  ) {
    if(project == null) {
      throw new IllegalArgumentException("Project may not be null.");
    }
    if(configuration == null) {
      throw new IllegalArgumentException("Configuration may not be null.");
    }
    this.project=project;
    this.configuration=configuration;
  }
  
  
  public void execute() {
    perform();
    writeNotice();
  }

  abstract protected void perform();
  abstract protected void writeNotice();

  public Project getProject() {
    return project;
  }

  public Configuration getConfiguration() {
    return configuration;
  }
  
  
  
}
