/*
 * Copyright (c) 2019. All Rights Reserved
 * ProjectName: hades-multi-module
 * FileName: Hierarchy
 * Author: gengwei.zheng
 * Date: 19-2-15 下午2:09
 * LastModified: 19-2-15 下午2:09
 */

package cn.com.felix.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author gengwei.zheng
 * @date 2019/2/15
 */
public enum Hierarchy {

    SENIOR(0, "高层"),
    MIDDLE(1, "中层"),
    GRASS_ROOTS(2, "基层");

    private int index;
    private String name;

    private static Map<Integer, Hierarchy> map = new HashMap<>();

    static {
        for (Hierarchy hierarchy : Hierarchy.values()) {
            map.put(hierarchy.index, hierarchy);
        }
    }

    Hierarchy(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public static Hierarchy getHierarchy(int index) {
        return map.get(index);
    }
}
