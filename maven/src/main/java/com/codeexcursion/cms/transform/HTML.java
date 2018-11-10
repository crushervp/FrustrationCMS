/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeexcursion.cms.transform;

import java.util.Optional;
import java.util.Objects;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;

/**
 *
 * @author chris Purpose of this class is to manipulate HTML documents to
 *         produce Frustration HTML Documents.
 */
public final class HTML {

    public static final Optional<String> transform(String html) {
        Optional.ofNullable(html).orElseThrow(IllegalArgumentException::new);

        Optional<Document> dom = Optional.ofNullable(Jsoup.parse(html));

        dom
          .flatMap((htmlDocument) -> Optional.ofNullable(htmlDocument.select(Tags.TERMINAL)))
          .ifPresent(HTML::handleTerminals);

        dom
          .flatMap((htmlDocument) -> Optional.ofNullable(htmlDocument.select(Tags.CODE)))
          .ifPresent(HTML::handleCodeBlocks);
        
        return dom.flatMap((htmlDocument) -> Optional.ofNullable(htmlDocument.html()));
    }


    private final static void handleTerminals(Elements terminals) {
      terminals.stream().filter(Objects::nonNull).forEach(HTML::replaceTerminal);
    }
  

    private final static void handleCodeBlocks(Elements codeBlocks) {
        codeBlocks.stream().filter(Objects::nonNull).forEach(HTML::replaceCodeBlocks);
    }

    private final static void replaceTerminal(Element currentElement) {
        replace(currentElement, "terminal");
    }

    private final static void replaceCodeBlocks(Element currentElement) {
        replace(currentElement, "prettifyprint");
    }

    private final static void replace(Element currentElement, String cssClass) {
        Tags.getCaption(currentElement).ifPresent(currentElement::before);
        currentElement.tagName("pre");
        currentElement.attr("class", cssClass);
        currentElement.removeAttr(Tags.CAPTION);
        currentElement.removeAttr(Tags.LANGUAGE);
    }    


}
