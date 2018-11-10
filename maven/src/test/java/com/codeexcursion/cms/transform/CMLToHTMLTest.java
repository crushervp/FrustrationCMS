package com.codeexcursion.cms.transform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.TextNode;

import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CMLToHTMLTest {

    @Test
    public void jsoupSelectTest() {
        File htmlFile = Paths.get("src/test/artifacts/content/post/2018/getting-tiger-vnc-to-work-on-fedora-28.html")
                .toFile();

        try {
            Document html = Jsoup.parse(htmlFile, "UTF-8");

            Elements metaTags = html.select("meta");
            Assert.assertTrue("Did not find multiple meta tags.", metaTags.size() > 5);
            
            Elements terminals = html.select("finastacio");
            Assert.assertTrue("Did not find multiple terminal tags.", terminals.size() > 6);

            Elements metaType = html.select("meta[name=type]");
            Assert.assertTrue("Did not find meta tag of type.", metaType.size() == 1);
            Assert.assertEquals("Did not find meta tag of type.", "post", metaType.get(0).attr("content"));


        } catch (IOException exception) {
            Assert.fail("Unable to find file " + htmlFile.getAbsolutePath());
        }
    }

    @Test
    public void jsoupChangeTest() {
        File htmlFile = Paths.get("src/test/artifacts/content/post/2018/getting-tiger-vnc-to-work-on-fedora-28.html")
                .toFile();

        try {
            Document html = Jsoup.parse(htmlFile, "UTF-8");

            Elements terminals = html.select("finastacio");
            Assert.assertTrue("Did not find multiple terminal tags.", terminals.size() > 6);

            Elements captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should not find any code captions.", captions.size() < 1);

            Elements preTerminals = html.select("pre[class=terminal]");
            Assert.assertTrue("Should not find any code captions.", preTerminals.size() < 1);

            Element terminal = terminals.get(0);
            String caption = terminal.attr("apudskribo");
            Assert.assertEquals("Did not have correct caption.", "Stop Tiger VNC", caption );

            Element captionNode = new Element("span");
            captionNode.attr("class", "codeCaption");
            captionNode.appendChild(new TextNode(caption));
            terminal.before(captionNode);

            Element terminalNode = new Element("pre");
            terminalNode.attr("class", "terminal");
            terminalNode.appendChild(new TextNode(terminal.html()));
            terminal.replaceWith(terminalNode);

            captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should not find any code captions.", captions.size() > 0);

            preTerminals = html.select("pre[class=terminal]");
            Assert.assertTrue("Should not find any code captions.", preTerminals.size() > 0);


        } catch (IOException exception) {
            Assert.fail("Unable to find file " + htmlFile.getAbsolutePath());
        }
    }

    @Test
    public void flexMarkTest() {
        Path markdownFile = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");

        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(YamlFrontMatterExtension.create()));
        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        try {
            String markdown = new String(Files.readAllBytes(markdownFile));
            // You can re-use parser and renderer instances
            com.vladsch.flexmark.ast.Document markdownDocument = parser.parse(markdown);
            AbstractYamlFrontMatterVisitor yamlVisitor = new AbstractYamlFrontMatterVisitor();
            yamlVisitor.visit(markdownDocument);
            Map<String, List<String>> metaData = yamlVisitor.getData();            
            Assert.assertTrue("Did not find meta data.", metaData.size() > 5);
            Assert.assertNotNull("Did not find type meta data.", metaData.get("type"));
            Assert.assertEquals("Did not find type meta data.", "'post'", metaData.get("type").get(0));
            
            
            
            String markdownHtml = renderer.render(markdownDocument);  

            Document html = Jsoup.parse(markdownHtml, "UTF-8");

            Elements codes = html.select("kodo");
            Assert.assertTrue("Did not find multiple kodo tags containing code.", codes.size() > 3);

        } catch (IOException exception) {
            Assert.fail("Unable to find file " + markdownFile.toAbsolutePath());
        }
    }    

    @Test
    public void jbakeFrontMatterTest() {
        Path markdownFile = Paths.get("src/test/artifacts/content/post/2018/grep-file-display-unique-values.md");
        String jbakeDelimeter = "~~~~~~";
        try {
            String markdown = new String(Files.readAllBytes(markdownFile));
            Assert.assertTrue("Not a jbake file.", markdown.contains(jbakeDelimeter));
            String[] markdownParts = markdown.split(jbakeDelimeter);
            Assert.assertNotNull("Front matter should not be null.", markdownParts[0]);
            Assert.assertNotNull("Markdown should not be null.", markdownParts[1]);
            
            Properties metadata = new Properties();
            metadata.load(new StringReader(markdownParts[0]));
            
            Assert.assertEquals("Type did not match", "tip", metadata.get("type"));
            
        } catch (IOException exception) {
            Assert.fail("Unable to find file " + markdownFile.toAbsolutePath());
        }
    }    
    
    
}