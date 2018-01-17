package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 推送日志记录
 * @author zjlWm
 * @version 2015-4-22
 */
@Entity
@Table(name = "wx_push_log")
public class PushLog  extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	private String name;
	private Date startDate;
	private Date endDate;
	private Integer total;
	private String remarks;
	
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="start_date")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name="end_date")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(name="total")
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	@Column(name="remarks")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
