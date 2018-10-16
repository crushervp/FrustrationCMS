package com.codeexcursion.cms.transform;

import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;;

public class CMLToHTMLTest {

    @Test
    public void jsoupTest() {
        File htmlFile = Paths.get("src/test/artifacts/content/post/2018/getting-tiger-vnc-to-work-on-fedora-28.html")
                .toFile();

        try {
            Document html = Jsoup.parse(htmlFile, "UTF-8");

            Elements metaTags = html.select("meta");
            Assert.assertTrue("Did not find multiple meta tags.", metaTags.size() == 6);
            
            Elements terminals = html.select("terminal");
            Assert.assertTrue("Did not find multiple terminal tags.", terminals.size() > 6);

        } catch (IOException exception) {
            Assert.fail("Unable to find file " + htmlFile.getAbsolutePath());
        }
    }

}