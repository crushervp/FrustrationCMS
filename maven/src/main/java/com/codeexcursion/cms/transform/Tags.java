package com.codeexcursion.cms.transform;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public final class Tags {

    public final static Optional<Element> getCaption(Element terminal) {
        Element captionNode = null;
        if (terminal != null) {
            String caption = terminal.attr("caption");
            if (caption != null && !caption.isEmpty()) {
                captionNode = new Element("span");
                captionNode.attr("class", "codeCaption");
                captionNode.appendChild(new TextNode(caption));
            }
        }
        return Optional.ofNullable(captionNode);
    }

    public final static Optional<Element> getContent(Element node, String cssClass) {
        Element returnNode = null;
        cssClass = Optional.ofNullable(cssClass).orElseGet(() -> "");
        if (node != null) {
            returnNode = new Element("pre");
            returnNode.attr("class", cssClass);
            returnNode.appendChild(new TextNode(node.html()));
        }
        return Optional.ofNullable(returnNode);
    }
    

    
}
