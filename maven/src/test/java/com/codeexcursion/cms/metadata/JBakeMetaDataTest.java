package com.codeexcursion.cms.metadata;

import com.codeexcursion.cms.metadata.JBakeMetaData;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


public class JBakeMetaDataTest {


    @Test
    public void getTest() {
        Path isJbake = Paths.get("src/test/artifacts/content/post/2018/grep-file-display-unique-values.md");

        try {
            String text = new String(Files.readAllBytes(isJbake));
            Map<String, List<String>> metadata = JBakeMetaData.get(text);
            Assert.assertNotNull("Failed to get metadata map.", metadata);
            Assert.assertNotNull("Failed to find 'type' in metadata map.", metadata.get("type"));
            Assert.assertEquals("Failed to find 'tip' in 'type' in metadata map.", "tip", metadata.get("type").get(0));

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + isJbake);
        }

    }
    
    
}