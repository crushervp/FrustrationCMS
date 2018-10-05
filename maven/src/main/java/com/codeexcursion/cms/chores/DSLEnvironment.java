/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.excursion.cms.chores;

import code.excursion.cms.Chore;
import code.excursion.cms.ConfigurationEnum;
import code.excursion.cms.EnvironmentEnum;

import code.excursion.cms.util.PrintUtil;
import code.excursion.ant.tasks.ExecTask;
import code.excursion.ant.tasks.Mkdir;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class DSLEnvironment extends Chore {

  private EnvironmentEnum targetEnv;

  public DSLEnvironment(
    Project project,
    Configuration configuration,
    EnvironmentEnum targetEnv
  ) {
    super(project, configuration);
    if (targetEnv == null) {
      throw new IllegalArgumentException("Target environment may not be null.");
    }
    this.targetEnv = targetEnv;
  }

  protected void perform() {
    final String templateBase = getConfiguration().getString(ConfigurationEnum.CONFIG_TEMPLATE_BASE.name());
    final String dslMediaDir = getConfiguration().getString(ConfigurationEnum.DSL_MEDIA_DIR.name())
      + "/" + targetEnv;
    final String version = getConfiguration().getString(ConfigurationEnum.VERSION.name());
    final String configurationFile = dslMediaDir + "/excursion_" + version + "_configuration_" + targetEnv + ".tgz";
    
    new Mkdir(getProject()).setDir(dslMediaDir).execute();
    
    new CopySupportFilesToDSLDeploy(getProject(), getConfiguration(), targetEnv).execute();
    new CopyEnvironmentConfigurationFiles(getProject(), getConfiguration(), targetEnv).execute();

    new ExecTask(getProject())
      .setExecutable("tar")
      .addArgument("-czf")
      .addArgument(configurationFile)
      .addArgument("-C")
      .addArgument(templateBase)
      .addArgument(".")
      .execute();
    PrintUtil.info("Tarred configuration for " + targetEnv + " into " + dslMediaDir);
    

    
  }

  protected void writeNotice() {
    PrintUtil.success("DSL Environment Complete for " + targetEnv + ".");
  }

}
