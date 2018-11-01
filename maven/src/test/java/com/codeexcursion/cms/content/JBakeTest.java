package com.codeexcursion.cms.content;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;


public class JBakeTest {

    @Test
    public void isJbakeTest() {
        Path isJbake = Paths.get("src/test/artifacts/content/post/2018/grep-file-display-unique-values.md");
        Path isNotJbake = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");

        try {
            String text = new String(Files.readAllBytes(isJbake));
            
            Assert.assertTrue("JBake file misidentified", JBake.isJBake(text));

            text = new String(Files.readAllBytes(isNotJbake));
            Assert.assertFalse("Non JBake file misidentified", JBake.isJBake(text));
            
        } catch (IOException exception) {
            Assert.fail("Unable to read file " + isJbake);
        }

    }

    @Test
    public void notJBakeTest() {
        Path isNotJbake = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");

        try {
            String text = new String(Files.readAllBytes(isNotJbake));
            try {
                Optional<Map<String, List<String>>> metadata = new JBake(text).getMetadata();
              Assert.fail("IllegalArgumentException was not thrown.");
            } catch(IllegalArgumentException exception) {
              //Exception was expected.
            }

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + isNotJbake);
        }

    }



    @Test
    public void getMetaDataTest() {
        Path isJbake = Paths.get("src/test/artifacts/content/post/2018/grep-file-display-unique-values.md");
        Path isNotJbake = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");

        try {
            String text = new String(Files.readAllBytes(isJbake));
            Optional<Map<String, List<String>>> metadata = new JBake(text).getMetadata();
            Assert.assertNotNull("Failed to get metadata map.", metadata);
            Assert.assertNotNull("Failed to find 'type' in metadata map.", metadata.get().get("type"));
            Assert.assertEquals("Failed to find 'tip' in 'type' in metadata map.", "tip", metadata.get().get("type").get(0));

            text = new String(Files.readAllBytes(isNotJbake));
            try {
              metadata = new JBake(text).getMetadata();
              Assert.fail("IllegalArgumentException was not thrown.");
            } catch(IllegalArgumentException exception) {
              //Exception was expected.
            }

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + isJbake);
        }

    }
    
    @Test
    public void getContentTest() {
        Path isJbake = Paths.get("src/test/artifacts/content/post/2018/grep-file-display-unique-values.md");
        Path isNotJbake = Paths.get("src/test/artifacts/content/post/2018/liferay-sybase-to-oracle-data-migration.md");

        try {
            String text = new String(Files.readAllBytes(isJbake));
            Optional<String> content = new JBake(text).getContent();
            Assert.assertNotNull("Failed to get content.", content);
            Assert.assertTrue("Failed to get content.", content.get().length() > 100);

        } catch (IOException exception) {
            Assert.fail("Unable to read file " + isJbake);
        }

    }    
}