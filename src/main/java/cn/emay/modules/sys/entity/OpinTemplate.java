package cn.emay.modules.sys.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 
 * @author zjlwm
 * @date 2017-2-20 上午11:49:55
 *
 */
@Entity
@Table(name = "sys_opintemplate")
public class OpinTemplate extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String descript;
	
	@Column(name = "descript", length = 100)
	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	
}