package com.aek.ebey.sys.model.bo;

import java.io.Serializable;

/**
 * 文件上传进度
 * 
 * @author Mr.han
 *
 */
public class UploadRate implements Serializable {

	private static final long serialVersionUID = 8196018912532943108L;

	/**
	 * 总数量
	 */
	private Integer totalNum;

	/**
	 * 上传成功数量
	 */
	private Integer succesNum = 0;

	/**
	 * 上传失败数量
	 */
	private Integer errorNum = 0;
	
	/**
	 * 处理进度百分比
	 */
	private Double rate;

	public UploadRate() {
		super();
	}
	
	public UploadRate(Integer totalNum) {
		super();
		this.totalNum = totalNum;
	}

	public UploadRate(Integer totalNum, Integer succesNum, Integer errorNum) {
		super();
		this.totalNum = totalNum;
		this.succesNum = succesNum;
		this.errorNum = errorNum;
	}

	public UploadRate(Integer succesNum, Integer errorNum) {
		super();
		this.succesNum = succesNum;
		this.errorNum = errorNum;
	}

	/**
	 * @return the succesNum
	 */
	public Integer getSuccesNum() {
		return succesNum;
	}

	/**
	 * @param succesNum
	 *            the succesNum to set
	 */
	public void setSuccesNum(Integer succesNum) {
		this.succesNum = succesNum;
	}

	/**
	 * @return the errorNum
	 */
	public Integer getErrorNum() {
		return errorNum;
	}

	/**
	 * @param errorNum
	 *            the errorNum to set
	 */
	public void setErrorNum(Integer errorNum) {
		this.errorNum = errorNum;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "UploadRate [totalNum=" + totalNum + ", succesNum=" + succesNum + ", errorNum=" + errorNum + ", rate="
				+ rate + "]";
	}
	
}
