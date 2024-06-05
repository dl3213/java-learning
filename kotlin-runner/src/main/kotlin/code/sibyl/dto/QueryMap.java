package code.sibyl.dto;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 租赁系统常规查询使用参数
 */

public class QueryMap extends JSONObject {

    public static QueryMap builder() {
        return new QueryMap();
    }

    //避免参数污染
    public static QueryMap base(QueryMap queryMap) {
        QueryMap map = new QueryMap();
        map.putAll(queryMap);
        return map;
    }

    public QueryMap regionCode(String regionCode) {
        this.put("regionCode", regionCode);
        return this;
    }

    public QueryMap orgCode(String orgCode) {
        this.put("orgCode", orgCode);
        return this;
    }

    public QueryMap storeCode(String storeCode) {
        this.put("storeCode", storeCode);
        return this;
    }

    public QueryMap warehousingWarehouse(String warehousingWarehouse) {
        this.put("warehousingWarehouse", warehousingWarehouse);
        return this;
    }

    public QueryMap storeCodeList(List<String> storeCodeList) {
        this.put("storeCodeList", storeCodeList);
        return this;
    }

    public QueryMap financeMonth(String financeMonth) {
        this.put("financeMonth", financeMonth);
        return this;
    }

    public QueryMap deptName(String deptName) {
        this.put("deptName", deptName);
        return this;
    }

    public QueryMap deptCode(String deptCode) {
        this.put("deptCode", deptCode);
        return this;
    }

    public QueryMap orgCodeList(List<String> orgCodeList) {
        this.put("orgCodeList", orgCodeList);
        return this;
    }

    public QueryMap customerId(String customerId) {
        this.put("customerId", customerId);
        return this;
    }

    public QueryMap customerCode(String customerCode) {
        this.put("customerCode", customerCode);
        return this;
    }

    public QueryMap customerName(String customerName) {
        this.put("customerName", customerName);
        return this;
    }

    public QueryMap supplierCode(String supplierCode) {
        this.put("supplierCode", supplierCode);
        return this;
    }

    public QueryMap supplierName(String supplierName) {
        this.put("supplierName", supplierName);
        return this;
    }

    public QueryMap className(String className) {
        this.put("className", className);
        return this;
    }

    public QueryMap targetDate(String targetDate) {
        this.put("targetDate", targetDate);
        return this;
    }

    public QueryMap startDate(String startDate) {
        this.put("startDate", startDate);
        return this;
    }

    public QueryMap endDate(String endDate) {
        this.put("endDate", endDate);
        return this;
    }

    public QueryMap yyyy_MM_dd(String yyyy_MM_dd) {
        this.put("yyyy_MM_dd", yyyy_MM_dd);
        return this;
    }

    public QueryMap yyyy_MM(String yyyy_MM) {
        this.put("yyyy_MM", yyyy_MM);
        return this;
    }

    public QueryMap yyyy(String yyyy) {
        this.put("yyyy", yyyy);
        return this;
    }

    public QueryMap MM(String MM) {
        this.put("MM", MM);
        return this;
    }

    public QueryMap MM_start(String MM_start) {
        this.put("MM_start", MM_start);
        return this;
    }

    public QueryMap MM_end(String MM_end) {
        this.put("MM_end", MM_end);
        return this;
    }

    public QueryMap dd(String dd) {
        this.put("dd", dd);
        return this;
    }

    public QueryMap salesManName(String salesmanName) {
        this.put("salesManName", salesmanName);
        return this;
    }

    public QueryMap subjectCode(String subjectCode) {
        this.put("subjectCode", subjectCode);
        return this;
    }

    public QueryMap docCode(String docCode) {
        this.put("docCode", docCode);
        return this;
    }

    public QueryMap subjectCodeList(String... subjectCodeList) {
        this.put("subjectCodeList", subjectCodeList);
        return this;
    }

    public QueryMap notSubjectCodeList(String... notSubjectCodeList) {
        this.put("notSubjectCodeList", notSubjectCodeList);
        return this;
    }

    public QueryMap subjectName(String subjectName) {
        this.put("subjectName", subjectName);
        return this;
    }

    public QueryMap itemName(String itemName) {
        this.put("itemName", itemName);
        return this;
    }

    public QueryMap itemNameList(List<String> itemNameList) {
        this.put("itemNameList", itemNameList);
        return this;
    }

    public QueryMap itemNameList(String... itemNameList) {
        this.put("itemNameList", Arrays.stream(itemNameList).collect(Collectors.toList()));
        return this;
    }

    public QueryMap contractCode(String contractCode) {
        this.put("contractCode", contractCode);
        return this;
    }

    public QueryMap projectName(String projectName) {
        this.put("projectName", projectName);
        return this;
    }

    //material_code
    public QueryMap matCode(String matCode) {
        this.put("matCode", matCode);
        return this;
    }

    public QueryMap materialCode(String materialCode) {
        this.put("materialCode", materialCode);
        return this;
    }

    public QueryMap materialCodeLike(String materialCodeLike) {
        this.put("materialCodeLike", materialCodeLike);
        return this;
    }

    public QueryMap contractCodeList(List<String> contractCodeList) {
        this.put("contractCodeList", contractCodeList);
        return this;
    }

    public QueryMap businessModel(String businessModel) {
        this.put("businessModel", businessModel);
        return this;
    }

    public QueryMap upstreamCode(String upstreamCode) {
        this.put("upstreamCode", upstreamCode);
        return this;
    }

    public QueryMap isKey(String isKey) {
        this.put("isKey", isKey);
        return this;
    }

    public QueryMap code(String code) {
        this.put("code", code);
        return this;
    }

    public QueryMap codeList(List<String> codeList) {
        this.put("codeList", codeList);
        return this;
    }

    public QueryMap select(String select) {
        this.put("select", select);
        return this;
    }

    public QueryMap notInner(boolean notInner) {
        this.put("notInner", notInner);
        return this;
    }

    public QueryMap notCheck(boolean notCheck) {
        this.put("notCheck", notCheck);
        return this;
    }

    public QueryMap startTime(Date startTime) {
        this.put("startTime", startTime);
        return this;
    }

    public QueryMap endTime(Date endTime) {
        this.put("endTime", endTime);
        return this;
    }

    public QueryMap salesContract(String salesContract) {
        this.put("salesContract", salesContract);
        return this;
    }

    public QueryMap sql(String sql) {
        this.put("sql", sql);
        return this;
    }

    public QueryMap amount(BigDecimal amount) {
        this.put("amount", amount);
        return this;
    }

    public QueryMap explanation(String... str) {
        this.put("explanation", Arrays.stream(str).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList()));
        return this;
    }

    public QueryMap region(String region) {
        this.put("region", region);
        return this;
    }
}
