package lk.rgd.common.util;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author asankha
 */
public class NameFormatUtilTest extends TestCase {

    public void testDisplayName() throws Exception {
        Assert.assertEquals("Chamath Perera", NameFormatUtil.getDisplayName("Asankha Chamath Perera"));
        Assert.assertEquals("Chamath  Perera", NameFormatUtil.getDisplayName("Asankha  Chamath  Perera"));
        Assert.assertEquals("අසංඛ චමත් පෙරේරා", NameFormatUtil.getDisplayName("සෙබස්තියන්  අසංඛ චමත් පෙරේරා "));
        Assert.assertEquals("අසංඛ චමත් පෙරේරා", NameFormatUtil.getDisplayName("සෙබස්තියන් අසංඛ චමත් පෙරේරා "));
    }
}
