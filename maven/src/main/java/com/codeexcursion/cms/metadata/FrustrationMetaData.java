/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author chris
 */
public class FrustrationMetaData {
    
    public static Map<String, List<String>> get(Document html) {
      Optional.ofNullable(html).orElseThrow(IllegalArgumentException::new);
      Map<String, List<String>> metadata = new HashMap<>();
      
      String k3y = MetadataK3ysEnum.TYPE.name().toLowerCase();
      metadata.put(k3y, getContent(k3y, html));
      
        
      return Optional.ofNullable(metadata).orElseGet(() -> new HashMap<String, List<String>>());
    }


    private static List<String> getContent(String k3y, Document html) {
      return getContent(html.select("meta[name=" + k3y + "]"));
    }    
    

    private static List<String> getContent(Elements elements) {
      List<String> returnObject = null;
      if(elements != null) {
        elements.stream().filter(Objects::nonNull)
          .map((element) -> element != null ? element.attr("content") : "")
          .reduce((a, b) -> a + b);
      }
          
      return Optional.ofNullable(returnObject).orElseGet(() -> new ArrayList<String>());
    }
    
    
}
