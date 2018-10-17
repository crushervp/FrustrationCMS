package com.codeexcursion.cms.transform;

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
import org.jsoup.nodes.Node;

public class TerminalTagTest {

    @Test
    public void jsoupTerminalTest() {
        File htmlFile = Paths.get("src/test/artifacts/content/post/2018/getting-tiger-vnc-to-work-on-fedora-28.html")
                .toFile();

            Document html = Jsoup.parse(
                "<div>" +
                "<terminal caption=\"Stuff\"> [chris@] >ls<span></terminal>" +
                "<terminal caption=\"More Stuff\"> [chris@] > cd ..</terminal>"                
                );

            Elements terminals = html.select("terminal");
            Assert.assertTrue("Did not find multiple terminal tags.", terminals.size() > 1);

            Elements captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should not find any terminal captions.", captions.size() < 1);

            Elements preTerminals = html.select("pre[class=terminal]");
            Assert.assertTrue("Should not find any terminal pre tags.", preTerminals.size() < 1);

            Element code = terminals.get(0);
            new TerminalTag(code);

            captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should find at least one terminal caption.", captions.size() > 0);

            preTerminals = html.select("pre[class=terminal]");
            Assert.assertTrue("Should have at least one terminal pre tag.", preTerminals.size() > 0);


    }

}