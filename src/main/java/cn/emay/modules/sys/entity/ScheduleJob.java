package cn.emay.modules.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 定时任务
 * @author zjlwm
 * @date 2017-2-20 上午11:50:59
 *
 */
@Entity
@Table(name = "sys_schedule_job")
public class ScheduleJob extends IdEntity {

	private static final long serialVersionUID = 1L;
	
	
    /** 任务名称 */
    private String name;
 
    /** 任务分组 */
    private String group;
 
    /**
     * 任务状态 【0 关闭 、 1 开启 】
     */
    private short status;
    
    /**
     * 运行状态【NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED】
     * 没有，正常，暂停，完成，错误，阻塞
     */
    private String state;
 
    /** 任务运行时间表达式 */
    private String cron;
 
    /** 任务描述 */
    private String desc;
    
    /**
     * 
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 
     * 更新时间
     */
    private Date updateDate;
    
    /**
     * 任务执行适配器
     */
    private String jobClass;


	public ScheduleJob(){
    	super();
    }
    
	
	@PrePersist
	public void prePersist(){
		this.createDate=new Date();
		this.updateDate=this.createDate;
	}
	
	@PreUpdate
	public void PreUpdate(){
		this.updateDate=new Date();
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="job_group")
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	@Column(name="job_status")
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	
	@Column(name="cron")
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	
	@Column(name="job_desc")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name="job_class")
	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	@Transient
	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof ScheduleJob){
			return this.getId().equals(((ScheduleJob)obj).getId());
		}
		return false;
	}
	
	
}
