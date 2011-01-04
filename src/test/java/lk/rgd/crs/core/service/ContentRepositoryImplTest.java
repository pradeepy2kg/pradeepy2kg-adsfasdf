package lk.rgd.crs.core.service;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author asankha
 */
public class ContentRepositoryImplTest extends TestCase {

    public void testContentRepository() throws Exception {

        try {
            Runtime.getRuntime().exec("rm -rf /tmp/cr");
        } catch (Exception ignore) {}

        ContentRepository cr = new ContentRepositoryImpl("/tmp/cr");

        File f = File.createTempFile("temp", ".data");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write("Hello World".getBytes());
        fos.close();

        int division = 1;
        for (int i=1; i<=260; i++) {
            String path = cr.storeFile(division, i, f);
            if (i <= 255) {
                Assert.assertEquals("/1/00/00/" + i ,path);
            } else {
                Assert.assertEquals("/1/00/01/" + i, path);
            }
        }

    }
}
