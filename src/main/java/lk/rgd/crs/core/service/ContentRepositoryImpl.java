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
import java.util.Arrays;
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
    /**
     * An in-memory cache of the last used File pointing to the directory for the given division
     */
    private final Map<Long, Holder> divisionMap = new HashMap<Long, Holder>();

    public ContentRepositoryImpl(String contentRoot) {

        this.contentRoot = contentRoot;

        if (!new File(contentRoot).exists()) {
            if (!new File(contentRoot).mkdirs()) {
                logger.error("Cannot create non-existent content root : {}", contentRoot);
                throw new IllegalArgumentException("Cannot create content repository root : " + contentRoot);
            }
        }
        this.startPos = contentRoot.length();
    }

    public synchronized String storeFile(long division, String idUKey, File image) {

        Holder holder = divisionMap.get(division);
        File nodeDir = holder != null ? holder.getDir() : null;

        // if we do not have a cached nodeDir or if the nodeDir is known to be full, locate nodeDir to use
        if (nodeDir == null || holder.getCount() >= 255) {

            Long directory;
            final File divisionLevel = new File(contentRoot + File.separator + division);

            if (divisionLevel.exists()) {

                final String[] firstLevelFileNames = divisionLevel.list();
                if (firstLevelFileNames.length == 0) {
                    directory = 0L;

                } else {
                    // sort, and select last if any exists
                    Arrays.sort(firstLevelFileNames);
                    String first = firstLevelFileNames[firstLevelFileNames.length-1];
                    final String[] secondLevelFileNames = new File(divisionLevel, first).list();

                    if (secondLevelFileNames.length == 0) {
                        directory = Long.parseLong(first,16) * 256L;

                    } else {
                        // sort, and select last if any exists
                        Arrays.sort(secondLevelFileNames);
                        String second = secondLevelFileNames[secondLevelFileNames.length-1];

                        long f = Long.parseLong(first,16);
                        directory =  (f == 0 ? 1 : f) * Long.parseLong(second,16);
                    }
                }
            } else {
                directory = 0L;
            }

            holder = new Holder();
            nodeDir = getNodeDir(directory, division, holder);
            divisionMap.put(division, holder);
        }

        //todo: to be removed
        /*String ext = ".tiff";
        final int dotPos = ext.indexOf('.');
        if (dotPos != -1) {
            ext = ext.substring(dotPos);
        }
        File leafFile = new File(nodeDir, Long.toString(idUKey) + ext);
        File leafFile = new File(nodeDir, Long.toString(idUKey));  */
        File leafFile = new File(nodeDir,idUKey);

        try {
            if (!leafFile.exists() && leafFile.createNewFile()) {
                holder.incrementCount();
                CommonUtil.copyStreams(new FileInputStream(image), new FileOutputStream(leafFile));
                return leafFile.getAbsolutePath().substring(startPos);
            } else if (leafFile.exists()) {
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

    private File getNodeDir(long directory, long division, Holder holder) {

        File nodeDir = toHexStringDirectoryPath(directory, division);

        if (nodeDir.exists()) {
            int count = nodeDir.listFiles().length;
            if (count < 255) {
                holder.setDir(nodeDir);
                holder.setCount(count);
                return nodeDir;
            } else {
                directory++;
                nodeDir = toHexStringDirectoryPath(directory, division);

                if (nodeDir.mkdirs()) {
                    holder.setDir(nodeDir);
                    return nodeDir;
                }
            }
        } else {
            if (nodeDir.mkdirs()) {
                holder.setDir(nodeDir);
                return nodeDir;
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

        return new File(contentRoot + File.separator + division + File.separator +
            dir.substring(0,2) + File.separator + dir.substring(2));
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

    private class Holder {
        private File dir;
        private int count;

        private Holder() {}

        private Holder(File dir, int count) {
            this.dir = dir;
            this.count = count;
        }

        public File getDir() {
            return dir;
        }

        public int getCount() {
            return count;
        }

        public void setDir(File dir) {
            this.dir = dir;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void incrementCount() {
            count++;
        }
    }
}
