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
