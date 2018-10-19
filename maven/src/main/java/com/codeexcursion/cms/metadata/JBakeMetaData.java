/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.metadata;

import com.codeexcursion.cms.metadata.JBakeEnum;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author chris
 */
public class JBakeMetaData {
    
    public static Map<String, List<String>> get(String text) {
      Map<String, List<String>> metadata = null;
      if(isJBake(text)) {
        metadata = propertiesToMetadataMap(getProperties(getFrontMatter(text)));
      }
        
      return Optional.ofNullable(metadata).orElseGet(() -> new HashMap<String, List<String>>());
    }
    
    
    public static boolean isJBake(String text) {
      boolean returnValue = false;
      if(text == null) {
          return returnValue;
      }
      
      Pattern pattern = Pattern.compile("^" + JBakeEnum.DELIMETER + "$", Pattern.MULTILINE);
      Matcher matcher = pattern.matcher(text);
      
      returnValue = matcher.find();
        
      return returnValue;
    }
    
    private static String getFrontMatter(String text) {
      return text.split(JBakeEnum.DELIMETER.toString())[0];
    }
    
    private static Properties getProperties(String frontMatter) {
      Properties returnObject = new Properties();
      try {
        returnObject.load(new StringReader(frontMatter));
      } catch(IOException exception) {
          
      }
      return returnObject;
    }

    private static Map<String, List<String>> propertiesToMetadataMap(Properties properties) {
      Map<String, List<String>> returnObject = new HashMap<>(); 
      Set<String> names = properties.stringPropertyNames();
      
      for(String name : names) {
          returnObject.put(name, getValueAsList(properties.getProperty(name)));
      }
      
      return returnObject;
    }
    
    private static List<String> getValueAsList(String value) {        
        if(value == null || value.isEmpty()) {
            return new ArrayList<String>();
        } else {
            return new ArrayList<String>(Arrays.asList(value.split(",")));
        }
    }

    
}
