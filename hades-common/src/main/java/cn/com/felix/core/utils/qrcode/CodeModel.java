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
 * File Name: CodeModel.java
 * Author: gengwei.zheng
 * Date: 2020/6/19 下午6:39
 * LastModified: 2020/3/16 下午5:24
 */

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
