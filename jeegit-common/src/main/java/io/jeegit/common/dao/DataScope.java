package io.jeegit.common.dao;

/**
 * 数据权限范围（Data Scope）—— 在角色层声明，服务层据此构造查询过滤。
 *
 * 语义参考 jeesite 的分级模型，但用枚举 + 查询规约（Specification）来实现，
 * 避免原生 SQL 拼接。
 */
public enum DataScope {
    /** 全部数据。 */
    ALL,
    /** 本公司（顶级组织）的数据。 */
    COMPANY,
    /** 本公司及其下属组织的数据。 */
    COMPANY_AND_CHILD,
    /** 本部门的数据。 */
    DEPARTMENT,
    /** 本部门及其下属部门的数据。 */
    DEPARTMENT_AND_CHILD,
    /** 仅本人创建的数据。 */
    SELF,
    /** 自定义（由关联的组织 ID 列表决定）。 */
    CUSTOM
}
