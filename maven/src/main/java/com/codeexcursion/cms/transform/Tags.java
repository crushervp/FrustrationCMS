package com.codeexcursion.cms.transform;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public final class Tags {
    public final static String TERMINAL = "finastacio";
    public final static String CODE= "kodo";
    public final static String CAPTION = "apudskribo";
    public final static String LANGUAGE = "lingvo";

    public final static Optional<Element> getCaption(Element customTag) {
        Optional.ofNullable(customTag).orElseThrow(IllegalArgumentException::new);
        Element captionNode = null;
        String caption = customTag.attr(CAPTION);
        if (caption != null && !caption.isEmpty()) {
            captionNode = new Element("span");
            captionNode.attr("class", "codeCaption");
            captionNode.appendChild(new TextNode(caption));
        }
        return Optional.ofNullable(captionNode);
    }

    public final static Element getContent(Element customTag, String cssClass) {
        Optional.ofNullable(cssClass).orElseThrow(IllegalArgumentException::new);
        Element returnNode = getContent(customTag);
        returnNode.attr("class", cssClass);
        returnNode.appendChild(new TextNode(customTag.html()));
        return returnNode;
    }
    
    public final static Element getContent(Element node) {
        Optional.ofNullable(node).orElseThrow(IllegalArgumentException::new);
        Element returnNode = new Element("pre");
        returnNode.appendChild(new TextNode(node.html()));
        return returnNode;
    }
    

    
}
