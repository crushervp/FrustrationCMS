package com.codeexcursion.cms.content;

import com.vladsch.flexmark.util.options.MutableDataSet;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import java.util.Optional;

import com.vladsch.flexmark.ast.Document;

public class ParseMarkdown {
    private HtmlRenderer renderer;
    private Document markdownDocument;

    public ParseMarkdown(MutableDataSet options, String markdown) {
        Optional.ofNullable(markdown).orElseThrow(IllegalArgumentException::new);
        options = Optional.ofNullable(options).orElseGet(MutableDataSet::new);
        Parser parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
        markdownDocument = parser.parse(markdown);
    }

    public Optional<Document> getDocument() {
        return Optional.ofNullable(markdownDocument);
    }

    public Optional<String> getHTML() {
        String returnValue = null;
        if(Optional.ofNullable(markdownDocument).isPresent()) {
          returnValue = renderer.render(markdownDocument);  
        }

        return Optional.ofNullable(returnValue);
    }


}