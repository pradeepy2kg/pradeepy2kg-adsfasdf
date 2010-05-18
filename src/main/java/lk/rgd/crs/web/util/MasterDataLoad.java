package lk.rgd.crs.web.util;

import lk.rgd.crs.api.domain.District;

import java.util.List;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;

/**
 * @author Chathuranga
 *
 * MasterDataLoad is a Singleton class used to load data to user ineterfaces.         * 
 */
public class MasterDataLoad {
    private final static MasterDataLoad masterDataLoad = new MasterDataLoad();

    /** MasterDataLoadConstructor */
    private MasterDataLoad() {
    }

    /**
     *  Get an instance of MasterDataLoad if not initialized otherwise gives a reference
     * @return  MasterDataLoad object
     */
    public static MasterDataLoad getInstance(){
        return masterDataLoad;
    }

    /**
     *   Returns list of available Districts according to selected language
     * @param language selected  
     * @return list of districts
     */
    public List<District> loadDistricts(int language){
        // hard coded for the moment should be loaded from the database
        List<District> districtList=new ArrayList<District>();

        switch (language){
            case 1:
                // sinhala
                districtList.add(new District(1,convertToString("\u0DBD\u0DD2\u0DBA\u0DCF\u0DB4\u0DAF\u0DD2\u0D82\u0DA0\u0DD2 \u0DAF\u0DD2\u0DB1\u0DBA")));
                districtList.add(new District(2,convertToString("\u0D9A\u0DAD\u0DD4\u0DC0\u0DBB\u0DBA\u0DCF\u0D9C\u0DDA \u0DB1\u0DB8")));
                districtList.add(new District(3,"දිවුලපිටි"));
                districtList.add(new District(4,"GalleS"));
                districtList.add(new District(5,"MataraS"));
                districtList.add(new District(6,"HambantotaS"));
                districtList.add(new District(7,"JaffnaS"));
                districtList.add(new District(8,"MataleS"));
                districtList.add(new District(9,"AnuradhapuraS"));
                break;
            case 2:
                // tamil
                districtList.add(new District(1,"ColomboT"));
                districtList.add(new District(2,"KalutaraT"));
                districtList.add(new District(3,"GampahaT"));
                districtList.add(new District(4,"GalleT"));
                districtList.add(new District(5,"MataraT"));
                districtList.add(new District(6,"HambantotaT"));
                districtList.add(new District(7,"JaffnaT"));
                districtList.add(new District(8,"MataleT"));
                districtList.add(new District(9,"AnuradhapuraT"));
                break;
            case 3:
                // english
                districtList.add(new District(1,"Colombo"));
                districtList.add(new District(2,"Kalutara"));
                districtList.add(new District(3,"Gampaha"));
                districtList.add(new District(4,"Galle"));
                districtList.add(new District(5,"Matara"));
                districtList.add(new District(6,"Hambantota"));
                districtList.add(new District(7,"Jaffna"));
                districtList.add(new District(8,"Matale"));
                districtList.add(new District(9,"Anuradhapura"));
                break;
        }
        return districtList;
    }

    /**
       this method convert unicode sequance to a UTF-8 string  only for testing purposes
      */
    private String convertToString(String unicodeSequance) {
        String converted = null;
        try {
            byte[] utf8 = unicodeSequance.getBytes("UTF-8");
            converted = new String(utf8, "UTF-8");
            System.out.println(">>>>>>>>>>>>>>>>>>>" + converted);
        } catch (UnsupportedEncodingException e) {
            //log.error("unicode sequance cannot convert to UTF-8", e);
        }
        return converted;
    }

    /**
     *   Returns list of Countries
     * @return list of countries
     */
    public List<String> loadCountries(){
        // hard coded for the moment should be loaded from the database
        List<String> countryList=new ArrayList<String>();
        countryList.add("Canada");
        countryList.add("USA");
        countryList.add("England");
        countryList.add("Germany");
        countryList.add("India");
        countryList.add("Australia");

        return countryList;
    }
}
