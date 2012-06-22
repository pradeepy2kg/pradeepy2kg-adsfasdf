package lk.rgd.common.api.domain;

/**
 * @author: shan
 */
public class CommonStatistics {

    private String statType;
    private String userRole;

    private int totalSubmissions;
    private int lateSubmissions;
    private int normalSubmissions;
    private int approvedItems;
    private int rejectedItems;
    private int totalPendingItems;
    private int arrearsPendingItems;
    private int thisMonthPendingItems;

    private int deletedItems;                       // Archived items
    private int confirmationPrintedItems;           // Birth confirmation printed items
    private int confirmationApprovalPendingItems;   // Birth confirmation captured and approval pending items
    private int confirmationApprovedItems;          // Birth confirmation approved items
    private int certificateGeneratedItems;          // Birth certificate generated items
    private int certificatePrintedItems;            // Birth, Death certificate printed items

    public CommonStatistics() {
        totalSubmissions = 0;
        lateSubmissions = 0;
        normalSubmissions = 0;
        approvedItems = 0;
        rejectedItems = 0;
        totalPendingItems = 0;
        arrearsPendingItems = 0;
        thisMonthPendingItems = 0;
        deletedItems = 0;
        confirmationPrintedItems = 0;
        confirmationApprovalPendingItems = 0;
        confirmationApprovedItems = 0;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getApprovedItems() {
        return approvedItems;
    }

    public void setApprovedItems(int approvedItems) {
        this.approvedItems = approvedItems;
    }

    public int getArrearsPendingItems() {
        return arrearsPendingItems;
    }

    public void setArrearsPendingItems(int arrearsPendingItems) {
        this.arrearsPendingItems = arrearsPendingItems;
    }

    public int getLateSubmissions() {
        return lateSubmissions;
    }

    public void setLateSubmissions(int lateSubmissions) {
        this.lateSubmissions = lateSubmissions;
    }

    public int getNormalSubmissions() {
        return normalSubmissions;
    }

    public void setNormalSubmissions(int normalSubmissions) {
        this.normalSubmissions = normalSubmissions;
    }

    public int getRejectedItems() {
        return rejectedItems;
    }

    public void setRejectedItems(int rejectedItems) {
        this.rejectedItems = rejectedItems;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }

    public int getThisMonthPendingItems() {
        return thisMonthPendingItems;
    }

    public void setThisMonthPendingItems(int thisMonthPendingItems) {
        this.thisMonthPendingItems = thisMonthPendingItems;
    }

    public int getTotalPendingItems() {
        return totalPendingItems;
    }

    public void setTotalPendingItems(int totalPendingItems) {
        this.totalPendingItems = totalPendingItems;
    }

    public int getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(int totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }

    public int getDeletedItems() {
        return deletedItems;
    }

    public void setDeletedItems(int deletedItems) {
        this.deletedItems = deletedItems;
    }

    public int getConfirmationPrintedItems() {
        return confirmationPrintedItems;
    }

    public void setConfirmationPrintedItems(int confirmationPrintedItems) {
        this.confirmationPrintedItems = confirmationPrintedItems;
    }

    public int getConfirmationApprovalPendingItems() {
        return confirmationApprovalPendingItems;
    }

    public void setConfirmationApprovalPendingItems(int confirmationApprovalPendingItems) {
        this.confirmationApprovalPendingItems = confirmationApprovalPendingItems;
    }

    public int getConfirmationApprovedItems() {
        return confirmationApprovedItems;
    }

    public void setConfirmationApprovedItems(int confirmationApprovedItems) {
        this.confirmationApprovedItems = confirmationApprovedItems;
    }

    public int getCertificateGeneratedItems() {
        return certificateGeneratedItems;
    }

    public void setCertificateGeneratedItems(int certificateGeneratedItems) {
        this.certificateGeneratedItems = certificateGeneratedItems;
    }

    public int getCertificatePrintedItems() {
        return certificatePrintedItems;
    }

    public void setCertificatePrintedItems(int certificatePrintedItems) {
        this.certificatePrintedItems = certificatePrintedItems;
    }

    public void add(CommonStatistics cs) {
        this.totalSubmissions += cs.totalSubmissions;
        this.lateSubmissions += cs.lateSubmissions;
        this.normalSubmissions += cs.normalSubmissions;
        this.approvedItems += cs.approvedItems;
        this.rejectedItems += cs.rejectedItems;
        this.totalPendingItems += cs.totalPendingItems;
        this.arrearsPendingItems += cs.arrearsPendingItems;
        this.thisMonthPendingItems += cs.thisMonthPendingItems;
        this.deletedItems += cs.deletedItems;
        this.confirmationPrintedItems += cs.confirmationPrintedItems;
        this.confirmationApprovalPendingItems += confirmationApprovalPendingItems;
        this.confirmationApprovedItems += confirmationApprovedItems;
    }
}
