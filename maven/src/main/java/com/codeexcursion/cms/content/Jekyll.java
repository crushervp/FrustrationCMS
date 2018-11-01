/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.content;

import java.util.List;
import java.util.Map;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author chris
 */
public final class Jekyll {
    private final Optional<Map<String, List<String>>> metaData;
    private final Optional<Document> document;
    private final Optional<String> html;
    
    public Jekyll(final String markdown) {
      Optional.ofNullable(markdown).orElseThrow(IllegalArgumentException::new);
      MutableDataSet options = new MutableDataSet();
      options.set(Parser.EXTENSIONS, Arrays.asList(YamlFrontMatterExtension.create()));      
      
      Optional<Document> localDocument = Markdown.getDocument(options, markdown);
      if(localDocument.isPresent()) {
        AbstractYamlFrontMatterVisitor yamlVisitor = new AbstractYamlFrontMatterVisitor();
        yamlVisitor.visit(localDocument.get());
        metaData = Optional.ofNullable(yamlVisitor.getData());
        html = Markdown.getHTML(options, localDocument.get());
        
      } else {
        metaData = Optional.empty();
        html = Optional.empty();
      }
      
      document = localDocument;
      
      
    }

    public final Optional<Map<String, List<String>>> getMetaData() {
        return metaData;
    }

    public final Optional<Document> getDocument() {
        return document;
    }

    public Optional<String> getHTML() {
        return html;
    }
    
    
    
    
}
