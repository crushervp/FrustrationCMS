/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.excursion.cms.chores;

import java.util.EnumSet;
import code.excursion.cms.Chore;
import code.excursion.cms.ConfigurationEnum;
import code.excursion.cms.EnvironmentEnum;

import code.excursion.cms.util.PrintUtil;
import code.excursion.ant.tasks.Copy;
import code.excursion.ant.tasks.Delete;
import code.excursion.ant.tasks.ExecTask;
import code.excursion.ant.tasks.Mkdir;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;
import code.excursion.ant.tasks.FileCount;

/**
 *
 * @author chris
 */
public class DSLDeploy extends Chore {

  public DSLDeploy(
    Project project,
    Configuration configuration
  ) {
    super(project, configuration);
  }

  protected void perform() {
    final String antTarFile = getConfiguration().getString(ConfigurationEnum.APACHE_ANT_FILE.name());
    final String templateBase = getConfiguration().getString(ConfigurationEnum.TEMPLATE_BASE.name());
    final String configTemplateBase = getConfiguration().getString(ConfigurationEnum.CONFIG_TEMPLATE_BASE.name());
    final String installScriptTemplateBase = getConfiguration().getString(ConfigurationEnum.INSTALL_SCRIPT_TEMPLATE_DIR.name());
    final String dslDeploy = getConfiguration().getString(ConfigurationEnum.DSL_DEPLOY_DIR.name());
    final String dslMedia = getConfiguration().getString(ConfigurationEnum.DSL_MEDIA_DIR.name());
    final String version = getConfiguration().getString(ConfigurationEnum.VERSION.name());
    final String tomcatTarFile = dslMedia + "/excursion_" + version + "_tomcat.tgz";
    final String filesToDeploy = getConfiguration().getString(ConfigurationEnum.FILES_TO_DEPLOY.name());
    final int filesToDeployCount = Integer.parseInt(getConfiguration().getString(ConfigurationEnum.FILES_TO_DEPLOY_COUNT.name()));

    new Delete(getProject()).setDir(dslDeploy).execute();
    PrintUtil.info("Deleted dsldeploy directory.");
    new Mkdir(getProject()).setDir(dslDeploy).execute();
    PrintUtil.info("Created dsldeploy directory.");

    
    new Clean(getProject(), getConfiguration()).execute();
    new CopySharedJARs(getProject(), getConfiguration()).execute();    

    new ModifyDynamicDataList(getProject(), getConfiguration()).execute();
    new ModifySOLRClient(getProject(), getConfiguration()).execute();
    new CopyFilesToDeploy(getProject(), getConfiguration()).execute();
    
    new FileCount(getProject())
    .setDir(filesToDeploy)
    .setCount(getConfiguration().getLong(ConfigurationEnum.FILES_TO_DEPLOY_COUNT.name()))
    .execute();
    PrintUtil.info("Counted deployable files.");    
    
    new CopyConfigurationFiles(getProject(), getConfiguration()).execute();
    new SetServerTemplatePermissions(getProject(), getConfiguration(), templateBase).execute();

    EnumSet.allOf(EnvironmentEnum.class)
      .stream().filter(environmentEnum -> !environmentEnum.equals(EnvironmentEnum.LOCAL))
      .forEach(environmentEnum -> {
        new DSLEnvironment(getProject(), getConfiguration(), environmentEnum).execute();
      });
      

    new Copy(getProject())
      .setToDir(dslMedia)
      .addFileset(".", antTarFile)
      .execute(); 
    PrintUtil.info("Tarred Tomcat for into " + dslMedia);
    
    new ExecTask(getProject())
      .setExecutable("tar")
      .addArgument("-czf")
      .addArgument(tomcatTarFile)
      .addArgument("-C")
      .addArgument(templateBase)
      .addArgument(".")
      .execute();
    PrintUtil.info("Tarred Tomcat into " + dslMedia);
    
    EnumSet.allOf(EnvironmentEnum.class)
      .stream().filter(environmentEnum -> !environmentEnum.equals(EnvironmentEnum.LOCAL))
      .forEach(environmentEnum -> {
        new CopyInstallScripts(getProject(), getConfiguration(), environmentEnum).execute();
      });

    new Delete(getProject()).setDir(configTemplateBase).execute();
    PrintUtil.info("Deleted configurationtemplate directory.");    
    
    new Delete(getProject()).setDir(installScriptTemplateBase).execute();
    PrintUtil.info("Deleted install script template directory.");    
  }

  protected void writeNotice() {
    PrintUtil.success("DSL Deploy Complete.");
  }

}
