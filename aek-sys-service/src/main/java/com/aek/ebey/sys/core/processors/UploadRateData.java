package com.aek.ebey.sys.core.processors;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.aek.common.core.config.RedisRepository;
import com.aek.ebey.sys.model.bo.UploadRate;
import com.alibaba.fastjson.JSON;

/**
 * Excel上传进度共享数据
 *	
 * @author HongHui
 * @date   2017年8月7日
 */
public class UploadRateData {

	private ReadWriteLock rwl = new ReentrantReadWriteLock();     
	
	// 缓存操作对象
	private RedisRepository redisRepository;
	// 机构数据上传进度数据放入缓存Key
	private String importRateRedisKey;
	
	public UploadRateData(RedisRepository redisRepository, String importRateRedisKey) {
		this.redisRepository = redisRepository;
		this.importRateRedisKey = importRateRedisKey;
	}

	/**
	 * EXCEL机构导入，成功数量+1
	 */
	public void successNumIncrement(){
		rwl.writeLock().lock();
		try{
			UploadRate uploadRate = JSON.parseObject(redisRepository.get(importRateRedisKey), UploadRate.class);
			uploadRate.setSuccesNum(uploadRate.getSuccesNum()+1);
			uploadRate.setRate(((uploadRate.getSuccesNum()+uploadRate.getErrorNum()) * 1.0) / uploadRate.getTotalNum());
			redisRepository.set(importRateRedisKey, JSON.toJSONString(uploadRate));
		}finally{
			rwl.writeLock().unlock();
		}
	}
	
	/**
	 * EXCEL机构导入，失败数量+1
	 */
	public void errorNumIncrement(){
		rwl.writeLock().lock();
		try{
			UploadRate uploadRate = JSON.parseObject(redisRepository.get(importRateRedisKey), UploadRate.class);
			uploadRate.setErrorNum(uploadRate.getErrorNum()+1);
			uploadRate.setRate(((uploadRate.getSuccesNum()+uploadRate.getErrorNum()) * 1.0) / uploadRate.getTotalNum());
			redisRepository.set(importRateRedisKey, JSON.toJSONString(uploadRate));
		}finally{
			rwl.writeLock().unlock();
		}
	}
	
	/**
	 * EXCEL机构导入，总数量-1
	 */
	public void totalNumDecrement(){
		rwl.writeLock().lock();
		try{
			UploadRate uploadRate = JSON.parseObject(redisRepository.get(importRateRedisKey), UploadRate.class);
			uploadRate.setTotalNum(uploadRate.getTotalNum()-1);
			uploadRate.setRate(((uploadRate.getSuccesNum()+uploadRate.getErrorNum()) * 1.0) / uploadRate.getTotalNum());
			redisRepository.set(importRateRedisKey, JSON.toJSONString(uploadRate));
		}finally{
			rwl.writeLock().unlock();
		}
	}
}
