package lk.rgd.crs.web.common;

/**
 * Constants which holds all the constant belogs to the project
@author Indunil Moremada
 @author Amith Jayasekara
 @author Chathuranga
 @author Duminda Dharmakeerthi
 */
public class Constants {
    //The repostory config file needed to initialize the Jackrabbit
    //repository. If the repository is obtained via a naming service,
    //for instance, then this information is not required for a client program.
    public static final String CONFIG_FILE = "jackrabbit/repository.xml";

    //The directory for the repository files.
    public static final String REPO_HOME_DIR = "jackrabbit";

    //The repository's JNDI name
    public static final String REPO_NAME = "repo";

    //User ID and password to log into the repository
    public static final String USERID = "userid";
    public static final char[] PASSWORD = "".toCharArray();

    // this is the default location to attach folder
    public static final String FOLDER_NODE="folder";
    //*****

    public static final String DIR_PATH = "/";

    public static final String FILE_ROOT_PATH = "/content";

    public static final String SYSTEM_PATH = "/system";

    public static final String TEMPLATE_PATH = "/system/template";

    // tempory file location
    public static final String TEMP_FILE_LOCATION="F:/tmpImg";

   //file uploading buffer size
    public static final int BUFFER_SIZE=100000;

    // default file path
    public static final String DEFAULT_FILE_PATH="/fileName/";

    //default node type descripter file location
    public static final String DEFAULT_NODE_TYPE_DESCRIPTER="E:/dev/DMS/Cabinet/conf/itf_custom_nodetypes.xml";

    public static final String ERROR="error";

    public static final String SUCCESS="success";
}
