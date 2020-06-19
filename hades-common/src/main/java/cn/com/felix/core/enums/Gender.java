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
 * Module Name: hades-common
 * File Name: Gender.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/4/22 上午10:30
 */

package cn.com.felix.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hades
 */
public enum Gender {
    OTHERS(0, "其它"),
    MAN(1, "男"),
    WOMAN(2, "女");

    private int index;
    private String name;

    private static Map<Integer, Gender> map = new HashMap<>();

    static {
        for (Gender gender : Gender.values()) {
            map.put(gender.index, gender);
        }
    }

    Gender(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public static Gender getGender(int index) {
        return map.get(index);
    }
}
