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
import code.excursion.ant.tasks.Copy;
import code.excursion.ant.tasks.Delete;
import code.excursion.ant.tasks.Mkdir;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class CopyEnvironmentConfigurationFiles extends Chore {
  final private EnvironmentEnum targetEnv;
  public CopyEnvironmentConfigurationFiles(
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
    final String templateBaseDir = getConfiguration().getString(ConfigurationEnum.CONFIG_TEMPLATE_BASE.name());
    final String templateTomcatBinDir = getConfiguration().getString(ConfigurationEnum.CONFIG_TEMPLATE_TOMCAT_BIN.name());
    final String templateTomcatConfDir = getConfiguration().getString(ConfigurationEnum.CONFIG_TEMPLATE_TOMCAT_CONF.name());
    final String templateTomcatLoggingDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_LIFERAY_ROOT_LOGGING_DIR.name());
    final String liferayDeployDir = getConfiguration().getString(ConfigurationEnum.CONFIG_TEMPLATE_LIFERAY_DEPLOY_DIR.name());
    final String liferayDir = getConfiguration().getString(ConfigurationEnum.CONFIG_TEMPLATE_LIFERAY_BASE.name());
    final String providedBinDir = getConfiguration().getString(ConfigurationEnum.PROVIDED_BIN_DIR.name());
    final String providedConfDir = getConfiguration().getString(ConfigurationEnum.PROVIDED_CONF_DIR.name());
    final String providedLiferayLoggingDir = getConfiguration().getString(ConfigurationEnum.PROVIDED_LIFERAY_LOGGING_DIR.name());
    final String providedPortalExtDir = getConfiguration().getString(ConfigurationEnum.PROVIDED_PORTAL_EXT_DIR.name());
    final String providedLicenseDir = getConfiguration().getString(ConfigurationEnum.PROVIDED_LICENSE_DIR.name());

    new Delete(getProject()).setDir(templateBaseDir).execute();
    new Mkdir(getProject()).setDir(templateBaseDir).execute();
    PrintUtil.info("Deleted and created " + templateBaseDir);
    
    new Mkdir(getProject()).setDir(templateTomcatBinDir).execute();
    PrintUtil.info("Created " + templateTomcatBinDir);
    new Mkdir(getProject()).setDir(templateTomcatConfDir).execute();
    PrintUtil.info("Created " + templateTomcatConfDir);
    new Mkdir(getProject()).setDir(liferayDeployDir).execute();
    PrintUtil.info("Created " + liferayDeployDir);

    new Copy(getProject())
      .setToDir(templateTomcatBinDir)
      .setFlatten(true)
      .addFileset(providedBinDir + "/" + targetEnv, "**/*")
      .execute();
    PrintUtil.info("Copied provided bin/" + targetEnv + " contents to " + templateTomcatBinDir);

    new Copy(getProject())
      .setToDir(templateTomcatConfDir)
      .setFlatten(true)
      .addFileset(providedConfDir + "/" + targetEnv, "**/*")
      .execute();
    PrintUtil.info("Copied provided conf/" + targetEnv + "  contents to" + templateTomcatConfDir);
    
    new Copy(getProject())
    .setToDir(templateTomcatLoggingDir)
    .setFlatten(true)
    .addFileset(providedLiferayLoggingDir ,  "**/*")
    .execute();
    PrintUtil.info("Copied provided conf/" + targetEnv + "  contents to" + templateTomcatConfDir);

  new Copy(getProject())
      .setToDir(liferayDir)
      .setFlatten(true)
      .addFileset(providedPortalExtDir + "/" + targetEnv, "**/*")
      .execute();
    PrintUtil.info("Copied provided portal-ext/" + targetEnv + "  contents to" + liferayDir);
    
    new Copy(getProject())
      .setToDir(liferayDeployDir)
      .setFlatten(true)
      .addFileset(providedLicenseDir + "/" + targetEnv, "**/*")
      .execute();
    PrintUtil.info("Copied provided license/" + targetEnv + "  contents to" + liferayDeployDir);
    
  }

  protected void writeNotice() {
    PrintUtil.success("Copying configuration files to template complete.");
  }

}
