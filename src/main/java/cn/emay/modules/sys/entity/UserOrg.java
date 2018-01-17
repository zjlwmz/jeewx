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
 * @Title 用户-组织机构 实体
 * @author zjlwm
 * @date 2017-2-20 上午11:53:17
 *
 */
@Entity
@Table(name = "sys_user_org")
public class UserOrg extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private User user;
	
    private Depart depart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User depart) {
        this.user = depart;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    public Depart getDepart() {
        return depart;
    }

    public void setDepart(Depart depart) {
        this.depart = depart;
    }
}
