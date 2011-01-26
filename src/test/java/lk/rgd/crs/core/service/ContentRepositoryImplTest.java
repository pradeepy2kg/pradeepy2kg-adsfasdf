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
        } catch (Exception ignore) {
        }

        ContentRepository cr = new ContentRepositoryImpl("/tmp/cr");

        File f = File.createTempFile("temp", ".data");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write("Hello World".getBytes());
        fos.close();

        int division = 1;
        String idUKey = null;
        for (int i = 1; i <= 260; i++) {
            idUKey = Integer.toString(i);
            if (i <= 255) {
                String path = cr.storeFile(division, idUKey, f);
                Assert.assertEquals("/1/00/00/" + idUKey, path);
            } else {
                String path = cr.storeFile(division, idUKey, f);
                Assert.assertEquals("/1/00/01/" + idUKey, path);
            }
        }

        // create a new instance
        cr = new ContentRepositoryImpl("/tmp/cr");

        // test use of last directory for existing directory structure after re-initialization
        division = 1;
        String path = cr.storeFile(division, Integer.toString(261), f);
        Assert.assertEquals("/1/00/01/" + 261, path);

        path = cr.storeFile(division, Integer.toString(262), f);
        Assert.assertEquals("/1/00/01/" + 262, path);

        // check update of the same idukey
        path = cr.storeFile(division, Integer.toString(262), f);
        Assert.assertEquals("/1/00/01/" + 262, path);

    }
}
