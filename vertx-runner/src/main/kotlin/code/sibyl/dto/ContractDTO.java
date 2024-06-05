package code.sibyl.dto;

//@DataObject
//@RowMapped
public class ContractDTO{
    private String contractCode;
    private String projectName;
    private String orgCode;
    private String orgName;
    private String customerCode;
    private String customerName;
    private String region;
    private String regionName;
    private String deliveryWarehouse;
    private String storeName;
    private String regionCompany;
    private String regionCompanyName;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDeliveryWarehouse() {
        return deliveryWarehouse;
    }

    public void setDeliveryWarehouse(String deliveryWarehouse) {
        this.deliveryWarehouse = deliveryWarehouse;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRegionCompany() {
        return regionCompany;
    }

    public void setRegionCompany(String regionCompany) {
        this.regionCompany = regionCompany;
    }

    public String getRegionCompanyName() {
        return regionCompanyName;
    }

    public void setRegionCompanyName(String regionCompanyName) {
        this.regionCompanyName = regionCompanyName;
    }
}
