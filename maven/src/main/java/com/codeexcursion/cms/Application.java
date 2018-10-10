package com.codeexcursion.cms;

import com.codeexcursion.cms.service.GetConfiguration;
import com.codeexcursion.cms.util.PrintUtil;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

 // @Autowired
  //private TestDatabaseService testDatabase;
  
  @Override
  public void run(String... args) {
    Configuration configuration = new GetConfiguration().get();
    Project project = new Project();
    project.setName(configuration.getString(ConfigurationEnum.PROJECT.name()));
    if(args == null || args.length < 1) {
      usageInstructions();
      return;
    } 
    if("whatever".equalsIgnoreCase(args[0])) {
    } else {
      usageInstructions();
    }
  }

  private void usageInstructions() {
      System.out.println(
        "Usage:  You must provide either the \"local\" or the \"dsl\" parameter."
      );    
  }
  
  public static void main(String[] args) {
    PrintUtil.mainTitle("excursion cms");
    SpringApplication.run(Application.class, args);    
  }

}
