package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.common.util.CommonUtil;
import lk.rgd.crs.CRSRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of a ContentRepository
 *
 * The implementation evaluated storing on disk vs MySQL tables, and considering many options, ideas and comments
 * from those who implemented such schemes before, decided to hold content on the file system, in a hierarchical
 * directory structure. (This is to balance inode usage, and prevent possible issues, esp with ext3 file systems)
 *
 * A content repository is rooted at a file system path, and is categorized at a first level (for marriages, this
 * is the DS division id). Next we use two directory levels with names in the range 00, 01, ... FF to create a
 * possible 65K node elements to store individual files. Directories are created only when the current node for
 * a DS division is filled up (i.e. to 256 files), and thus we will not be creating a 65K directories at start!
 *
 * Image files are stored with the idUkey used as the file name (allows a find command on the file system) at the
 * leaf levels, and this implementation will support 16M files per each DS division.
 *
 * @author asankha
 */
public class ContentRepositoryImpl implements ContentRepository {

    private static final Logger logger = LoggerFactory.getLogger(ContentRepositoryImpl.class);

    private final String contentRoot;
    private final int startPos;
    private final Map<Long, Long> divisionMap = new HashMap<Long, Long>();

    public ContentRepositoryImpl(String contentRoot) {
        this.contentRoot = contentRoot;
        if (!new File(contentRoot).exists()) {
            try {
                if (!new File(contentRoot).createNewFile()) {
                    logger.error("Cannot create non-existent content root : {}", contentRoot);
                    throw new IllegalArgumentException("Cannot create content repository root : " + contentRoot);
                }
            } catch (IOException e) {
                handleException("Error using content repository root : " + contentRoot, e, ErrorCodes.UNKNOWN_ERROR);
            }
        }
        this.startPos = contentRoot.length();
    }

    public synchronized String storeFile(long division, long idUKey, File image) {

        Long directory = divisionMap.get(division);
        if (directory == null) {
            directory = 0L;
            divisionMap.put(division, directory);
        }

        File nodeDir = getNodeDir(directory, division);
        File leafFile = new File(nodeDir, Long.toString(idUKey));

        try {
            if (leafFile.createNewFile()) {
                CommonUtil.copyStreams(new FileInputStream(image), new FileOutputStream(leafFile));
                return leafFile.getAbsolutePath().substring(startPos);
            } else {
                handleException("Unexpected error creating leaf file : " +
                    leafFile.getAbsolutePath(), null, ErrorCodes.UNKNOWN_ERROR);
            }
        } catch (IOException e) {
            handleException("Cannot create leaf file : " + leafFile.getAbsolutePath(), e, ErrorCodes.UNKNOWN_ERROR);
        }
        return null;
    }

    private File getNodeDir(long directory, long division) {

        File nodeDir = toHexStringDirectoryPath(directory, division);

        if (nodeDir.exists()) {
            int count = nodeDir.listFiles().length;
            if (count >= 255) {
                directory++;
                nodeDir = toHexStringDirectoryPath(directory, division);
                try {
                    if (nodeDir.createNewFile()) {
                        return nodeDir;
                    }
                } catch (IOException e) {
                    handleException("Cannot create directory : " + nodeDir.getAbsolutePath(), e, ErrorCodes.UNKNOWN_ERROR);
                }
            } else {
                return nodeDir;
            }
        } else {
            try {
                if (nodeDir.createNewFile()) {
                    return nodeDir;
                }
            } catch (IOException e) {
                handleException("Cannot create directory : " + nodeDir.getAbsolutePath(), e, ErrorCodes.UNKNOWN_ERROR);
            }
        }

        handleException("Unexpected error creating node directory : " + nodeDir.getAbsolutePath(), null, ErrorCodes.UNKNOWN_ERROR);
        return null;
    }

    private File toHexStringDirectoryPath(long directory, long division) {

        String dir = Long.toHexString(directory);

        switch (dir.length()) {
            case 1: dir = "000" + dir; break;
            case 2: dir = "00" + dir; break;
            case 3: dir = "0" + dir; break;
            case 4: break;
            default: {
                handleException("Fatal - too large. Unsupported directory length : " + dir +
                    " at division : " + division, null, ErrorCodes.CONTENT_REPOSITORY_TOO_LARGE);
            }
        }

        return new File(contentRoot + File.separator + dir.substring(0,2) + File.separator + dir.substring(2));
    }

    private void handleException(String msg, Exception e, int errorCode) {
        if (e == null) {
            logger.error(msg);
            throw new CRSRuntimeException(msg, errorCode);
        } else {
            logger.error(msg, e);
            throw new CRSRuntimeException(msg, errorCode, e);
        }
    }
}
