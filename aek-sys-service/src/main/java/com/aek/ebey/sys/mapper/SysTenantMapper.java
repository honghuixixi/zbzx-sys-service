package com.aek.ebey.sys.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.Tenants;
import com.aek.ebey.sys.model.query.TenantQuery;
import com.aek.ebey.sys.model.vo.SysTenantRoleVo;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysTenantMapper extends BaseMapper<SysTenant> {
	
	List<Tenants> getTenants(@Param("tenantIds") Long[] tenantIds);

	List<SysTenant> getPage(Page<SysTenant> page, @Param("query") TenantQuery query);

	List<SysTenant> selectByOrgCode(@Param("orgCode") String orgCode);
	
	List<SysTenant> selectExpiredTenant();

	List<SysTenant> selectExpiredTenantBetweenDate(@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);
	
	List<SysTenantRoleVo> findAllRoleSubTenant(@Param("tenantId") Long tenantId,@Param("parentIds") String parentIds);
}