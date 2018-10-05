/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.excursion.cms;

/**
 *
 * @author chris
 */
public enum ApplicationEnum {
  CONFIGURATION_FILE("cms.conf");
  
  private String value;

  private ApplicationEnum() {
    this.value=this.name();
  }

  private ApplicationEnum(String value) {
    this.value=value;
  }
  
  public String toString() {
    return value;
  }
  
}
