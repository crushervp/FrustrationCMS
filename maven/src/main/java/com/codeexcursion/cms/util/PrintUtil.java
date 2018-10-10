/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.util;

/**
 *
 * @author chris
 */
public class PrintUtil {
  
  public static void mainTitle(String message) {
    System.out.println("");
    System.out.print(ColorCodes.BLUE);
    System.out.print(ColorCodes.WHITE_BACKGROUND);
    System.out.print(message);
    System.out.println(ColorCodes.RESET);
    System.out.println("");
  }

  
  public static void success(String message) {
    System.out.print(ColorCodes.GREEN);
    System.out.print(message);
    System.out.println(ColorCodes.RESET);
  }
  
  public static void info(String message) {
    System.out.print(ColorCodes.CYAN);
    System.out.print(message);
    System.out.println(ColorCodes.RESET);
  }
  
  public static void warning(String message) {
    System.out.print(ColorCodes.YELLOW);
    System.out.print(message);
    System.out.println(ColorCodes.RESET);
  }
  
  public static void error(String message) {
    System.out.print(ColorCodes.RED);
    System.out.print(message);
    System.out.println(ColorCodes.RESET);
  }
}
