package cn.emay.modules.sys.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;
/**
 * 
 * @Title 角色-组织机构 实体
 * @author zjlwm
 * @date 2017-2-20 上午11:50:37
 *
 */
@Entity
@Table(name = "sys_role_org")
public class RoleOrg extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private Depart depart;
    
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    public Depart getDepart() {
        return depart;
    }

    public void setDepart(Depart depart) {
        this.depart = depart;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
