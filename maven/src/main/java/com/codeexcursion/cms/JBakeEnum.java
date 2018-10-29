/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms;

/**
 *
 * @author chris
 */
public enum JBakeEnum {
    DELIMETER("~~~~~~");
    
    
    private JBakeEnum(final String value) {
        this.value=value;
    }
    private String value;
    
    @Override
    public String toString() {
        if(value != null && !value.isEmpty()) {
          return value;
        } else {
          return super.toString();
        }
    }
}
