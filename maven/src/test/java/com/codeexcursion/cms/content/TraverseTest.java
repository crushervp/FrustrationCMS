package com.codeexcursion.cms.content;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

public class TraverseTest {


    @Test
    public final void visitTest() {
        Path contentDir = Paths.get("src/test/artifacts/content");

      ContentFileVisitor visitor = 
        new ContentFileVisitor(Paths.get("src/test/artifacts/contenttarget"), contentDir);
      try {
      Files.walkFileTree(contentDir, visitor);
      } catch (IOException exception) {
          Assert.fail("File tree walk failed");
          exception.printStackTrace();
      }
    }
}