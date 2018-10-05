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
public enum EnvironmentEnum {
  LOCAL, DEV, FT1, FT2, RTN, PROD, PROD_INDY("PROD-Indy");
  
  private String value;

  private EnvironmentEnum() {
    this.value=this.name();
  }

  private EnvironmentEnum(String value) {
    this.value=value;
  }
  
  public String toString() {
    return value;
  }
  
}
