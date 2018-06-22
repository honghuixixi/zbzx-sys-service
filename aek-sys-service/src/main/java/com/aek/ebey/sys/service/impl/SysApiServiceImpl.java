package com.aek.ebey.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.ebey.sys.mapper.SysApiMapper;
import com.aek.ebey.sys.model.SysApi;
import com.aek.ebey.sys.service.SysApiService;

/**
 * <p>
 * 系统api表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysApiServiceImpl extends BaseServiceImpl<SysApiMapper, SysApi> implements SysApiService {

}
