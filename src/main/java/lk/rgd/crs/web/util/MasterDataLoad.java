package lk.rgd.crs.web.util;

import lk.rgd.common.api.domain.District;
import lk.rgd.crs.api.domain.PrintData;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Chathuranga
 *         <p/>
 *         MasterDataLoad is a Singleton class used to load data to user ineterfaces.
 */
public class MasterDataLoad {
    private final static MasterDataLoad masterDataLoad = new MasterDataLoad();

    /**
     * MasterDataLoadConstructor
     */
    private MasterDataLoad() {
    }

    /**
     * Get an instance of MasterDataLoad if not initialized otherwise gives a reference
     *
     * @return MasterDataLoad object
     */
    public static MasterDataLoad getInstance() {
        return masterDataLoad;
    }

    /**
     * Returns list of available Districts according to selected language
     *
     * @param language selected
     * @return list of districts
     */
    public List<District> loadDistricts(String language) {
        int lang_id = 0;
        if (language.equals("Sinhala")) {
            lang_id = 1;
        } else if (language.equals("Tamil")) {
            lang_id = 2;
        } else {
            lang_id = 3;
        }
        // hard coded for the moment should be loaded from the database
        List<District> districtList = new ArrayList<District>();

        switch (lang_id) {
            case 1:
                // sinhala
                /*districtList.add(new District(1, "කොළඹ"));
                districtList.add(new District(2, "ගම්පහ"));
                districtList.add(new District(3, "ගාල්ල"));
                districtList.add(new District(4, "පානදුර"));
                districtList.add(new District(5, "මාතර"));
                districtList.add(new District(6, "හම්බන්තොට"));
                districtList.add(new District(7, "යාපනය"));
                districtList.add(new District(8, "මාතලේ"));
                districtList.add(new District(9, "අනුරාධපුර"));*/
                break;
            case 2:
                // tamil
                /*districtList.add(new District(1, "ச்cட்ச்ட்c"));
                districtList.add(new District(2, "ச்cட்ச்ட்c"));
                districtList.add(new District(3, "ச்cட்ச்ட்c"));
                districtList.add(new District(4, "அச்டச்ட்க்க்"));
                districtList.add(new District(5, "அச்டச்ட்க்க்"));
                districtList.add(new District(6, "அச்டச்ட்க்க்"));
                districtList.add(new District(7, "அச்டச்ட்க்க்"));
                districtList.add(new District(8, "பானதுர"));
                districtList.add(new District(9, "பானதுர"));*/
                break;
            case 3:
                // english
                /*districtList.add(new District(1, "Colombo"));
                districtList.add(new District(2, "Kalutara"));
                districtList.add(new District(3, "Gampaha"));
                districtList.add(new District(4, "Galle"));
                districtList.add(new District(5, "Matara"));
                districtList.add(new District(6, "Hambantota"));
                districtList.add(new District(7, "Jaffna"));
                districtList.add(new District(8, "Matale"));
                districtList.add(new District(9, "Anuradhapura"));*/
                break;
        }
        return districtList;
    }

    /**
     * Returns list of Countries
     *
     * @return list of countries
     */
    public List loadCountries(String language) {
//        // hard coded for the moment should be loaded from the database
        List countryMap = new ArrayList();
//        countryMap.add(1, "Canada");
//        countryMap.add(2, "USA");
//        countryMap.add(3, "England");
//        countryMap.add(4, "Germany");
//        countryMap.add(5, "India");
//        countryMap.add(6, "Australia");

        return countryMap;
    }

    /**
     * Return list of birth confirmation forms to be printed
     *
     * @return
     */
    public List<PrintData> getPrintList(int i) {
        List<PrintData> printList = new ArrayList<PrintData>();

        if (i == 1) {
            printList.add(new PrintData(1, "BC001", "Chathuranga", 0));
            printList.add(new PrintData(2, "BC002", "Amith", 0));
            printList.add(new PrintData(3, "BC003", "Indunil", 0));
            printList.add(new PrintData(4, "BC004", "Duminda", 0));
            printList.add(new PrintData(5, "BC005", "Sunil", 0));
            printList.add(new PrintData(6, "BC006", "Tharanga", 0));
            printList.add(new PrintData(7, "BC007", "Mahesh", 1));
            printList.add(new PrintData(8, "BC008", "Chathuranga", 1));
            printList.add(new PrintData(9, "BC009", "Amith", 0));
            printList.add(new PrintData(10, "BC010", "Indunil", 0));
            printList.add(new PrintData(11, "BC011", "Yasas", 1));
            printList.add(new PrintData(12, "BC012", "Dilan", 0));
            printList.add(new PrintData(13, "BC013", "Udara", 0));
            printList.add(new PrintData(14, "BC014", "Mahinda", 0));
            printList.add(new PrintData(15, "BC015", "Chathuranga", 0));
            printList.add(new PrintData(16, "BC016", "Amith", 0));
            printList.add(new PrintData(17, "BC017", "Indunil", 0));
            printList.add(new PrintData(18, "BC018", "Duminda", 0));
            printList.add(new PrintData(19, "BC019", "Sunil", 0));
            printList.add(new PrintData(20, "BC020", "Tharanga", 0));
            printList.add(new PrintData(21, "BC021", "Mahesh", 1));
            printList.add(new PrintData(22, "BC022", "Chathuranga", 1));
            printList.add(new PrintData(23, "BC023", "Amith", 0));
            printList.add(new PrintData(24, "BC024", "Indunil", 0));
            printList.add(new PrintData(25, "BC025", "Yasas", 1));
            printList.add(new PrintData(26, "BC026", "Dilan", 0));
            printList.add(new PrintData(27, "BC027", "Udara", 0));
            printList.add(new PrintData(28, "BC028", "Mahinda", 0));
        } else if (i == 2) {
            printList.add(new PrintData(1, "BC001", "Chathuranga", 0));
            printList.add(new PrintData(2, "BC002", "Amith", 0));
            printList.add(new PrintData(3, "BC003", "Indunil", 0));
            printList.add(new PrintData(4, "BC004", "Duminda", 0));
            printList.add(new PrintData(5, "BC005", "Sunil", 0));
            printList.add(new PrintData(6, "BC006", "Tharanga", 0));
            printList.add(new PrintData(9, "BC009", "Amith", 0));
            printList.add(new PrintData(10, "BC010", "Indunil", 0));
            printList.add(new PrintData(12, "BC012", "Dilan", 0));
            printList.add(new PrintData(13, "BC013", "Udara", 0));
            printList.add(new PrintData(14, "BC014", "Mahinda", 0));
        } else {
            printList.add(new PrintData(7, "BC007", "Mahesh", 1));
            printList.add(new PrintData(8, "BC008", "Chathuranga", 1));
            printList.add(new PrintData(11, "BC011", "Yasas", 1));
        }
        return printList;
    }
}
