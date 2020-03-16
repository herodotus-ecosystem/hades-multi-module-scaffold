package cn.com.felix.common.basic.domain;

import cn.com.felix.core.enums.StateEnum;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

/**
 * <p>Description: 脚手架核心功能基础实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/3 16:25
 */
@MappedSuperclass
public abstract class BaseSysDomain extends BaseDomain {

    @Column(name = "state")
    private StateEnum state = StateEnum.NORMAL;

    @Column(name = "available")
    private Boolean available = Boolean.TRUE;

    @Column(name = "reversion")
    @OrderBy("reversion asc")
    private Integer reversion = 0;

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getReversion() {
        return reversion;
    }

    public void setReversion(Integer reversion) {
        this.reversion = reversion;
    }
}
