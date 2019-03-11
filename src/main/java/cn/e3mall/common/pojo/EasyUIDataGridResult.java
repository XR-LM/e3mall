package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;
/**
 * 翻页辅助类
 * @author XR
 *
 */
public class EasyUIDataGridResult implements Serializable {

    private Integer total;

    private List<?> rows;

    public EasyUIDataGridResult(Integer total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }
    public EasyUIDataGridResult(){}

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

}
