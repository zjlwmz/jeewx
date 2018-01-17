/**===========================================
 *        Copyright (C) 2014 Tempus
 *           All rights reserved
 *
 *  项 目 名： jeecg-framework
 *  文 件 名： DictEntity.java
 *  版本信息： V1.0.0 
 *  作    者： Administrator
 *  日    期： 2014年5月11日-上午1:57:29
 * 
 ============================================*/

package cn.emay.modules.sys.entity;


/**
 * 
 * @Title 
 * @author zjlwm
 * @date 2017-2-20 上午11:47:47
 *
 */
public class DictEntity {
	private String typecode;
	private String typename;
	
	public String getTypecode() {
		return typecode;
	}
	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
}
