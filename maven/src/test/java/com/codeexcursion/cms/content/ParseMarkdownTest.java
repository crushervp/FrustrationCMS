package com.codeexcursion.cms.content;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


public class ParseMarkdownTest {


    @Test
    public void makeDocumentTest() {
        Path markdownFile = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");
        try {
            String text = new String(Files.readAllBytes(markdownFile));
            ParseMarkdown parseMarkdown = new ParseMarkdown(null, text);
            Assert.assertTrue("Markdown document should not be null", parseMarkdown.getDocument().isPresent());

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + markdownFile);
        }

    }


}