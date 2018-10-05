/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.excursion.cms.chores;

import code.excursion.cms.Chore;
import code.excursion.cms.ConfigurationEnum;
import code.excursion.cms.util.PrintUtil;
import code.excursion.ant.tasks.Delete;
import code.excursion.ant.util.PathsUtil;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import org.apache.commons.configuration2.Configuration;
import org.apache.tools.ant.Project;

/**
 *
 * @author chris
 */
public class Clean extends Chore {

  public Clean(Project project, Configuration configuration) {
    super(project, configuration);
  }

  protected void perform() {

    final String tomcatLibExtDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_TOMCAT_EXT.name());
    final String tomcatBinDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_TOMCAT_BIN.name());
    final String tomcatConfDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_TOMCAT_CONF.name());
    final String liferayBaseDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_LIFERAY_BASE.name());
    final String filesToDeployDir = getConfiguration().getString(ConfigurationEnum.FILES_TO_DEPLOY.name());
    final String liferayDeployDir = getConfiguration().getString(ConfigurationEnum.TEMPLATE_LIFERAY_DEPLOY_DIR.name());
    final String liferayAppsNewDir = getConfiguration().getString(ConfigurationEnum.LIFERAY_APPS_NEW_DIR.name());
    final String liferayAppsSandboxDir = getConfiguration().getString(ConfigurationEnum.LIFERAY_SANDBOX_DIR.name());

    new Delete(getProject()).addFileset(tomcatLibExtDir, "**/excursion*").execute();
    new Delete(getProject()).addFileset(tomcatLibExtDir, "**/cardcatalog*").execute();
    new Delete(getProject()).addFileset(tomcatLibExtDir, "**/LEMA*").execute();
    PrintUtil.info("Deleted excursion portlet service jar files from lib/ext.");

    new Delete(getProject()).addFileset(tomcatLibExtDir, "**/r2d2*").execute();
    PrintUtil.info("Deleted R2D2 jar files from lib/ext.");

    new Delete(getProject()).addFileset(tomcatBinDir, "setenv.sh").execute();
    PrintUtil.info("Deleted setenv.sh from Apache Tomcat bin directory.");

    new Delete(getProject()).addFileset(tomcatConfDir, "context.xml").execute();
    new Delete(getProject()).addFileset(tomcatConfDir, "server.xml").execute();
    PrintUtil.info("Deleted server and context files from Apache Tomcat conf directory.");

    new Delete(getProject()).addFileset(liferayBaseDir, "portal-ext.properties").execute();
    PrintUtil.info("Deleted portal-ext.properties from Liferay directory.");

    new Delete(getProject()).addFileset(filesToDeployDir, "**/*").execute();
    PrintUtil.info("Deleted files from files_to_deploy.");

    new Delete(getProject()).addFileset(liferayDeployDir, "**/*").execute();
    PrintUtil.info("Deleted files from Liferay deploy.");

    if (Files.exists(Paths.get(liferayAppsNewDir), LinkOption.NOFOLLOW_LINKS)) {
      new Delete(getProject()).addFileset(liferayAppsNewDir, "**/*").execute();
      PrintUtil.info("Deleted files from Liferay apps new directory.");
    }

    if (Files.exists(Paths.get(liferayAppsSandboxDir), LinkOption.NOFOLLOW_LINKS)) {
      new Delete(getProject()).setDir(liferayAppsSandboxDir).execute();
      PrintUtil.info("Deleted sandbox directory.");
    }

  }

  protected void writeNotice() {
    PrintUtil.success("Cleaning Server Template completed.");
  }

}
