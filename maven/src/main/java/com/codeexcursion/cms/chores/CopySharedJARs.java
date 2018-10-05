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
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class CopySharedJARs extends Chore {

  public CopySharedJARs(
    Project project,
    Configuration configuration
  ) {
    super(project, configuration);
  }

  protected void perform() {

    final String templateTomcatLibExtDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_TOMCAT_EXT.name());
    final String excursionSharedTargetDir = getConfiguration().getString(ConfigurationEnum.excursion_SHARED_TARGET.name());
    final String r2d2TargetDir = getConfiguration().getString(ConfigurationEnum.R2D2_TARGET.name());
    final String globalParametersTargetDir = getConfiguration().getString(ConfigurationEnum.GLOBAL_PARAMETERS_TARGET.name());
    final String cardCatalogTargetDir = getConfiguration().getString(ConfigurationEnum.CARD_CATALOG_TARGET.name());
    final String lemaTargetDir = getConfiguration().getString(ConfigurationEnum.LEMA_TARGET.name());
    final String webServiceClientsDir = getConfiguration().getString(ConfigurationEnum.WEB_SERVICE_CLIENTS_DIR.name());
    final String providedJarsDir = getConfiguration().getString(ConfigurationEnum.PROVIDED_JARS_DIR.name());
    final String filenamePattern = getConfiguration().getString(ConfigurationEnum.JAR_EXTENSION.name());

    new Copy(getProject())
      .setToDir(templateTomcatLibExtDir)
      .addFileset(excursionSharedTargetDir, filenamePattern)
      .execute();    
    PrintUtil.info("Copied excursion Shared JAR.");
    
    new Copy(getProject())
      .setToDir(templateTomcatLibExtDir)
      .addFileset(r2d2TargetDir, filenamePattern)
      .execute();
    PrintUtil.info("Copied R2D2 JAR.");
    
    new Copy(getProject())
      .setToDir(templateTomcatLibExtDir)
      .addFileset(globalParametersTargetDir, filenamePattern)
      .execute();
    PrintUtil.info("Copied excursion Global Parameters Service JAR.");
    
    new Copy(getProject())
      .setToDir(templateTomcatLibExtDir)
      .addFileset(cardCatalogTargetDir, filenamePattern)
      .execute();
    PrintUtil.info("Copied Card Catalog Service JAR.");
    
    new Copy(getProject())
      .setToDir(templateTomcatLibExtDir)
      .addFileset(lemaTargetDir, filenamePattern)
      .execute();
    PrintUtil.info("Copied LEMA Service JAR.");
    
    new Copy(getProject())
      .setToDir(templateTomcatLibExtDir)
      .setFlatten(true)
      .addFileset(webServiceClientsDir, "**/target/" + filenamePattern)
      .execute();
    PrintUtil.info("Copied Web Service Clients JARs.");
    
    new Copy(getProject())
      .setToDir(templateTomcatLibExtDir)
      .addFileset(providedJarsDir, "**/*")
      .execute();
    PrintUtil.info("Copied provided JARs.");
  }

  protected void writeNotice() {
    PrintUtil.success("Copying Shared JAR files complete.");
  }

}
