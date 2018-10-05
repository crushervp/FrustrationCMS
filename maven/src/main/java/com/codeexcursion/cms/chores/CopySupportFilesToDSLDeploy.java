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
import code.excursion.ant.tasks.FixCRLF;
import code.excursion.ant.tasks.Mkdir;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class CopySupportFilesToDSLDeploy extends Chore {

  final private EnvironmentEnum targetEnv;

  public CopySupportFilesToDSLDeploy(
    final Project project,
    final Configuration configuration,
    final EnvironmentEnum targetEnv
  ) {
    super(project, configuration);
    if (targetEnv == null) {
      throw new IllegalArgumentException("Target environment may not be null.");
    }
    this.targetEnv = targetEnv;
  }

  protected void perform() {
    final String dslSupportDir = 
      getConfiguration().getString(ConfigurationEnum.DSL_SUPPORT_DIR.name())
      + "/" + targetEnv;

    final String dslInstallDir = 
      getConfiguration().getString(ConfigurationEnum.DSL_INSTALL_DIR.name())
      + "/" + targetEnv;

    final String providedInstallDir = 
      getConfiguration().getString(ConfigurationEnum.PROVIDED_INSTALL_DIR.name())
      + "/" + targetEnv;
    
    final String providedSupportDir = 
      getConfiguration().getString(ConfigurationEnum.PROVIDED_SUPPORT_DIR.name())
      + "/" + targetEnv;
    
    final String eol = "crlf";
    
    
    new Mkdir(getProject()).setDir(dslSupportDir).execute();
    new Mkdir(getProject()).setDir(dslInstallDir).execute();
    
    new FixCRLF(getProject())
      .setDestdir(dslInstallDir)
      .setSrcdir(providedInstallDir)
      .addFilename("*")
      .setEol(eol)
      .execute();
    PrintUtil.info("Copied DSL install documents to dsldeploy.");

    new FixCRLF(getProject())
      .setDestdir(dslSupportDir)
      .setSrcdir(providedSupportDir)
      .addFilename("*")
      .setEol(eol)
      .execute();
    PrintUtil.info("Copied DSL support documents to dsldeploy.");

  }

  protected void writeNotice() {
    PrintUtil.success("DSL Support files copy Complete for " + targetEnv + ".");
  }

}
