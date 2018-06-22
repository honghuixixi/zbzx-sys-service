package com.aek.ebey.sys.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.ebey.sys.mapper.SysUserModuleMapper;
import com.aek.ebey.sys.model.SysUserModule;
import com.aek.ebey.sys.service.SysUserModuleService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * <p>
 * 用户模块信息 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-27
 */
@Service
@Transactional
public class SysUserModuleServiceImpl extends BaseServiceImpl<SysUserModuleMapper, SysUserModule>
		implements SysUserModuleService {

	@Override
	public void modifySysUserModuleAll(List<SysUserModule> sysUserModuleList) {
		if (CollectionUtils.isNotEmpty(sysUserModuleList)) {

			Wrapper<SysUserModule> wrap = new EntityWrapper<SysUserModule>();
			wrap.eq("user_id", sysUserModuleList.get(0).getUserId());
			// 根据user_id获取用户模块信息
			List<SysUserModule> dataList = this.selectList(wrap);

			if (CollectionUtils.isNotEmpty(dataList)) {
				// 判断前端穿来的用户模块信息跟数据库中进行比较
				if (sysUserModuleList.size() >= dataList.size()) {

					for (SysUserModule before_sysUserModule : sysUserModuleList) {

						Wrapper<SysUserModule> wrapper = new EntityWrapper<SysUserModule>();
						wrapper.eq("user_id", before_sysUserModule.getUserId()).eq("module_id",
								before_sysUserModule.getModuleId());
						// 根据用户user_id和module_id查询出结果
						SysUserModule date_sysModule = this.selectOne(wrapper);
						
						if (date_sysModule != null) {
							updateSysUserModule(before_sysUserModule, date_sysModule);
						} else {
							// 新增模块
							this.insert(before_sysUserModule);
						}

					}

				} else {
					//前端返回数据
					List<String> f_list = new ArrayList<String>();
					//数据库中数据
					Set<String> d_set=new HashSet<String>();
					//需要删除的数据
					List<SysUserModule> del_list = new ArrayList<SysUserModule>();
					//for (SysUserModule date_sysModule : dataList ) {
					for (SysUserModule before_sysUserModule:sysUserModuleList) {
						f_list.add(before_sysUserModule.getUserId() + "_" + before_sysUserModule.getModuleId());
						for (SysUserModule date_sysModule : dataList ) {
							
							d_set.add(date_sysModule.getUserId() + "_" + date_sysModule.getModuleId());
							
							if (date_sysModule.getModuleId() == before_sysUserModule.getModuleId()) {
								updateSysUserModule(before_sysUserModule, date_sysModule);
							}

						}

					}
					for (String ddate : d_set) {
						if (!f_list.contains(ddate)) {
							SysUserModule sysUserModule = new SysUserModule();
							String str[] = ddate.split("_");
							sysUserModule.setUserId(Long.parseLong(str[0]));
							sysUserModule.setModuleId(Long.parseLong(str[1]));
							del_list.add(sysUserModule);
						}
					}
					// 删除
					if (del_list.size() > 0) {
						for (SysUserModule date_sysModule : del_list) {
							Wrapper<SysUserModule> delete_wrapper = new EntityWrapper<SysUserModule>();
							delete_wrapper.eq("user_id", date_sysModule.getUserId()).eq("module_id",
									date_sysModule.getModuleId());
							this.delete(delete_wrapper);
						}

					}

				}

			} else {
				//批量插入
				this.insertBatch(sysUserModuleList);
				/*for (SysUserModule before_sysUserModule : sysUserModuleList) {
					// 新增模块
					this.insert(before_sysUserModule);

				}*/
			}

		}
	}

	// 修改个人应用数据
	private void updateSysUserModule(SysUserModule before_sysUserModule, SysUserModule date_sysModule) {
		if (date_sysModule.getModuleGroup().intValue() != before_sysUserModule.getModuleGroup().intValue()
				|| date_sysModule.getModuleOrder().intValue() != before_sysUserModule.getModuleOrder().intValue()) {
			before_sysUserModule.setId(date_sysModule.getId());
			this.updateById(before_sysUserModule);
		}
	}

}
