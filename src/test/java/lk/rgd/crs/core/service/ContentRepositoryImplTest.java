package lk.rgd.crs.core.service;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author asankha
 */
public class ContentRepositoryImplTest extends TestCase {

    public void testContentRepository() throws Exception {

        ContentRepository cr = new ContentRepositoryImpl("/tmp/cr");

        File f = File.createTempFile("temp", ".data");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write("Hello World".getBytes());
        fos.close();

        String path = cr.storeFile(1, 1, f);
        System.out.println(path);
    }
}
