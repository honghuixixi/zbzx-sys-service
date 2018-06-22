package com.aek.ebey.sys.enums.export;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.aek.ebey.sys.enums.ClientType;
import com.aek.ebey.sys.enums.ModuleSource;
import com.aek.ebey.sys.enums.ModuleType;
import com.aek.ebey.sys.enums.RoleDataScope;
import com.aek.ebey.sys.enums.TenantAccountType;
import com.aek.ebey.sys.enums.TenantAuditStatus;
import com.aek.ebey.sys.enums.TenantCategory;
import com.aek.ebey.sys.enums.TenantEconomicType;
import com.aek.ebey.sys.enums.TenantGrade;
import com.aek.ebey.sys.enums.TenantHierarchy;
import com.aek.ebey.sys.enums.TenantManageType;
import com.aek.ebey.sys.enums.TenantOrigin;
import com.aek.ebey.sys.enums.TenantTrial;
import com.aek.ebey.sys.enums.TenantType;

/**
 * 全局静态枚举类型变量表
 * 
 * @author Mr.han
 *
 */
public class StaticVariable {

	/** 全局枚举变量 */
	private static final Map<String, Map<Object, String>> variableMap = new HashMap<String, Map<Object, String>>();

	static {

		/*-------------------用户登录客户端类型--------------------*/
		Map<Object, String> clientTypeMap = new LinkedHashMap<Object, String>();
		for (ClientType clientType : ClientType.values()) {
			clientTypeMap.put(clientType.getNumber(), clientType.getTitle());
		}
		getVariablemap().put("clientType", clientTypeMap);

		/*--------------------模块来源-----------------------------*/
		Map<Object, String> moduleSourceMap = new LinkedHashMap<Object, String>();
		for (ModuleSource moduleSource : ModuleSource.values()) {
			moduleSourceMap.put(moduleSource.getNumber(), moduleSource.getTitle());
		}
		getVariablemap().put("moduleSource", moduleSourceMap);

		/*--------------------模块类型-----------------------------*/
		Map<Object, String> moduleTypeMap = new LinkedHashMap<Object, String>();
		for (ModuleType moduleType : ModuleType.values()) {
			moduleTypeMap.put(moduleType.getNumber(), moduleType.getTitle());
		}
		getVariablemap().put("moduleType", moduleTypeMap);

		/*--------------------租户账户类型-----------------------------*/
		Map<Object, String> tenantAccountTypeMap = new LinkedHashMap<Object, String>();
		for (TenantAccountType TenantAccountType : TenantAccountType.values()) {
			tenantAccountTypeMap.put(TenantAccountType.getNumber(), TenantAccountType.getTitle());
		}
		getVariablemap().put("tenantAccountType", tenantAccountTypeMap);

		/*--------------------租户审核状态-----------------------------*/
		Map<Object, String> tenantAuditStatusMap = new LinkedHashMap<Object, String>();
		for (TenantAuditStatus tenantAuditStatus : TenantAuditStatus.values()) {
			tenantAuditStatusMap.put(tenantAuditStatus.getNumber(), tenantAuditStatus.getTitle());
		}
		getVariablemap().put("tenantAuditStatus", tenantAuditStatusMap);

		/*--------------------租户经济类型-----------------------------*/
		Map<Object, String> tnantEconomicTypeMap = new LinkedHashMap<Object, String>();
		for (TenantEconomicType tenantEconomicType : TenantEconomicType.values()) {
			tnantEconomicTypeMap.put(tenantEconomicType.getNumber(), tenantEconomicType.getTitle());
		}
		getVariablemap().put("tenantEconomicType", tnantEconomicTypeMap);

		/*--------------------租户等次-----------------------------*/
		Map<Object, String> tenantHierarchyMap = new LinkedHashMap<Object, String>();
		for (TenantHierarchy tenantHierarchy : TenantHierarchy.values()) {
			tenantHierarchyMap.put(tenantHierarchy.getNumber(), tenantHierarchy.getTitle());
		}
		getVariablemap().put("tenantHierarchy", tenantHierarchyMap);

		/*--------------------租户等级-----------------------------*/
		Map<Object, String> tenantGradeMap = new LinkedHashMap<Object, String>();
		for (TenantGrade tenantGrade : TenantGrade.values()) {
			tenantGradeMap.put(tenantGrade.getNumber(), tenantGrade.getTitle());
		}
		getVariablemap().put("tenantGrade", tenantGradeMap);

		/*--------------------租户管理类型-----------------------------*/
		Map<Object, String> tenantManageTypeMap = new LinkedHashMap<Object, String>();
		for (TenantManageType tenantManageType : TenantManageType.values()) {
			tenantManageTypeMap.put(tenantManageType.getNumber(), tenantManageType.getTitle());
		}
		getVariablemap().put("tenantManageType", tenantManageTypeMap);

		/*---------------------租户类别------------------------------------*/
		Map<Object, String> tenantCategoryMap = new LinkedHashMap<Object, String>();
		for (TenantCategory tenantCategory : TenantCategory.values()) {
			tenantCategoryMap.put(tenantCategory.getNumber(), tenantCategory.getTitle());
		}
		getVariablemap().put("tenantCategory", tenantCategoryMap);

		/*--------------------租户来源-----------------------------*/
		Map<Object, String> tenantOriginMap = new LinkedHashMap<Object, String>();
		for (TenantOrigin tenantOrigin : TenantOrigin.values()) {
			tenantOriginMap.put(tenantOrigin.getNumber(), tenantOrigin.getTitle());
		}
		getVariablemap().put("tanentOrigin", tenantOriginMap);

		/*--------------------租户类型-----------------------------*/
		Map<Object, String> tenantTypeMap = new LinkedHashMap<Object, String>();
		for (TenantType tenantType : TenantType.values()) {
			if (tenantType.getNumber() == 1) {
				tenantTypeMap.put(tenantType.getNumber(), tenantType.getTitle());
			}
		}
		getVariablemap().put("tenantType", tenantTypeMap);

		/*--------------------租户是否测试-----------------------------*/
		Map<Object, String> tenantTrialMap = new LinkedHashMap<Object, String>();
		for (TenantTrial tenantTrial : TenantTrial.values()) {
			tenantTrialMap.put(tenantTrial.getNumber(), tenantTrial.getTitle());
		}
		getVariablemap().put("tenantTrial", tenantTrialMap);

		/*--------------------角色数据范围-----------------------------*/
		Map<Object, String> roleDataScopeMap = new LinkedHashMap<Object, String>();
		for (RoleDataScope roleDataScope : RoleDataScope.values()) {
			roleDataScopeMap.put(roleDataScope.getNumber(), roleDataScope.getTitle());
		}
		getVariablemap().put("roleDataScope", roleDataScopeMap);
	}

	public static Map<String, Map<Object, String>> getVariablemap() {
		return variableMap;
	}
}
