package cn.emay.modules.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * @Title 系统参数
 * @author zjlwm
 * @date 2017-2-7 下午2:43:14
 */
@Entity
@Table(name = "sys_params")
public class Params extends IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 参数类型
	 */
	private String paramsType;
	
	/**
	 * 参数名称
	 */
	private String paramName;

	/**
	 * 参数值
	 */
	private String paramsValue;
	
	/**
	 * 备注
	 */
	private String remarks;
	

	@Column(name="param_name")
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	@Column(name="params_value")
	public String getParamsValue() {
		return paramsValue;
	}

	public void setParamsValue(String paramsValue) {
		this.paramsValue = paramsValue;
	}

	
	@Column(name="remarks")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="params_type")
	public String getParamsType() {
		return paramsType;
	}

	public void setParamsType(String paramsType) {
		this.paramsType = paramsType;
	}

	
	
}
