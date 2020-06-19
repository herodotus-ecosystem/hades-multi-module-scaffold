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
 * File Name: ColorORCode.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

package cn.com.felix.core.utils.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 清云
 * @describe 生成彩色的二维码的类
 * @time 2018年8月26日下午4:02:00
 */
public class ColorORCode {

    public static void main(String[] args) throws UnsupportedEncodingException {
        // 依次为内容(不支持中文),宽,长,logo图标路径,储存路径
        ColorORCode.encode("https://www.baidu.com/", 300, 300, "D:/testImage/qrcode.jpg");
    }

    // 二维码写码器
    private static MultiFormatWriter mutiWriter = new MultiFormatWriter();

    public static void encode(String content, int width, int height, String destImagePath) {
        try {
            //生成图片文件
            ImageIO.write(genBarcode(content, width, height), "png", new File(destImagePath));
            System.out.println("二维码生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到BufferedImage
     *
     * @param content 二维码显示的文本
     * @param width   二维码的宽度
     * @param height  二维码的高度
     * @return
     * @throws WriterException
     * @throws IOException
     */
    private static BufferedImage genBarcode(String content, int width, int height) throws WriterException, IOException {
        //定义二维码内容参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //设置字符集编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置容错等级，在这里我们使用M级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 生成二维码，参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
        BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        // 二维矩阵转为一维像素数组
        int halfW = matrix.getWidth() / 2;
        int halfH = matrix.getHeight() / 2;
        int[] pixels = new int[width * height];

        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                // 二维码颜色（RGB）
                int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight()
                        * (y + 1));
                int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight()
                        * (y + 1));
                int num3 = (int) (162 - (162.0 - 107.0)
                        / matrix.getHeight() * (y + 1));
                Color color = new Color(num1, num2, num3);
                int colorInt = color.getRGB();
                // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
                pixels[y * width + x] = matrix.get(x, y) ? colorInt : 16777215;// 0x000000:0xffffff
            }
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, width, height, pixels);

        return image;
    }

}