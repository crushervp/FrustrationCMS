package com.codeexcursion.cms.content;

import com.vladsch.flexmark.ast.Document;


import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;


public class JekyllTest {


    @Test
    public void makeDocumentTest() {
        Path markdownFile = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");
        try {
            String text = new String(Files.readAllBytes(markdownFile));
            Jekyll jekyll = new Jekyll(text);
            Optional<Document> document = jekyll.getDocument();
            Assert.assertTrue("Markdown document should not be null", document.isPresent());
            Assert.assertTrue("Markdown document should have multiple lines", document.get().getLineCount() > 1);

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + markdownFile);
        }

    }

    @Test
    public void makeHTMLTest1() {
        Path markdownFile = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");
        try {
            String text = new String(Files.readAllBytes(markdownFile));
            Jekyll jekyll = new Jekyll(text);
            Optional<String> html = jekyll.getHTML();
            Assert.assertTrue("HTML should not be null", html.isPresent());
            Assert.assertTrue("Markdown document should have multiple lines", html.get().length() > 50);

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + markdownFile);
        }

    }
    
}