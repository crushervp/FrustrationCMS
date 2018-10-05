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
import code.excursion.ant.tasks.Checksum;
import code.excursion.ant.tasks.Chmod;
import code.excursion.ant.tasks.Copy;
import code.excursion.ant.tasks.Delete;
import code.excursion.ant.tasks.ExecTask;
import code.excursion.ant.tasks.Mkdir;
import code.excursion.ant.tasks.Replace;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class CopyInstallScripts extends Chore {
  private final EnvironmentEnum targetEnv;
  
  public CopyInstallScripts(
    final Project project,
    final Configuration configuration,
    final EnvironmentEnum targetEnv
  ) {
    super(project, configuration);
    if(targetEnv == null) {
      throw new IllegalArgumentException("Target environment may not be null.");
    }
    this.targetEnv=targetEnv;
  }

  protected void perform() {
    //Environment specific configuration template
    final String templateDir = getConfiguration().getString(ConfigurationEnum.INSTALL_SCRIPT_TEMPLATE_DIR.name());
    final String scriptDir = getConfiguration().getString(ConfigurationEnum.INSTALL_SCRIPT_DIR.name());
    final String scriptFile = getConfiguration().getString(ConfigurationEnum.INSTALL_SCRIPT_FILE_NAME.name());
    final String version = getConfiguration().getString(ConfigurationEnum.VERSION.name());
    final String mediaDir = getConfiguration().getString(ConfigurationEnum.DSL_MEDIA_DIR.name());  
    final String tomcatTGZ = mediaDir + "/excursion_" + version + "_tomcat.tgz";
    final String apacheTGZ = mediaDir + "/apache-ant-1.9.7-bin.tar.gz";
    final String configurationTGZ = mediaDir + "/" + targetEnv + "/excursion_" + version + "_configuration_" + targetEnv + ".tgz";
    final String deployScriptsTGZ = mediaDir + "/" + targetEnv  + "/deploy_scripts-" + version + "-" + targetEnv + ".tgz";
    final String checksumAlgorithm = "SHA-256";
    
    new Delete(getProject()).setDir(templateDir).execute();
    new Mkdir(getProject()).setDir(templateDir).execute();
    PrintUtil.info("Deleted and created " + templateDir);
    
    new Copy(getProject())
      .setToDir(templateDir)
      .addFileset(scriptDir, "*")
      .execute();    
    PrintUtil.info("Copied ant install scripts to " + templateDir);

    new Replace(getProject())
      .setFile(templateDir + "/" + scriptFile)
      .setToken("###SERVERTYPE###")
      .setValue(targetEnv.toString())
      .execute();    
    PrintUtil.info("Set environment for install script.");

    new Chmod(getProject())
      .addFileset(templateDir, "*.sh")
      .setPerm("u+rwx,g+r-xw,o+r-xw")
      .setType("file")
      .execute();
    PrintUtil.info("Set Install script permissions.");    
    
    new Checksum(getProject())
      .setToDir(templateDir)
      .setFile(tomcatTGZ)
      .setAlgorithm(checksumAlgorithm)
      .execute();
    PrintUtil.info("Generated checksum for Liferay Tomcat tar.");

    new Checksum(getProject())
      .setToDir(templateDir)
      .setFile(apacheTGZ)
      .setAlgorithm(checksumAlgorithm)
      .execute();
    PrintUtil.info("Generated checksum for apache ant tar.");

    new Checksum(getProject())
      .setToDir(templateDir)
      .setFile(configurationTGZ)
      .setAlgorithm(checksumAlgorithm)
      .execute();
    PrintUtil.info("Generated checksum for configuration tar.");
    
    new ExecTask(getProject())
      .setExecutable("tar")
      .addArgument("-cpzf")
      .addArgument(deployScriptsTGZ)
      .addArgument("-C")
      .addArgument(templateDir)
      .addArgument(".")
      .execute();
    PrintUtil.info("Tarred configuration for " + targetEnv + " into " + mediaDir);

  }

  protected void writeNotice() {
    PrintUtil.success("Packaging ant install scripts complete.");
  }

}
