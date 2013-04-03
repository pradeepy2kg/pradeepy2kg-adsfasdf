package lk.syntaxchecker;
/**
 * @author Duminda Dharmakeerthi
 */

import lk.rgd.common.util.SinhalaTamilSyntaxCheckerUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class syntaxChecker {
    private static FileWriter fstream;
    private static BufferedWriter output;
    // Settings used below
    // This is the Path where the generated files will be created.
    private static String PATH = "/Syntax_Checker/";

    // Settings for the DB Connection.
    private static Connection conn = null;
    private static String userName = "root";
    private static String password = "root";
    private static String DB_SERVER = "jdbc:mysql://localhost:3306/";
    private static String DRIVER = "com.mysql.jdbc.Driver";

    private static final String COMMA = ",";
    private static final String NEW_LINE = "\n";
    private static String OUT = "";
    private static String SUMMARY = "";
    private static int TOTAL_ERRORS = 0;

    private static List<String> commonTables;
    private static List<String> commonQueries;
    private static List<String> crsTables;
    private static List<String> crsQueries;
    private static List<String> prsTables;
    private static List<String> prsQueries;
    private static Map<String, List<String>> fieldNames;
    private static Map<String, String> tableIndexes;
    private static Map<String, String> errorFields;

    public static void main(String[] args) {
        try {
            makeDirectories();
            errorFields = new HashMap<String, String>();
            populateTableNames();
            populateFieldNames();

            String common = commonSyntaxCorrector();
            writeFile(OUT, "COMMON.csv", PATH);

            String crs = crsSyntaxCorrector();
            writeFile(OUT, "CRS.csv", PATH);

            String prs = prsSyntaxCorrector();
            writeFile(OUT, "PRS.csv", PATH);

            // Writing the summary of syntax checking.
            writeFile(SUMMARY, "SUMMARY.csv", PATH);
            System.out.println("Total Errors: " + TOTAL_ERRORS);

            /**
             * Start correcting error records.
             */
            String executionLog = "";
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_SERVER + "COMMON", userName, password);
            Statement statement = conn.createStatement();
            // Correcting Errors in COMMON
            executionLog += "USE COMMON;" + NEW_LINE;
            statement.executeQuery("USE COMMON;");
            for (String q : commonQueries) {
                executionLog += q + NEW_LINE;
                //System.out.print(q + "\t");
                //statement.executeUpdate(q);
                //System.out.println("\t : OK");
            }
            conn.close();


            // Correcting errors in CRS.
            conn = DriverManager.getConnection(DB_SERVER + "CRS", userName, password);
            statement = conn.createStatement();
            executionLog += "USE CRS;" + NEW_LINE;
            //statement.executeQuery("USE CRS");
            for (String q : crsQueries) {
                executionLog += q + NEW_LINE;
                //System.out.print(q + "\t");
                //statement.executeUpdate(q);
                //System.out.println("\t : OK");
            }
            conn.close();

            // Correcting errors in PRS
            conn = DriverManager.getConnection(DB_SERVER + "PRS", userName, password);
            statement = conn.createStatement();
            executionLog += "USE PRS;" + NEW_LINE;
            //statement.executeQuery("USE PRS");
            for (String q : prsQueries) {
                executionLog += q + NEW_LINE;
                //System.out.print(q + "\t");
                //statement.executeUpdate(q);
                //System.out.println("\t : OK");
            }
            // write the execution log to file.
            writeFile(executionLog, "update.sql", PATH);
            writeFile(executionLog, "execution.log", PATH);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------------- SQL ERROR ---------------");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("------------- I/O ERROR ---------------");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n------------- Unable to connect to DB --------------------\n");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("\n --------------- Database connection terminated ----------------\n");
                } catch (Exception e) { /* ignore close errors */ }
            }
        }
    }

    public static void populateTableNames() {
        commonTables = new ArrayList<String>();
        crsTables = new ArrayList<String>();
        prsTables = new ArrayList<String>();
        tableIndexes = new HashMap<String, String>();

        commonTables.add("COUNTRIES");
        tableIndexes.put("COUNTRIES", "countryId");

        commonTables.add("PROVINCE");
        tableIndexes.put("PROVINCE", "provinceUKey");

        commonTables.add("DISTRICTS");
        tableIndexes.put("DISTRICTS", "districtUKey");

        commonTables.add("DS_DIVISIONS");
        tableIndexes.put("DS_DIVISIONS", "dsDivisionUKey");

        commonTables.add("GN_DIVISIONS");
        tableIndexes.put("GN_DIVISIONS", "gnDivisionUKey");

        commonTables.add("LOCATIONS");
        tableIndexes.put("LOCATIONS", "locationUKey");

        commonTables.add("RACES");
        tableIndexes.put("RACES", "raceId");

        commonTables.add("USERS");
        tableIndexes.put("USERS", "userId");

        commonTables.add("ZONAL_OFFICES");
        tableIndexes.put("ZONAL_OFFICES", "zonalOfficeUKey");

        crsTables.add("COURTS");
        tableIndexes.put("COURTS", "courtUKey");

        crsTables.add("REGISTRAR");
        tableIndexes.put("REGISTRAR", "registrarUKey");

        crsTables.add("BD_DIVISIONS");
        tableIndexes.put("BD_DIVISIONS", "bdDivisionUKey");

        crsTables.add("MR_DIVISIONS");
        tableIndexes.put("MR_DIVISIONS", "mrDivisionUKey");

        crsTables.add("BIRTH_REGISTER");
        tableIndexes.put("BIRTH_REGISTER", "idUKey");

        crsTables.add("DEATH_REGISTER");
        tableIndexes.put("DEATH_REGISTER", "idUKey");

        crsTables.add("ALT_DEATH");
        tableIndexes.put("ALT_DEATH", "idUKey");

        crsTables.add("CERTIFICATE_SEARCH");
        tableIndexes.put("CERTIFICATE_SEARCH", "idUKey");

        prsTables.add("PERSON");
        tableIndexes.put("PERSON", "personUKey");

        prsTables.add("MARRIAGE");
        tableIndexes.put("MARRIAGE", "marriageUKey");

        prsTables.add("ADDRESS");
        tableIndexes.put("ADDRESS", "addressUKey");

    }

    public static void populateFieldNames() {
        fieldNames = new HashMap<String, List<String>>();

        List<String> COUNTRIES = new ArrayList<String>();
        COUNTRIES.add("siCountryName");
        COUNTRIES.add("taCountryName");
        fieldNames.put("COUNTRIES", COUNTRIES);

        List<String> PROVINCE = new ArrayList<String>();
        PROVINCE.add("siProvinceName");
        PROVINCE.add("taProvinceName");
        fieldNames.put("PROVINCE", PROVINCE);

        List<String> DISTRICTS = new ArrayList<String>();
        DISTRICTS.add("siDistrictName");
        DISTRICTS.add("taDistrictName");
        fieldNames.put("DISTRICTS", DISTRICTS);

        List<String> DS_DIVISIONS = new ArrayList<String>();
        DS_DIVISIONS.add("siDivisionName");
        DS_DIVISIONS.add("taDivisionName");
        fieldNames.put("DS_DIVISIONS", DS_DIVISIONS);

        List<String> GN_DIVISIONS = new ArrayList<String>();
        GN_DIVISIONS.add("siGNDivisionName");
        GN_DIVISIONS.add("taGNDivisionName");
        fieldNames.put("GN_DIVISIONS", GN_DIVISIONS);

        List<String> LOCATIONS = new ArrayList<String>();
        LOCATIONS.add("siLocationMailingAddress");
        LOCATIONS.add("siLocationName");
        LOCATIONS.add("sienLocationSignature");
        LOCATIONS.add("taLocationMailingAddress");
        LOCATIONS.add("taLocationName");
        LOCATIONS.add("taenLocationSignature");
        fieldNames.put("LOCATIONS", LOCATIONS);

        List<String> RACES = new ArrayList<String>();
        RACES.add("siRaceName");
        RACES.add("taRaceName");
        fieldNames.put("RACES", RACES);

        List<String> USERS = new ArrayList<String>();
        USERS.add("sienSignatureText");
        USERS.add("taenSignatureText");
        fieldNames.put("USERS", USERS);

        List<String> ZONAL_OFFICES = new ArrayList<String>();
        ZONAL_OFFICES.add("siZonalOfficeMailAddress");
        ZONAL_OFFICES.add("siZonalOfficeName");
        ZONAL_OFFICES.add("taZonalOfficeMailAddress");
        ZONAL_OFFICES.add("taZonalOfficeName");
        fieldNames.put("ZONAL_OFFICES", ZONAL_OFFICES);


        List<String> COURTS = new ArrayList<String>();
        COURTS.add("siCourtName");
        COURTS.add("taCourtName");
        fieldNames.put("COURTS", COURTS);

        List<String> REGISTRAR = new ArrayList<String>();
        REGISTRAR.add("fullNameInOfficialLanguage");
        fieldNames.put("REGISTRAR", REGISTRAR);

        List<String> BD_DIVISIONS = new ArrayList<String>();
        BD_DIVISIONS.add("siDivisionName");
        BD_DIVISIONS.add("taDivisionName");
        fieldNames.put("BD_DIVISIONS", BD_DIVISIONS);

        List<String> MR_DIVISIONS = new ArrayList<String>();
        MR_DIVISIONS.add("siDivisionName");
        MR_DIVISIONS.add("taDivisionName");
        fieldNames.put("MR_DIVISIONS", MR_DIVISIONS);

        List<String> BIRTH_REGISTER = new ArrayList<String>();
        BIRTH_REGISTER.add("childFullNameOfficialLang");
        BIRTH_REGISTER.add("placeOfBirth");
        BIRTH_REGISTER.add("confirmantFullName");
        BIRTH_REGISTER.add("grandFatherBirthPlace");
        BIRTH_REGISTER.add("grandFatherFullName");
        BIRTH_REGISTER.add("greatGrandFatherBirthPlace");
        BIRTH_REGISTER.add("greatGrandFatherFullName");
        BIRTH_REGISTER.add("informantAddress");
        BIRTH_REGISTER.add("informantName");
        BIRTH_REGISTER.add("placeOfMarriage");
        BIRTH_REGISTER.add("notifyingAuthorityAddress");
        BIRTH_REGISTER.add("notifyingAuthorityName");
        BIRTH_REGISTER.add("fatherFullName");
        BIRTH_REGISTER.add("fatherPlaceOfBirth");
        BIRTH_REGISTER.add("motherAddress");
        BIRTH_REGISTER.add("motherFullName");
        BIRTH_REGISTER.add("motherPlaceOfBirth");
        fieldNames.put("BIRTH_REGISTER", BIRTH_REGISTER);

        List<String> DEATH_REGISTER = new ArrayList<String>();
        DEATH_REGISTER.add("causeOfDeath");
        DEATH_REGISTER.add("placeOfBurial");
        DEATH_REGISTER.add("placeOfDeath");
        DEATH_REGISTER.add("deathPersonFatherFullName");
        DEATH_REGISTER.add("deathPersonMotherFullName");
        DEATH_REGISTER.add("deathPersonNameOfficialLang");
        DEATH_REGISTER.add("deathPersonPermanentAddress");
        DEATH_REGISTER.add("rankOrProfession");
        DEATH_REGISTER.add("declarantAddress");
        DEATH_REGISTER.add("declarantFullName");
        DEATH_REGISTER.add("notifyingAuthorityAddress");
        DEATH_REGISTER.add("notifyingAuthorityName");
        fieldNames.put("DEATH_REGISTER", DEATH_REGISTER);


        List<String> ALT_DEATH = new ArrayList<String>();
        ALT_DEATH.add("deathPersonNameOfficialLang");
        ALT_DEATH.add("deathPersonPermanentAddress");
        ALT_DEATH.add("declarantAddress");
        ALT_DEATH.add("declarantFullName");
        fieldNames.put("ALT_DEATH", ALT_DEATH);

        List<String> CERTIFICATE_SEARCH = new ArrayList<String>();
        CERTIFICATE_SEARCH.add("applicantAddress");
        CERTIFICATE_SEARCH.add("applicantFullName");
        CERTIFICATE_SEARCH.add("searchFullNameOfficialLang");
        fieldNames.put("CERTIFICATE_SEARCH", CERTIFICATE_SEARCH);

        List<String> PERSON = new ArrayList<String>();
        PERSON.add("fullNameInOfficialLanguage");
        PERSON.add("placeOfBirth");
        fieldNames.put("PERSON", PERSON);

        List<String> MARRIAGE = new ArrayList<String>();
        MARRIAGE.add("placeOfMarriage");
        fieldNames.put("MARRIAGE", MARRIAGE);

        List<String> ADDRESS = new ArrayList<String>();
        ADDRESS.add("line1");
        fieldNames.put("ADDRESS", ADDRESS);
    }

    public static String commonSyntaxCorrector() throws SQLException, ClassNotFoundException {
        OUT = "";
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(DB_SERVER + "COMMON", userName, password);
        System.out.println("\n---------------- Connected to COMMON ----------------\n");
        commonQueries = new ArrayList<String>();
        for (int i = 0; i < commonTables.size(); i++) {
            String index;
            String TABLE = commonTables.get(i);
            OUT += NEW_LINE + "Table" + COMMA + "COMMON." + TABLE + NEW_LINE;
            SUMMARY += NEW_LINE + "Table" + COMMA + "COMMON." + TABLE + NEW_LINE;
            String QUERY = "SELECT * FROM " + TABLE;
            Statement statement = conn.createStatement();
            statement.executeQuery(QUERY);
            ResultSet rs = statement.getResultSet();

            List<String> fields = fieldNames.get(TABLE);
            String currentValue, checkerValue;
            int totalRecords = 0, errors = 0;
            while (rs.next()) {
                boolean isError = false;
                index = rs.getObject(tableIndexes.get(TABLE)).toString();
                // Checking for syntax errors.
                for (int j = 0; j < fields.size(); j++) {
                    currentValue = rs.getString(fields.get(j));
                    if (currentValue != null) {
                        checkerValue = SinhalaTamilSyntaxCheckerUtil.checkSyntax(currentValue);
                        if (currentValue.length() != checkerValue.length()) {
                            // Error fields will come here
                            isError = true;
                            ++TOTAL_ERRORS;
                            errorFields.put(fields.get(j), checkerValue);
                            OUT += "ID" + COMMA + index + COMMA + fields.get(j) + " Before" + COMMA + formatStringForCSV(currentValue) + COMMA + fields.get(j) + " After" + COMMA + formatStringForCSV(checkerValue) + NEW_LINE;
                        }
                    }
                }
                if (isError) {
                    commonQueries.add(makeUpdateQuery(TABLE, errorFields, tableIndexes.get(TABLE), index));
                    errorFields.clear();
                    ++errors;
                }
                ++totalRecords;
            }
            OUT += "Total Records" + COMMA + totalRecords + NEW_LINE;
            OUT += "Error Records" + COMMA + errors + NEW_LINE;
            SUMMARY += "Total Records" + COMMA + totalRecords + NEW_LINE;
            SUMMARY += "Error Records" + COMMA + errors + NEW_LINE;
        }
        conn.close();
        System.out.println("\n---------------- Disconnected from COMMON ----------------\n");
        return OUT;
    }

    public static String crsSyntaxCorrector() throws SQLException, ClassNotFoundException, IOException {
        OUT = "";
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(DB_SERVER + "CRS", userName, password);
        System.out.println("\n---------------- Connected to CRS ----------------\n");
        crsQueries = new ArrayList<String>();
        for (int i = 0; i < crsTables.size(); i++) {
            String TABLE = crsTables.get(i);
            OUT += NEW_LINE + "Table" + COMMA + "CRS." + TABLE + NEW_LINE;
            SUMMARY += NEW_LINE + "Table" + COMMA + "CRS." + TABLE + NEW_LINE;
            String QUERY = "SELECT * FROM " + TABLE;
            Statement statement = conn.createStatement();
            statement.executeQuery(QUERY);
            ResultSet rs = statement.getResultSet();

            List<String> fields = fieldNames.get(TABLE);
            String index, tableOUT = "Table" + COMMA + TABLE + NEW_LINE;
            String currentValue, checkerValue;
            int totalRecords = 0, errors = 0;
            while (rs.next()) {
                boolean isError = false;
                errorFields.clear();
                index = rs.getObject(tableIndexes.get(TABLE)).toString();
                // Checking for syntax errors.
                for (int j = 0; j < fields.size(); j++) {
                    currentValue = rs.getString(fields.get(j));
                    if (currentValue != null) {
                        checkerValue = SinhalaTamilSyntaxCheckerUtil.checkSyntax(currentValue);

                        if (currentValue.length() != checkerValue.length()) {
                            // Error Records will come here
                            isError = true;
                            ++TOTAL_ERRORS;
                            errorFields.put(fields.get(j), checkerValue);
                            if (TABLE.equals("BIRTH_REGISTER")) {
                                String serial = rs.getString("bdfSerialNo");
                                int bdDivisionUKey = rs.getInt("bdDivisionUKey");
                                OUT += "ID" + COMMA + index + COMMA + fields.get(j) + " Before" + COMMA + formatStringForCSV(currentValue) + COMMA + fields.get(j) + " After" + COMMA + formatStringForCSV(checkerValue) + COMMA + "Serial" + COMMA + serial + COMMA + "BDDivision" + COMMA + bdDivisionUKey + NEW_LINE;
                                tableOUT += "ID" + COMMA + index + COMMA + fields.get(j) + " Before" + COMMA + formatStringForCSV(currentValue) + COMMA + fields.get(j) + " After" + COMMA + formatStringForCSV(checkerValue) + COMMA + "Serial" + COMMA + serial + COMMA + "BDDivision" + COMMA + bdDivisionUKey + NEW_LINE;
                            } else if (TABLE.equals("DEATH_REGISTER")) {
                                String serial = rs.getString("deathSerialNo");
                                int bdDivisionUKey = rs.getInt("bdDivisionUKey");
                                OUT += "ID" + COMMA + index + COMMA + fields.get(j) + " Before" + COMMA + formatStringForCSV(currentValue) + COMMA + fields.get(j) + " After" + COMMA + formatStringForCSV(checkerValue) + COMMA + "Serial" + COMMA + serial + COMMA + "BDDivision" + COMMA + bdDivisionUKey + NEW_LINE;
                                tableOUT += "ID" + COMMA + index + COMMA + fields.get(j) + " Before" + COMMA + formatStringForCSV(currentValue) + COMMA + fields.get(j) + " After" + COMMA + formatStringForCSV(checkerValue) + COMMA + "Serial" + COMMA + serial + COMMA + "BDDivision" + COMMA + bdDivisionUKey + NEW_LINE;
                            } else {
                                OUT += "ID" + COMMA + index + COMMA + fields.get(j) + " Before" + COMMA + formatStringForCSV(currentValue) + COMMA + fields.get(j) + " After" + COMMA + formatStringForCSV(checkerValue) + NEW_LINE;
                                tableOUT += "ID" + COMMA + index + COMMA + fields.get(j) + " Before" + COMMA + formatStringForCSV(currentValue) + COMMA + fields.get(j) + " After" + COMMA + formatStringForCSV(checkerValue) + NEW_LINE;
                            }
                        }
                    }
                }
                if (isError) {
                    crsQueries.add(makeUpdateQuery(TABLE, errorFields, tableIndexes.get(TABLE), index));
                    errorFields.clear();
                    ++errors;
                }
                ++totalRecords;
            }
            OUT += NEW_LINE + "Total Records" + COMMA + totalRecords + NEW_LINE;
            tableOUT += NEW_LINE + "Total Records" + COMMA + totalRecords + NEW_LINE;
            OUT += "Error Records" + COMMA + errors + NEW_LINE;
            tableOUT += "Error Records" + COMMA + errors + NEW_LINE;
            SUMMARY += NEW_LINE + "Total Records" + COMMA + totalRecords + NEW_LINE;
            SUMMARY += "Error Records" + COMMA + errors + NEW_LINE;

            writeFile(tableOUT, TABLE + ".csv", PATH + "CRS/");
        }
        conn.close();
        System.out.println("\n---------------- Disconnected from CRS ----------------\n");
        return OUT;
    }

    public static String prsSyntaxCorrector() throws SQLException, ClassNotFoundException {
        OUT = "";
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(DB_SERVER + "PRS", userName, password);
        System.out.println("\n---------------- Connected to PRS ----------------\n");
        prsQueries = new ArrayList<String>();
        for (int i = 0; i < prsTables.size(); i++) {
            String TABLE = prsTables.get(i);
            OUT += NEW_LINE + "Table" + COMMA + "PRS." + TABLE + NEW_LINE;
            SUMMARY += NEW_LINE + "Table" + COMMA + "PRS." + TABLE + NEW_LINE;
            String QUERY = "SELECT * FROM " + TABLE;
            Statement statement = conn.createStatement();
            statement.executeQuery(QUERY);
            ResultSet rs = statement.getResultSet();

            List<String> fields = fieldNames.get(TABLE);
            String index;
            String currentValue, checkerValue;
            int totalRecords = 0, errors = 0;
            while (rs.next()) {
                boolean isError = false;
                errorFields.clear();
                index = rs.getObject(tableIndexes.get(TABLE)).toString();
                // Checking for syntax errors.
                for (int j = 0; j < fields.size(); j++) {
                    currentValue = rs.getString(fields.get(j));
                    if (currentValue != null) {
                        checkerValue = SinhalaTamilSyntaxCheckerUtil.checkSyntax(currentValue);

                        if (currentValue.length() != checkerValue.length()) {
                            // Error Records will come here
                            isError = true;
                            ++TOTAL_ERRORS;
                            errorFields.put(fields.get(j), checkerValue);
                            OUT += "ID" + COMMA + index + COMMA + fields.get(j) + " Before" + COMMA + formatStringForCSV(currentValue) + COMMA + fields.get(j) + " After" + COMMA + formatStringForCSV(checkerValue) + NEW_LINE;
                        }
                    }
                }
                if (isError) {
                    prsQueries.add(makeUpdateQuery(TABLE, errorFields, tableIndexes.get(TABLE), index));
                    errorFields.clear();
                    ++errors;
                }
                ++totalRecords;
            }
            OUT += NEW_LINE + "Total Records" + COMMA + totalRecords + NEW_LINE;
            OUT += "Error Records" + COMMA + errors + NEW_LINE;
            SUMMARY += NEW_LINE + "Total Records" + COMMA + totalRecords + NEW_LINE;
            SUMMARY += "Error Records" + COMMA + errors + NEW_LINE;
        }
        conn.close();
        System.out.println("\n---------------- Disconnected from PRS ----------------\n");
        return OUT;
    }

    // To write the results to a file.
    public static void writeFile(String content, String fileName, String filePath) throws IOException {
        fstream = new FileWriter(filePath + fileName);
        output = new BufferedWriter(fstream);
        output.write(content);
        output.close();
    }

    public static String makeUpdateQuery(String TABLE, Map<String, String> errorColumns, String indexName, String indexValue) {
        String query = "UPDATE " + TABLE + " SET ";
        int k = 0;
        for (String key : errorColumns.keySet()) {
            if (k > 0) {
                query += ", ";
            }
            query += key + " = \'" + formatStringForMySQL(errorColumns.get(key)) + "\' ";
            k++;
        }
        query += "WHERE " + indexName + " = ";
        if (indexValue.matches("[0-9]+")) {
            query += indexValue + ";";
        } else {
            query += "\'" + indexValue + "\';";
        }
        return query;
    }

    /**
     * Format the string to use in a CSV file.
     * Commas will be replaced by semi-colons. ( ',' -> ';')
     * New Lines will be replaced by spaces ('\n' -> ' ')
     * Return will be replaced by spaces ('\r' -> ' ')
     * Line Feed will be replaced by spaces ('\r' -> ' ')
     *
     * @param input String that needed to be formatted to use in CSV.
     * @return Formatted string
     */
    public static String formatStringForCSV(String input) {
        String output = input.replace(",", ";");
        output = output.replace("\n", " ");
        output = output.replace("\r", " ");
        output = output.replace("\f", " ");
        return output;
    }

    public static String formatStringForMySQL(String input) {
        String output = input.replace("\'", "\\'");
        output = output.replace("\"", "\\'");
        output = output.replace("\n", "\\n");
        output = output.replace("\r", "\\r");
        output = output.replace("\f", "\\f");
        return output;
    }

    public static void makeDirectories() {
        // Creating the directory to store files.
        File syntax_checker = new File(PATH);
        if (!syntax_checker.exists()) {
            if (syntax_checker.mkdir()) {
                System.out.println("Directory Created [ " + PATH + " ]");
            }
        }

        // Create CRS folder
        File crs = new File(PATH + "CRS/");
        if (!crs.exists()) {
            if (crs.mkdir()) {
                System.out.println("Directory created. [ " + PATH + "CRS/ ]");
            }
        }
    }
}
