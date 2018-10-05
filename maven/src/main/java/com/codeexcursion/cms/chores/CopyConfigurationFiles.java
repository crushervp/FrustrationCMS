/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.excursion.cms.chores;

import code.excursion.cms.ApplicationEnum;
import code.excursion.cms.Chore;
import code.excursion.cms.ConfigurationEnum;
import code.excursion.cms.util.PrintUtil;
import code.excursion.ant.tasks.Copy;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class CopyConfigurationFiles extends Chore {
  private ApplicationEnum targetEnv;
  public CopyConfigurationFiles(
    Project project,
    Configuration configuration
  ) {
    super(project, configuration);
  }

  protected void perform() {
    final String templateTomcatLibExtDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_TOMCAT_EXT.name());
    final String templateRootClassesDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_LIFERAY_ROOT_CLASSES_DIR.name());
    final String providedDir = getConfiguration().getString(ConfigurationEnum.PROVIDED_DIR.name());
    final String templateLiferayRootDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_LIFERAY_ROOT_DIR.name());
    final String providedRootFileDir = getConfiguration().getString(ConfigurationEnum.PROVIDED_ROOT_FILE_DIR.name());
    final String providedJavaSecurityDir = getConfiguration().getString(ConfigurationEnum.JAVA_SECURITY_DIR.name());

    new Copy(getProject())
      .setToDir(templateTomcatLibExtDir)
      .addFileset(providedJavaSecurityDir, "**/*")
      .execute(); 
    PrintUtil.info("Copied Java Security File to template.");
    
    new Copy(getProject())
      .setToDir(templateRootClassesDir)
      .addFileset(providedDir, "ehcache/*")
      .execute(); 
    PrintUtil.info("Copied Ehcache configuration files to template.");
    
    new Copy(getProject())
      .setToDir(templateLiferayRootDir)
      .addFileset(providedRootFileDir, "**/*")
      .execute(); 
    PrintUtil.info("Copied ROOT configuration files to template.");
    
  }

  protected void writeNotice() {
    PrintUtil.success("Copying configuration files to template complete.");
  }

}
