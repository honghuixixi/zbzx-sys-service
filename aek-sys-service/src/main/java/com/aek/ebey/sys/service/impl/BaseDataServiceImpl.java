package com.aek.ebey.sys.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.ebey.sys.enums.export.StaticVariable;
import com.aek.ebey.sys.service.BaseDataService;

@Service
@Transactional
public class BaseDataServiceImpl implements BaseDataService {

	@Override
	public Map<String, Map<Object, String>> findStaticVariable() {
		return StaticVariable.getVariablemap();
	}

}
