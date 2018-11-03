package com.codeexcursion.cms.content;

import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;


import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;


public class MarkdownTest {


    @Test
    public void makeDocumentTest() {
        Path markdownFile = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");
        try {
            String text = new String(Files.readAllBytes(markdownFile));
            Optional<Document> document = Markdown.getDocument(null, text);
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
            Optional<String> html = Markdown.getHTML(null, Markdown.getDocument(null, text).get());
            Assert.assertTrue("HTML should not be null", html.isPresent());
            Assert.assertTrue("Markdown document should have multiple lines", html.get().length() > 50);

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + markdownFile);
        }

    }
    
    @Test
    public void makeHTMLTest2() {
        Path markdownFile = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");
        try {
            String text = new String(Files.readAllBytes(markdownFile));
            Optional<String> html = Markdown.getHTML(null, text);
            Assert.assertTrue("HTML should not be null", html.isPresent());
            Assert.assertTrue("Markdown document should have multiple lines", html.get().length() > 50);

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + markdownFile);
        }

    }
    

    @Test
    public void codeBlockTest1() {
        String text = "<code caption=\"bob\">for(int i = 0; i < 20;i++) {\n" +
                      "//do nothing\n" +
                      "}</code>";
            Optional<String> html = Markdown.getHTML(null, text);
            Assert.assertTrue("HTML should not be null", html.isPresent());
            Assert.assertTrue("Markdown document should have multiple lines", html.get().length() > 50);
//            System.out.println(html);

    }

    @Test
    public void codeBlockTest2() {
        String text = "    for(int i = 0; i < 20;i++) {\n" +
                      "      //do nothing for now\n" +
                      "    }\n";
            Optional<String> html = Markdown.getHTML(null, text);
            Assert.assertTrue("HTML should not be null", html.isPresent());
            Assert.assertTrue("Markdown document should have multiple lines", html.get().length() > 50);
//            System.out.println(html);

    }

    @Test
    public void codeBlockTest3() {
        String text = "<pre>for(int i = 0; i < 20;i++) {\n" +
                      "      //do nothing for now\n" +
                      "    }</pre>";
            Optional<String> html = Markdown.getHTML(null, text);
            Assert.assertTrue("HTML should not be null", html.isPresent());
            Assert.assertTrue("Markdown document should have multiple lines", html.get().length() > 50);
 //           System.out.println(html);

    }
    
    
    
    @Test
    public void codeTerminalTest1() {
        String text = "<terminal>for(int i = 0; i < 20;i++) {\n" +
                      "//do nothing\n" +
                      "}</terminal>";
            Optional<String> html = Markdown.getHTML(null, text);
            Assert.assertTrue("HTML should not be null", html.isPresent());
            Assert.assertTrue("Markdown document should have multiple lines", html.get().length() > 50);

    }

    
    
}