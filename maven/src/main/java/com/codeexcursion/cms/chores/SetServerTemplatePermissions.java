/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.excursion.cms.chores;

import code.excursion.cms.Chore;

import code.excursion.cms.util.PrintUtil;
import code.excursion.ant.tasks.Chmod;
import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class SetServerTemplatePermissions extends Chore {
  private final String baseDir;
  
  public SetServerTemplatePermissions(
    final Project project,
    final Configuration configuration,
    final String baseDir
  ) {
    super(project, configuration);
    if(baseDir == null || baseDir.trim().isEmpty()) {
      throw new IllegalArgumentException("baseDir may not be null");
    }
    this.baseDir=baseDir;
  }

  protected void perform() {

    final String confDir = baseDir + "/liferay/apache-tomcat/conf";
    final String patchingToolDir = baseDir + "/liferay/patching-tool";
    final String binDir = baseDir + "/liferay/apache-tomcat/bin";
    
    new Chmod(getProject())
      .setDir(baseDir)
      .setPerm("u+rwx,g+rx-w,o+rx-w")
      .setType("dir")
      .execute();
    PrintUtil.info("Set Liferay directory permissions.");
    
    new Chmod(getProject())
      .addFileset(baseDir, "**/*")
      .setPerm("u+rwx,g+rx-w,o+rx-w")
      .setType("file")
      .execute();
    PrintUtil.info("Set Liferay file permissions.");
    
    new Chmod(getProject())
      .addFileset(confDir, "*.xml")
      .setPerm("u+rw-x,g-rwx,o-rwx")
      .setType("file")
      .execute();
    PrintUtil.info("Set Tomcat configuration permissions.");
    
    new Chmod(getProject())
      .addFileset(patchingToolDir, "*.sh")
      .setPerm("u+rwx,g+r-wx,o+r-wx")
      .setType("file")
      .execute();
    PrintUtil.info("Set Liferay patching tool script permissions.");

    new Chmod(getProject())
      .addFileset(binDir, "*.sh")
      .setPerm("u+rwx,g+r-xw,o+r-xw")
      .setType("file")
      .execute();
    PrintUtil.info("Set Tomcat script permissions.");
    
  }

  protected void writeNotice() {
    PrintUtil.success("Setting server template permissons is complete.");
  }

}
