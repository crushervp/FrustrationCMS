package com.codeexcursion.cms.transform;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public final class Tags {

    public final static Optional<Element> getCaption(Element terminal) {
        Optional.ofNullable(terminal).orElseThrow(IllegalArgumentException::new);
        Element captionNode = null;
        String caption = terminal.attr("caption");
        if (caption != null && !caption.isEmpty()) {
            captionNode = new Element("span");
            captionNode.attr("class", "codeCaption");
            captionNode.appendChild(new TextNode(caption));
        }
        return Optional.ofNullable(captionNode);
    }

    public final static Element getContent(Element node, String cssClass) {
        Optional.ofNullable(node).orElseThrow(IllegalArgumentException::new);
        cssClass = Optional.ofNullable(cssClass).orElseGet(() -> "");
        Element returnNode = new Element("pre");
        returnNode.attr("class", cssClass);
        returnNode.appendChild(new TextNode(node.html()));
        return returnNode;
    }
    

    
}
