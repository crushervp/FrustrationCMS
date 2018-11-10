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

    public final static Optional<Document> getDocument(final String markdown) {
      return getDocument(new MutableDataSet(),markdown);
    }
    
    public final static Optional<Document> getDocument(final MutableDataSet options, final String markdown) {
        Optional.ofNullable(markdown).orElseThrow(IllegalArgumentException::new);        
        Optional.ofNullable(options).orElseThrow(IllegalArgumentException::new);        
        return Optional.ofNullable(
          Parser.builder(options).build().parse(markdown)
        );
    }


    public final static Optional<String> getHTML(final Document markdown) {
      return getHTML(new MutableDataSet(),markdown);
    }

    
    public final static Optional<String> getHTML(final MutableDataSet options, final Document markdown) {        
        Optional.ofNullable(options).orElseThrow(IllegalArgumentException::new);
        Optional.ofNullable(markdown).orElseThrow(IllegalArgumentException::new);
        return Optional.ofNullable(
          HtmlRenderer.builder(options).build().render(markdown)
        );
    }    


    public final static Optional<String> getHTML(final String markdown) {
      return getHTML(new MutableDataSet(),markdown);
    }
    
    
    public final static Optional<String> getHTML(final MutableDataSet options, final String markdown) {        
        Optional.ofNullable(options).orElseThrow(IllegalArgumentException::new);
        Optional.ofNullable(markdown).orElseThrow(IllegalArgumentException::new);
        final Optional<Document> optionalDocument = getDocument(options, markdown);
        return optionalDocument.flatMap(document -> getHTML(options, document));
    }    
    
}
