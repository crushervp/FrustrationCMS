/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.service;

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
import org.jsoup.nodes.Document;

/**
 *
 * @author chris
 */
public class FrustrationMetaData {
    
    public static Map<String, List<String>> get(Document html) {
      Optional.ofNullable(html).orElseThrow(IllegalArgumentException::new);
      Map<String, List<String>> metadata = new HashMap<>();
      
      
        
      return Optional.ofNullable(metadata).orElseGet(() -> new HashMap<String, List<String>>());
    }
    
    

    
}
