/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.content;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import java.util.Optional;
import com.vladsch.flexmark.ast.Document;

/**
 *
 * @author chris
 */
public final class Markdown {

    public final static Optional<Document> getDocument(final MutableDataSet options, final String markdown) {
        Optional.ofNullable(markdown).orElseThrow(IllegalArgumentException::new);        
        return Optional.ofNullable(
          Parser.builder(
          Optional.ofNullable(options)
            .orElseGet(MutableDataSet::new)).build().parse(markdown)
        );
    }
    
    public final static Optional<String> getHTML(final MutableDataSet options, final Document markdown) {        
        Optional.ofNullable(markdown).orElseThrow(IllegalArgumentException::new);
        return Optional.ofNullable(
          HtmlRenderer.builder(
            Optional.ofNullable(options)
              .orElseGet(MutableDataSet::new)).build().render(markdown)
        );
    }    


    public final static Optional<String> getHTML(final MutableDataSet options, final String markdown) {        
        Optional.ofNullable(markdown).orElseThrow(IllegalArgumentException::new);
        final Optional<Document> document = getDocument(options, markdown);
        if(document.isPresent()) {
            return getHTML(options, document.get());
        } else {
          return Optional.ofNullable(null);            
        }
    }    
    
}
