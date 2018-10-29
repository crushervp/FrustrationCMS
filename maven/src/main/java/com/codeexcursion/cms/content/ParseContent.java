package com.codeexcursion.cms.content;

import java.util.Optional;

public class ParseContent {

  public ParseContent(ContentTypeEnum type, String text) {
    Optional.ofNullable(type).orElseThrow(IllegalArgumentException::new);
    Optional.ofNullable(text).orElseThrow(IllegalArgumentException::new);
  }

}