/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.excursion.cms.chores;

import code.excursion.cms.Chore;
import code.excursion.cms.ConfigurationEnum;
import code.excursion.cms.util.PrintUtil;
import code.excursion.ant.tasks.Copy;
import code.excursion.ant.tasks.Delete;
import code.excursion.ant.tasks.Mkdir;
import code.excursion.ant.tasks.Unzip;
import code.excursion.ant.tasks.Zip;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class ModifySOLRClient extends Chore {

  public ModifySOLRClient(
    Project project,
    Configuration configuration
  ) {
    super(project, configuration);
  }

  protected void perform() {
    final String packageName = getConfiguration().getString(ConfigurationEnum.SOLR_LPKG_FILE_NAME.name());
    final String warName = getConfiguration().getString(ConfigurationEnum.SOLR_WAR_FILE_NAME.name());
    final String liferayModifiedDir = getConfiguration().getString(ConfigurationEnum.LIFERAY_MODIFIED_DIR.name());
    final String sandboxDir =  getConfiguration().getString(ConfigurationEnum.LIFERAY_SANDBOX_DIR.name());
    final String lpkgDir =  getConfiguration().getString(ConfigurationEnum.LIFERAY_SANDBOX_LPKG_DIR.name());
    final String warDir =  getConfiguration().getString(ConfigurationEnum.LIFERAY_SANDBOX_WAR_DIR.name());
    final String newDir =  getConfiguration().getString(ConfigurationEnum.LIFERAY_APPS_NEW_DIR.name());
    final String providedSolrFileDir =  getConfiguration().getString(ConfigurationEnum.PROVIDED_SOLR_FILE_DIR.name());

    new Delete(getProject()).setDir(sandboxDir).execute();
    new Mkdir(getProject()).setDir(lpkgDir).execute();
    new Mkdir(getProject()).setDir(warDir).execute();
    new Mkdir(getProject()).setDir(newDir).execute();
    
    new Delete(getProject()).addFileset(newDir, "*" + packageName).execute();
    PrintUtil.info("Deleted SOLR LPKG in " + newDir);
    
    new Copy(getProject())
      .setToDir(sandboxDir)
      .addFileset(liferayModifiedDir, packageName)
      .execute();
    PrintUtil.info("Copied SOLR LPKG file to sandbox:  " + packageName);
    
    new Unzip(getProject())
      .setDest(lpkgDir)
      .setSrc(sandboxDir + "/" + packageName)
      .execute();
    PrintUtil.info("Extracted SOLR LPKG file to " + lpkgDir);
    
    new Unzip(getProject())
      .setDest(warDir)
      .setSrc(lpkgDir + "/" + warName)
      .execute();
    PrintUtil.info("Extracted SOLR WAR file to " + warDir);

    new Copy(getProject())
      .setToDir(warDir)
      .addFileset(providedSolrFileDir, "**/*")
      .execute();
    PrintUtil.info("Copied custom SOLR files.");

    new Delete(getProject()).addFileset(lpkgDir, warName).execute();
    PrintUtil.info("Delete original WAR file.");
    
    new Zip(getProject())
      .setDestFile(lpkgDir + "/Modified-" + warName)
      .addFileset(warDir, "**/*")
      .execute();
    PrintUtil.info("Zipped new SOLR WAR file.");

    new Zip(getProject())
      .setDestFile(newDir + "/Modified-" + packageName)
      .addFileset(lpkgDir, "**/*")
      .execute();
    PrintUtil.info("Zipped new SOLR LPKG file.");
    
  }

  protected void writeNotice() {
    PrintUtil.success("Modified SOLR CLient configuration successful.");
  }

}
