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
        Document htmlDocument = Jsoup.parse(html);
        Elements terminals = htmlDocument.select(Tags.TERMINAL);
        Optional.ofNullable(terminals).ifPresent(HTML::handleTerminals);

        Elements codeBlocks = htmlDocument.select(Tags.CODE);
        Optional.ofNullable(codeBlocks).ifPresent(HTML::handleCodeBlocks);

        return Optional.ofNullable(htmlDocument.html());
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
        replace(currentElement, Tags.getContent(currentElement, cssClass));
    }    

    private final static void replace(Element currentElement, Element newElement) {
      Optional.of(newElement).ifPresent(currentElement::replaceWith);
    }

}
