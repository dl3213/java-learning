package code.sibyl.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 租赁系统常规查询使用参数
 */
public class QueryDTO implements Serializable {

    private List<String> contractCodeList;

    public QueryDTO(){

    }

    public static QueryDTO builder() {
        return new QueryDTO();
    }

    public List<String> getContractCodeList() {
        return contractCodeList;
    }

    public QueryDTO setContractCodeList(List<String> contractCodeList) {
        this.contractCodeList = contractCodeList;
        return this;
    }

}
