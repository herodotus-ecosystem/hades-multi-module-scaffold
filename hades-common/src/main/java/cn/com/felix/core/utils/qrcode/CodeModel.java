package cn.com.felix.core.utils.qrcode;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
@Data
public class CodeModel {
    private String contents;
    private int width = 400;
    private int height = 400;
    private String format = "jpg";
    private String character_set = "utf-8";
    private int fontSize = 12;
    private File logoFile;
    private float logoRatio = 0.20f;
    private String desc;
    private int whiteWidth;//白边的宽度
    private int[] bottomStart;//二维码最下边的开始坐标
    private int[] bottomEnd;//二维码最下边的结束坐标
}
