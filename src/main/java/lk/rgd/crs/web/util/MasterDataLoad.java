package lk.rgd.crs.web.util;

import lk.rgd.crs.api.domain.District;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
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
    public List<District> loadDistricts(String language){
        int lang_id=0;
        if(language.equals("Sinhala")){
            lang_id=1;
        }else if(language.equals("Tamil")){
            lang_id=2;
        } else{
            lang_id=3;
        }
        // hard coded for the moment should be loaded from the database
        List<District> districtList=new ArrayList<District>();

        switch (lang_id){
            case 1:
                // sinhala
                districtList.add(new District(1,"කොළඹ"));
                districtList.add(new District(2,"ගම්පහ"));
                districtList.add(new District(3,"ගාල්ල"));
                districtList.add(new District(4,"පානදුර"));
                districtList.add(new District(5,"මාතර"));
                districtList.add(new District(6,"හම්බන්තොට"));
                districtList.add(new District(7,"යාපනය"));
                districtList.add(new District(8,"මාතලේ"));
                districtList.add(new District(9,"අනුරාධපුර"));
                break;
            case 2:
                // tamil
                districtList.add(new District(1,"ச்cட்ச்ட்c"));
                districtList.add(new District(2,"ச்cட்ச்ட்c"));
                districtList.add(new District(3,"ச்cட்ச்ட்c"));
                districtList.add(new District(4,"அச்டச்ட்க்க்"));
                districtList.add(new District(5,"அச்டச்ட்க்க்"));
                districtList.add(new District(6,"அச்டச்ட்க்க்"));
                districtList.add(new District(7,"அச்டச்ட்க்க்"));
                districtList.add(new District(8,"பானதுர"));
                districtList.add(new District(9,"பானதுர"));
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
    public HashMap loadCountries(){
//        // hard coded for the moment should be loaded from the database
        HashMap<Integer,String> countryList=new HashMap<Integer,String>();
        countryList.put(1,"Canada");
        countryList.put(2,"USA");
        countryList.put(3,"England");
        countryList.put(4,"Germany");
        countryList.put(5,"India");
        countryList.put(6,"Australia");

        return countryList;
    }
}
