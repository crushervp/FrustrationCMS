package com.codeexcursion.cms.transform;

import java.nio.file.Paths;
import java.io.File;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;

public class TagsTest {

    @Test
    public void getCodeTitleTest() {
        File htmlFile = Paths.get("src/test/artifacts/content/post/2018/getting-tiger-vnc-to-work-on-fedora-28.html")
                .toFile();

            Document html = Jsoup.parse(
                "<div>\n" +
                "<code caption=\"HTML\"> <span>html<span></code>\n" +
                "<code caption=\"More HTML\"> <span>more html<span></code>\n" +
                "</div>\n"
                );

            Elements codes = html.select("code");
            Assert.assertTrue("Did not find multiple code tags.", codes.size() > 1);

            Elements captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should not find any code captions.", captions.size() < 1);

            Elements preTerminals = html.select("pre[class=prettifyprint]");
            Assert.assertTrue("Should not find any code pre tags.", preTerminals.size() < 1);

            Optional<Element> optionalResult = Tags.getCaption(codes.get(0));

            Assert.assertNotNull("Code caption should not be null.", optionalResult);
            Assert.assertTrue("Code caption should have value.", optionalResult.isPresent());
            Element result = optionalResult.get();
            
            Assert.assertTrue("Should find at least one code caption.", result.nodeName().equals("span"));

            Assert.assertTrue("Code caption should have codeCaption class.", result.hasClass("codeCaption"));
            
    }

    @Test
    public void getCodeTitleNullTest() {
            Optional<Element> optionalResult = Tags.getCaption(null);

            Assert.assertNotNull("Optional result should not be null.", optionalResult);
            Assert.assertFalse("Code caption should not have value.", optionalResult.isPresent());
            
    }


    @Test
    public void getCodeTitleWrongElementTest() {
            Optional<Element> optionalResult = Tags.getCaption(new Element("div"));

            Assert.assertNotNull("Optional result should not be null.", optionalResult);
            Assert.assertFalse("Code caption should not have value.", optionalResult.isPresent());
            
    }

    
    
    @Test
    public void getContentTest() {
        File htmlFile = Paths.get("src/test/artifacts/content/post/2018/getting-tiger-vnc-to-work-on-fedora-28.html")
                .toFile();

            Document html = Jsoup.parse(
                "<div>\n" +
                "<code caption=\"HTML\"> <span>html<span></code>\n" +
                "<code caption=\"More HTML\"> <span>more html<span></code>\n" +
                "</div>\n"
                );

            Elements codes = html.select("code");
            Assert.assertTrue("Did not find multiple code tags.", codes.size() > 1);

            Elements captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should not find any code captions.", captions.size() < 1);

            Elements preTerminals = html.select("pre[class=prettifyprint]");
            Assert.assertTrue("Should not find any code pre tags.", preTerminals.size() < 1);

            Optional<Element> optionalResult = Tags.getContent(codes.get(0), "prettifyprint");

            Assert.assertNotNull("Code body should not be null.", optionalResult);
            Assert.assertTrue("Code body should have value.", optionalResult.isPresent());
            
            
            Element result = optionalResult.get();

            Assert.assertTrue("Should find pre tag.", result.nodeName().equals("pre"));

            Assert.assertTrue("Pre tag should have prettifyprint class.", result.hasClass("prettifyprint"));
            
            Assert.assertTrue("Pre tag should have code in it.", result.hasText());

            Assert.assertTrue("Pre tag should have code with 'html' in it.", result.text().contains("html"));

    }
    
    
    @Test
    public void getContentNullTest() {
            Optional<Element> optionalResult = Tags.getContent(null, null);

            Assert.assertNotNull("Optional result should not be null.", optionalResult);
            Assert.assertFalse("Code body should not have value.", optionalResult.isPresent());
            
    }
    
    
}