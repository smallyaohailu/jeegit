package io.jeegit.tech.dict;

import io.jeegit.common.dao.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 字典项（Dictionary Item）。一个字典类型下的可选值集合。
 * {@code attributes} 以 JSON 字符串存储，为业务方提供结构化扩展位，
 * 例如在分派规则中记录关键词数组、优先级、目标部门编码等。
 */
@Entity
@Table(name = "jg_dict_item",
        indexes = {
                @Index(name = "idx_dict_item_type", columnList = "tenant_id,type_code"),
                @Index(name = "uk_dict_item_kv", columnList = "tenant_id,type_code,item_key", unique = true)
        })
public class DictItem extends TenantAwareEntity {

    @Column(name = "type_code", length = 64, nullable = false)
    private String typeCode;

    @Column(name = "item_key", length = 128, nullable = false)
    private String itemKey;

    @Column(name = "item_label", length = 200, nullable = false)
    private String itemLabel;

    @Column(name = "item_value", length = 500)
    private String itemValue;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 30;

    @Column(name = "attributes", length = 4000)
    private String attributes;

    public DictItem() {
    }

    public DictItem(String typeCode, String itemKey, String itemLabel, String itemValue,
                    int sortOrder, String attributes) {
        this.typeCode = typeCode;
        this.itemKey = itemKey;
        this.itemLabel = itemLabel;
        this.itemValue = itemValue;
        this.sortOrder = sortOrder;
        this.attributes = attributes;
    }

    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public String getItemKey() { return itemKey; }
    public void setItemKey(String itemKey) { this.itemKey = itemKey; }
    public String getItemLabel() { return itemLabel; }
    public void setItemLabel(String itemLabel) { this.itemLabel = itemLabel; }
    public String getItemValue() { return itemValue; }
    public void setItemValue(String itemValue) { this.itemValue = itemValue; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public String getAttributes() { return attributes; }
    public void setAttributes(String attributes) { this.attributes = attributes; }
}
