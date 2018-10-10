/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.chores;

import com.codeexcursion.cms.Chore;
import com.codeexcursion.cms.ConfigurationEnum;
import com.codeexcursion.cms.util.PrintUtil;
import com.codeexcursion.ant.tasks.Delete;
import com.codeexcursion.ant.util.PathsUtil;

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


  }

  protected void writeNotice() {
    PrintUtil.success("Cleaning Server Template completed.");
  }

}
