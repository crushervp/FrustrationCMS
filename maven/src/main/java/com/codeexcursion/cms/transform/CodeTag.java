package com.codeexcursion.cms.transform;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class CodeTag {

  public CodeTag(Element terminal) {
    Optional.ofNullable(terminal).orElseThrow(IllegalArgumentException::new);

    String caption = terminal.attr("caption");

    Element captionNode = new Element("span");
    captionNode.attr("class", "codeCaption");
    captionNode.appendChild(new TextNode(caption));
    terminal.before(captionNode);

    Element terminalNode = new Element("pre");
    terminalNode.attr("class", "prettifyprint");
    terminalNode.appendChild(new TextNode(terminal.html()));
    terminal.replaceWith(terminalNode);    
  }

}