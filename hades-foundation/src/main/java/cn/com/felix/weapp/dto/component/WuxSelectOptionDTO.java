/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: WuxSelectOptionDTO
 * Author: gengwei.zheng
 * Date: 19-2-15 下午2:51
 * LastModified: 19-2-15 下午2:51
 */

package cn.com.felix.weapp.dto.component;

import cn.com.felix.common.basic.dto.BaseDTO;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2019/2/15
 */
public class WuxSelectOptionDTO extends BaseDTO {

    private String title;
    private String value;
    private Integer ranking = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("value", value)
                .append("ranking", ranking)
                .toString();
    }
}
