package cn.emay.modules.sys.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;


/**
 * 
 * @Title 部门机构表
 * @author zjlwm
 * @date 2017-2-20 上午11:47:37
 *
 */
@Entity
@Table(name = "sys_depart")
public class Depart extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Depart depart;//上级部门
	
	@Excel(exportName = "部门名称")
	private String departname;//部门名称
	
	@Excel(exportName = "部门描述")
	private String description;//部门描述
	
	@Excel(exportName = "机构编码")
    private String orgCode;//机构编码
	
	@Excel(exportName = "机构类型编码")
    private String orgType;//机构编码
	
	@Excel(exportName = "电话")
	private String mobile;//电话
	
	@Excel(exportName = "传真")
	private String fax;//传真
	
	@Excel(exportName = "地址")
	private String address;//地址
	
	private List<Depart> departs = new ArrayList<Depart>();//下属部门

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentdepartid")
	public Depart getDepart() {
		return this.depart;
	}

	public void setDepart(Depart depart) {
		this.depart = depart;
	}

	@Column(name = "departname", nullable = false, length = 100)
	public String getDepartname() {
		return this.departname;
	}

	public void setDepartname(String departname) {
		this.departname = departname;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depart")
	public List<Depart> getDeparts() {
		return departs;
	}

	public void setdeparts(List<Depart> departs) {
		this.departs = departs;
	}

    @Column(name = "org_code", length = 64)
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Column(name = "org_type", length = 1)
    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

	@Column(name = "mobile", length = 32)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "fax", length = 32)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "address", length = 100)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}