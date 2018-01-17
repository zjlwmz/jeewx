package cn.emay.framework.core.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
@MappedSuperclass
public abstract class IdEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="id",nullable=false,length=32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
