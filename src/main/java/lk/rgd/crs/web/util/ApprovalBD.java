package lk.rgd.crs.web.util;

import lk.rgd.crs.api.domain.BirthRegisterApproval;

import java.util.ArrayList;
import java.sql.Date;

/**
 * @author Indunil Moremada
 */
public class ApprovalBD {

    private ArrayList<BirthRegisterApproval> birthRegisterApproval = new ArrayList();
    private ArrayList<BirthRegisterApproval> birthRegisterApprovalExpired = new ArrayList();

    /**
     * populate an array list with tempory data
     */
    public ArrayList<BirthRegisterApproval> createArrayList() {
        BirthRegisterApproval BRA = new BirthRegisterApproval();
        BirthRegisterApproval BRA1 = new BirthRegisterApproval();
        BirthRegisterApproval BRA2 = new BirthRegisterApproval();
        BirthRegisterApproval BRA3 = new BirthRegisterApproval();
        BirthRegisterApproval BRA4 = new BirthRegisterApproval();
        BirthRegisterApproval BRA5 = new BirthRegisterApproval();
        BirthRegisterApproval BRA6 = new BirthRegisterApproval();
        BirthRegisterApproval BRA7 = new BirthRegisterApproval();
        BirthRegisterApproval BRA8 = new BirthRegisterApproval();
        BirthRegisterApproval BRA9 = new BirthRegisterApproval();
        BirthRegisterApproval BRA10 = new BirthRegisterApproval();
        BirthRegisterApproval BRA11 = new BirthRegisterApproval();

        BRA.setSerial(10);
        BRA.setName("Duminda");
        BRA.setChanges(true);
        BRA.setRecievedDate(new Date(110, 10, 24));
        BRA.setActions("Approve");
        birthRegisterApproval.add(BRA);

        BRA1.setSerial(2);
        BRA1.setName("Indunil");
        BRA1.setChanges(true);
        BRA1.setRecievedDate(new Date(110, 05, 12));
        BRA1.setActions("Approve");
        birthRegisterApproval.add(BRA1);

        BRA2.setSerial(3);
        BRA2.setName("Amith");
        BRA2.setChanges(false);
        BRA2.setRecievedDate(new Date(108, 03, 02));
        BRA2.setActions("Expired");
        birthRegisterApproval.add(BRA2);

        BRA3.setSerial(5);
        BRA3.setName("kamal");
        BRA3.setChanges(true);
        BRA3.setRecievedDate(new Date(110, 10, 24));
        BRA3.setActions("Approve");
        birthRegisterApproval.add(BRA3);

        BRA4.setSerial(4);
        BRA4.setName("Nimal");
        BRA4.setChanges(true);
        BRA4.setRecievedDate(new Date(107, 01, 01));
        BRA4.setActions("Approve");
        birthRegisterApproval.add(BRA4);

        BRA5.setSerial(8);
        BRA5.setName("sunil");
        BRA5.setChanges(true);
        BRA5.setRecievedDate(new Date(108, 10, 5));
        BRA5.setActions("Expired");
        birthRegisterApproval.add(BRA5);

        BRA6.setSerial(17);
        BRA6.setName("jagath");
        BRA6.setChanges(true);
        BRA6.setRecievedDate(new Date(110, 04, 20));
        BRA6.setActions("Expired");
        birthRegisterApproval.add(BRA6);

        BRA7.setSerial(16);
        BRA7.setName("sampath");
        BRA7.setChanges(true);
        BRA7.setRecievedDate(new Date(110, 02, 19));
        BRA7.setActions("Expired");
        birthRegisterApproval.add(BRA7);

        BRA8.setSerial(21);
        BRA8.setName("nuwan");
        BRA8.setChanges(false);
        BRA8.setRecievedDate(new Date(108, 10, 5));
        BRA8.setActions("Expired");
        birthRegisterApproval.add(BRA8);

        BRA9.setSerial(36);
        BRA9.setName("namal");
        BRA9.setChanges(true);
        BRA9.setRecievedDate(new Date(110, 10, 24));
        BRA9.setActions("Expired");
        birthRegisterApproval.add(BRA9);

        BRA10.setSerial(60);
        BRA10.setName("sumith");
        BRA10.setChanges(true);
        BRA10.setRecievedDate(new Date(110, 05, 05));
        BRA10.setActions("Approve");
        birthRegisterApproval.add(BRA10);

        BRA11.setSerial(3);
        BRA11.setName("jagath");
        BRA11.setChanges(false);
        BRA11.setRecievedDate(new Date(108, 03, 02));
        BRA11.setActions("Approve");
        birthRegisterApproval.add(BRA11);

        return birthRegisterApproval;
    }

    /**
     * filter the birthRegisterApproval with the expired data and return
     * filtered ArrayList birthRegisterApprovalExpired
     *
     * @param birthRegisterApproval is an ArrayList which holds
     *                              all the Approval data of the BirthRegistration
     * @return birthRegisterApprovalExpired which is having filtered
     *         expired BirthRegistrationData
     */
    public ArrayList<BirthRegisterApproval> getExpiredData(ArrayList<BirthRegisterApproval> birthRegisterApproval) {
        for (BirthRegisterApproval bra : birthRegisterApproval) {
            if (bra.getActions().equals("Expired")) {
                birthRegisterApprovalExpired.add(bra);
            }
        }
        return birthRegisterApprovalExpired;
    }
}
