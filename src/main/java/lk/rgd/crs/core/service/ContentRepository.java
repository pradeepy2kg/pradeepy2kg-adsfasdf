package lk.rgd.crs.core.service;

import java.io.File;

/**
 * @author asankha
 */
public interface ContentRepository {

    /**
     * Store given file in the content repository and return its relative path name
     * @param division the first level of hierarchy
     * @param idUKey the unique ID of the resource stored
     * @param image the image file
     * @return the relative file path used to store the file
     */
    public String storeFile(long division, String idUKey, File image);
}
