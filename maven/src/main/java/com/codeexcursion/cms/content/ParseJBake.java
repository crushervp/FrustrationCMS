/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.content;

import com.codeexcursion.cms.JBakeEnum;
import com.codeexcursion.cms.metadata.JBakeMetaData;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author chris
 */
public class ParseJBake {
    private Map<String, List<String>> metadata;
    private String content;
    private String[] metadataAndContent;

    public ParseJBake(String text) {
      Optional.ofNullable(text).orElseThrow(this::throwNotJBake);
      if(isJBake(text)) {
        metadataAndContent = text.split(JBakeEnum.DELIMETER.toString());
        if(metadataAndContent == null || metadataAndContent.length < 2) {
          throw throwNotJBake();
        }
        content = metadataAndContent[1];
        metadata = JBakeMetaData.get(metadataAndContent[0]);
      } else {
        throw throwNotJBake();
      }
    }

    private IllegalArgumentException throwNotJBake() {
      return new IllegalArgumentException("JBake content must not be null and must contain a JBake header delimeted by '~~~~~~'.");
    }

    public Map<String, List<String>> getMetadata() {
      return Optional.ofNullable(metadata).orElseGet(() -> new HashMap<String, List<String>>());
    }
    
    public String getContent() {
      return Optional.ofNullable(content).orElseGet(() -> "");
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
    
}
