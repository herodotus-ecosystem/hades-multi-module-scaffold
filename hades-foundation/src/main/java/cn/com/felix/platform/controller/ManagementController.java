/*
 * Copyright 2019-2019 the original author or authors.
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
 * Project Name: hades-multi-module
 * Module Name: hades-foundation
 * File Name: ManagementController.java
 * Author: hades
 * Date: 2019/11/3 下午3:34
 * LastModified: 2019/11/3 下午3:28
 */

package cn.com.felix.platform.controller;

import cn.com.felix.common.basic.controller.AbstractController;

import cn.com.felix.core.utils.qrcode.CodeCreator;
import cn.com.felix.core.utils.qrcode.CodeModel;
import cn.com.felix.core.utils.qrcode.ColorImageORCode;
import cn.com.felix.core.utils.qrcode.ColorORCode;
import cn.com.felix.core.utils.encrypt.AesEncryptUtils;
import cn.com.felix.core.utils.encrypt.Base64Utils;
import cn.com.felix.core.utils.encrypt.SecurityTools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

@Controller
public class ManagementController extends AbstractController {

    @RequestMapping(value = "/management")
    public String management(Model model) {
        return "/management";
    }


    @RequestMapping(value = "/management/test")
    @ResponseBody
    public String changePassword(String base64AndAesString, String aesString) {

        try {
            String ori = "123!@#Qwe ";
            String aa="{\n" +
                    "\"随行人数\":20,\n\"timestamp\": 1564046335228" +

                    "}";//SecurityTools.encrypt(ori)
            //ZxingQrCodeUtil.createZxingqrCode(SecurityTools.encrypt(aa),300,300);
            ColorORCode.encode(SecurityTools.encrypt(aa),400,400,"E:\\qr-color.png");
            ColorImageORCode.encode(SecurityTools.encrypt(aa),400,400,"E:\\1.jpg",
                    "E:\\133333.jpg");


            CodeCreator creator = new CodeCreator();
            CodeModel info = new CodeModel();
            info.setWidth(400);
            info.setHeight(400);
            info.setFontSize(24);
         //   info.setContents("<a href='http://www.sohu.com'>人生就是拼搏</a>");
            //info.setContents("http://www.sohu.com");
            info.setContents(SecurityTools.encrypt(aa));//"万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新\n万水千山只等闲\n小荷才楼尖尖角\n谁家新燕啄春泥\n无边光景一时新");
            info.setLogoFile(new File("E:\\1.jpg"));
            info.setDesc("码通");
            //info.setLogoDesc("一叶浮萍归大海，adsasfbhtjg人生何处不相逢");
            //info.setLogoDesc("一叶浮萍");
            creator.createCodeImage(info, "E:\\dest." + info.getFormat());


            AesEncryptUtils aes = new AesEncryptUtils();

            String stringBase64AndAes = aes.decrypt(base64AndAesString);
            System.out.println("AES解密:" + stringBase64AndAes);
            System.out.println("base64解密:" + Base64Utils.decodeData(stringBase64AndAes));
            String stringAes = aes.decrypt(aesString);
            System.out.println("AES解密:" + stringAes);
//

            base64AndAesString = aes.encrypt(Base64Utils.encodeData(ori));
          //  base64AndAesString = aes.encrypt(ori);
            aesString = aes.encrypt("15198971986");



            String tmp = "";
            // 解密方法
            tmp = aes.decrypt("132B3833E388F30B0A8D075FBA63E044E1461DACE489723F597048BB0CFA4593");
            System.out.println("解密后的内容：" + tmp);

            // 解密方法
            tmp = aes.decrypt("3E1703153E0DBB7FA6CA2C60DA4C54CB");
            System.out.println("解密后的内容：" + tmp);

            tmp = aes.encrypt(ori);
            System.out.println("加密后的内容：" + tmp);

            tmp = aes.decrypt(tmp);
            System.out.println("解密后的内容：" + tmp);

        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        String newPassword = Base64Utils.decodeData(newsecuwd);

        return ajaxSuccess("{\"a\":\"" + base64AndAesString + "\",\"b\":\"" + aesString + "\"}");
    }
}
