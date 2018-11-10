package com.codeexcursion.cms.content;

import com.codeexcursion.cms.transform.HTML;
import com.vladsch.flexmark.ast.Document;


import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;


public class HTMLTest {

    @Test
    public void codeTest() {
        Path markdownFile = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");
        try {
            String text = new String(Files.readAllBytes(markdownFile));
            Optional<String> html = Markdown.getHTML(text);
            Assert.assertTrue("Markdown document should not be null", html.isPresent());
            Assert.assertTrue("Markdown document should have multiple lines", html.get().length() > 100);
            Optional<String> transformedHTML = HTML.transform(html.get());

            Assert.assertTrue("HTML should not be null", transformedHTML.isPresent());
            Assert.assertTrue("HTML should have multiple lines", transformedHTML.get().length() > 100);
            
            System.out.println(transformedHTML.get());
            
            
        } catch (IOException exception) {
            Assert.fail("Unable to read file " + markdownFile);
        }

    }
    
}