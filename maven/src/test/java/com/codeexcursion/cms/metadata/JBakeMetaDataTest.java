package com.codeexcursion.cms.metadata;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;


public class JBakeMetaDataTest {


    @Test
    public void getTest() {
        Path isJbake = Paths.get("src/test/artifacts/content/post/2018/grep-file-display-unique-values.md");

        try {
            String text = new String(Files.readAllBytes(isJbake));
            Optional<Map<String, List<String>>> metadata = JBakeMetaData.get(text);
            Assert.assertNotNull("Failed to find 'type' in metadata map.", metadata.get().get("type"));
            Assert.assertEquals("Failed to find 'tip' in 'type' in metadata map.", "tip", metadata.get().get("type").get(0));

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + isJbake);
        }

    }
    
    
}