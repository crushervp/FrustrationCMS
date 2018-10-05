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
public class LocalDeploy extends Chore {

  public LocalDeploy(
    Project project,
    Configuration configuration
  ) {
    super(project, configuration);
  }

  protected void perform() {
    final String localDeployDir = getConfiguration().getString(ConfigurationEnum.LOCAL_DEPLOY_DIR.name());
    final String filesToDeploy = getConfiguration().getString(ConfigurationEnum.FILES_TO_DEPLOY.name());
    final String templateBase = getConfiguration().getString(ConfigurationEnum.TEMPLATE_BASE.name());
    final String configTemplateBase = getConfiguration().getString(ConfigurationEnum.CONFIG_TEMPLATE_BASE.name());
  
    new Delete(getProject()).setDir(localDeployDir).execute();
    PrintUtil.info("Deleted local deploy directory.");
    new Mkdir(getProject()).setDir(localDeployDir).execute();
    PrintUtil.info("Created local deploy directory.");
    
    new Clean(getProject(), getConfiguration()).execute();
    new CopySharedJARs(getProject(), getConfiguration()).execute();
    new CopyConfigurationFiles(getProject(), getConfiguration()).execute();
    new ModifyDynamicDataList(getProject(), getConfiguration()).execute();
    new CopyFilesToDeploy(getProject(), getConfiguration()).execute();
    new CopyEnvironmentConfigurationFiles(getProject(), getConfiguration(), EnvironmentEnum.LOCAL).execute();

    new Delete(getProject()).addFileset(filesToDeploy, "*Solr*").execute();
    PrintUtil.info("Deleted SOLR LPKG from deployable files.");
    new Delete(getProject()).addFileset(filesToDeploy, "*Ehcache*").execute();
    PrintUtil.info("Deleted Ehcache LPKG from deployable files.");

    new Copy(getProject())
      .setToDir(localDeployDir)
      .addFileset(templateBase, "**/*")
      .execute();
    PrintUtil.info("Copied servertemplate contents to localdeploy.");
    
    new Copy(getProject())
      .setToDir(localDeployDir)
      .addFileset(configTemplateBase, "**/*")
      .execute();
    PrintUtil.info("Copied configurationtemplate contents to localdeploy.");
    
    new SetServerTemplatePermissions(getProject(), getConfiguration(), localDeployDir).execute();
    
    new Delete(getProject()).setDir(configTemplateBase).execute();
    PrintUtil.info("Deleted configurationtemplate directory.");
  }

  protected void writeNotice() {
    PrintUtil.success("Local Deploy Complete.");
  }

}
