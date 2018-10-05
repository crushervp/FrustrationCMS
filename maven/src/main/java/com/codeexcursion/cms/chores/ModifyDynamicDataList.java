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
public class ModifyDynamicDataList extends Chore {

  public ModifyDynamicDataList(
    Project project,
    Configuration configuration
  ) {
    super(project, configuration);
  }

  protected void perform() {

    final String packageName = getConfiguration().getString(ConfigurationEnum.DDL_LPKG_FILE_NAME.name());
    final String warName = getConfiguration().getString(ConfigurationEnum.DDL_WAR_FILE_NAME.name());
    String sourceDir = getConfiguration().getString(ConfigurationEnum.LIFERAY_MODIFIED_DIR.name());
    final String sandboxDir =  getConfiguration().getString(ConfigurationEnum.LIFERAY_SANDBOX_DIR.name());
    final String lpkgDir =  getConfiguration().getString(ConfigurationEnum.LIFERAY_SANDBOX_LPKG_DIR.name());
    final String warDir =  getConfiguration().getString(ConfigurationEnum.LIFERAY_SANDBOX_WAR_DIR.name());
    final String newDir =  getConfiguration().getString(ConfigurationEnum.LIFERAY_APPS_NEW_DIR.name());
    final String providedDDLFileDir =  getConfiguration().getString(ConfigurationEnum.PROVIDED_DDL_FILE_DIR.name());

    
    new Delete(getProject()).setDir(sandboxDir).execute();
    new Mkdir(getProject()).setDir(lpkgDir).execute();
    new Mkdir(getProject()).setDir(warDir).execute();
    new Mkdir(getProject()).setDir(newDir).execute();
    
    new Delete(getProject()).addFileset(newDir, "*" + packageName).execute();
    PrintUtil.info("Deleted DDL LPKG in " + newDir);

    new Copy(getProject())
      .setToDir(sandboxDir)
      .addFileset(sourceDir, packageName)
      .execute();
    PrintUtil.info("Copied DDL LPKG to sandbox.");
    
    new Unzip(getProject())
      .setDest(lpkgDir)
      .setSrc(sandboxDir + "/" + packageName)
      .execute();
    PrintUtil.info("Extracted DDL LPKG File.");
        
    new Unzip(getProject())
      .setDest(warDir)
      .setSrc(lpkgDir + "/" + warName)
      .execute();
    PrintUtil.info("Extracted DDL WAR file.");

    new Copy(getProject())
      .setToDir(warDir)
      .addFileset(providedDDLFileDir, "**/*")
      .execute();
    PrintUtil.info("Copied DDL custom files.");

    new Delete(getProject()).addFileset(lpkgDir, warName).execute();
    PrintUtil.info("Delete original DDL WAR file.");
    
    new Zip(getProject())
      .setDestFile(lpkgDir + "/Modified-" + warName)
      .addFileset(warDir, "**/*")
      .execute();
    PrintUtil.info("Zipped new DDL WAR file.");

    new Zip(getProject())
      .setDestFile(newDir + "/Modified-" + packageName)
      .addFileset(lpkgDir, "**/*")
      .execute();
    PrintUtil.info("Zipped new DDL LPKG file.");
        
  }

  protected void writeNotice() {
    PrintUtil.success("Modified Dynamic Data List to change display category complete.");
  }

}
