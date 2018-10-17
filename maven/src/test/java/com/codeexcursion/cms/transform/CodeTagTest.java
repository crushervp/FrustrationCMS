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

public class CodeTagTest {

    @Test
    public void jsoupHTMLCodeTest() {
        File htmlFile = Paths.get("src/test/artifacts/content/post/2018/getting-tiger-vnc-to-work-on-fedora-28.html")
                .toFile();

            Document html = Jsoup.parse(
                "<div>" +
                "<code caption=\"HTML\"> <span>html<span></code>" +
                "<code caption=\"More HTML\"> <span>more html<span></code>"                
                );

            Elements codes = html.select("code");
            Assert.assertTrue("Did not find multiple code tags.", codes.size() > 1);

            Elements captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should not find any code captions.", captions.size() < 1);

            Elements preTerminals = html.select("pre[class=prettifyprint]");
            Assert.assertTrue("Should not find any code pre tags.", preTerminals.size() < 1);

            Element code = codes.get(0);
            new CodeTag(code);

            captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should find at least one code caption.", captions.size() > 0);

            preTerminals = html.select("pre[class=prettifyprint]");
            Assert.assertTrue("Should have at least one code pre tag.", preTerminals.size() > 0);


    }

}