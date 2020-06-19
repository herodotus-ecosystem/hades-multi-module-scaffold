/*
 * Copyright (c) 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Project Name: hades-platform
 * Module Name: hades-kernel
 * File Name: TaskDefine.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.quartz;

import lombok.Builder;
import lombok.Data;
import org.quartz.Job;
import org.quartz.JobKey;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by EalenXie on 2019/7/10 16:28.
 * Quartz 里面 一个最简单,最基本的定时任务 应该包含以下必要基本属性
 * 注意 : 这里只是方便演示,故写此类进行简单说明, 可针对自身业务对此类进行扩展
 */
@Data
@Builder
public class TaskDefine {

    /**
     * 定时任务 的名字和分组名 JobKey,{@link org.quartz.JobKey}
     */
    @NotNull(message = "定时任务的 名字 和 组名 坚决不为空")
    private JobKey jobKey;
    /**
     * 定时任务 的描述(可以定时任务本身的描述,也可以是触发器的)
     * {@link org.quartz.JobDetail} description ; {@link org.quartz.Trigger} description
     */
    private String description;
    /**
     * 定时任务 的执行cron (Trigger的CronScheduleBuilder 的cronExpression)
     * {@link org.quartz.Trigger} CronScheduleBuilder {@link org.quartz.CronScheduleBuilder}
     */
    @NotEmpty(message = "定时任务的执行cron 不能为空")
    private String cronExpression;
    /**
     * 定时任务 的元数据
     * {@link org.quartz.JobDataMap}
     */
    private Map<?, ?> jobDataMap;
    /**
     * 定时任务 的 具体执行逻辑类
     * {@link org.quartz.Job}
     */
    @NotNull(message = "定时任务的具体执行逻辑类 坚决不能为空")
    private Class<? extends Job> jobClass;

    private int misfirePolicy;
}
