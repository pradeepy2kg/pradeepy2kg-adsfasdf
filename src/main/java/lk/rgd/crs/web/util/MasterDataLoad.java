package lk.rgd.crs.web.util;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Chathuranga
 *
 * MasterDataLoad is a Singleton class used to load data to user ineterfaces.         * 
 */
public class MasterDataLoad {
    private static MasterDataLoad masterDataLoad;

    /** MasterDataLoadConstructor */
    private MasterDataLoad() {
    }

    /**
     *  Get an instance of MasterDataLoad if not initialized otherwise gives a reference
     * @return  MasterDataLoad object
     */
    public static MasterDataLoad getInstance(){
        if(masterDataLoad==null){
            synchronized (MasterDataLoad.class){
                masterDataLoad=new MasterDataLoad();
            }
        }
        return masterDataLoad;
    }

    /**
     *   Returns list of available Districts
     * @return list of districts
     */
    public List<String> loadDistricts(){
        // hard coded for the moment should be loaded from the database
        List<String> districtList=new ArrayList<String>();
        districtList.add("Colombo");
        districtList.add("Kalutara");
        districtList.add("Gampaha");
        districtList.add("Galle");
        districtList.add("Matara");
        districtList.add("Hambantota");
        districtList.add("Jaffna");
        districtList.add("Matale");
        districtList.add("Anuradhapura");

        return districtList;
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
