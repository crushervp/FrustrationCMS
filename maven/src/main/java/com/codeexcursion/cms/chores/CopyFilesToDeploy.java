/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.excursion.cms.chores;

import code.excursion.cms.Chore;
import code.excursion.cms.ConfigurationEnum;
import code.excursion.ant.tasks.Copy;
import code.excursion.cms.util.PrintUtil;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class CopyFilesToDeploy extends Chore {

  public CopyFilesToDeploy(
    Project project,
    Configuration configuration
  ) {
    super(project, configuration);
  }

  protected void perform() {

    final String filesToDeploy = getConfiguration().getString(ConfigurationEnum.FILES_TO_DEPLOY.name());
    final String codeDir = getConfiguration().getString(ConfigurationEnum.CODE_DIR.name());
    final String themeDir = getConfiguration().getString(ConfigurationEnum.THEME_DIR.name());
    final String unmodifiedDir = getConfiguration().getString(ConfigurationEnum.LIFERAY_UNMODIFIED_DIR.name());
    final String appsNewDir = getConfiguration().getString(ConfigurationEnum.LIFERAY_APPS_NEW_DIR.name());
    final String filenamePattern = "**/target/*.war";

    PrintUtil.info("Following operations copy to " + filesToDeploy + " from " + codeDir);

    new Copy(getProject())
      .setToDir(filesToDeploy)
      .setFlatten(true)
      .addFileset(codeDir, filenamePattern)
      .execute();    
    PrintUtil.info("Copied Portlet WAR files");
    
    new Copy(getProject())
      .setToDir(filesToDeploy)
      .setFlatten(true)
      .addFileset(themeDir, filenamePattern)
      .execute();    
    PrintUtil.info("Copied Theme WAR files");
    
    new Copy(getProject())
      .setToDir(filesToDeploy)
      .setFlatten(true)
      .addFileset(unmodifiedDir, "*")
      .execute();  
    PrintUtil.info("Copied unmodified Liferay LPKG files.");
    
    new Copy(getProject())
      .setToDir(filesToDeploy)
      .setFlatten(true)
      .addFileset(appsNewDir, "*")
      .execute();  
    PrintUtil.info("Copied modified Liferay LPKG files");

  }

  protected void writeNotice() {
    PrintUtil.success("Copying Porltet and theme WAR files to files_to_deploy complete.");
  }

}
