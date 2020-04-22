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
 * File Name: SecurityController.java
 * Author: hades
 * Date: 2019/11/3 下午3:34
 * LastModified: 2019/11/2 下午8:31
 */

package cn.com.felix.platform.controller;

import cn.com.felix.common.config.shiro.CurrentUser;
import cn.com.felix.core.utils.RequestUtils;
import cn.com.felix.core.utils.encrypt.Base64Utils;
import cn.com.felix.system.domain.SysUser;
import cn.com.felix.system.service.SysLogService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

@Controller
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    public static final String KEY_CAPTCHA = "KEY_CAPTCHA";

    @Autowired
    private SysLogService sysLogService;
    @Resource
    private DefaultKaptcha defaultKaptcha;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login404(Model model) {

        return "/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("sysUser", new SysUser());
        return "/login";
    }

    /**
     * Angular SPA 单页面应用和MVC控制器的整合，会有一个疑问，SPA中点击链接的跳转，是经过SPA的路由，还是直接被SpringMVC拦截了?
     * <p>
     * SPA中点击链接的跳转先经过路由还是拦截器，主要看链接的形式，
     * Angular SPA会把/#/形式的链接先进行路由中转处理，除了/#/形式的链接会按正常流程进入拦截器处理
     *
     * @return
     */
    @RequestMapping(value = "/flowable", method = RequestMethod.GET)
    public String index() {
        return "/flowable-modeler/index.html";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, @Valid SysUser sysUser, String vrifyCode, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (logger.isTraceEnabled()) {
            logger.trace("[System] |- Do Login Action");
        }

        if (bindingResult.hasErrors()) {
            return "/login";
        }

        if (StringUtils.isEmpty(vrifyCode)) {
            redirectAttributes.addFlashAttribute("message", "请输入验证码");
            return "redirect:/login";
        }


        Session session = CurrentUser.getSession();
        String captchaId = (String) session.getAttribute(KEY_CAPTCHA);
        String vrifyCodeStr = Base64Utils.decodeData(vrifyCode);
        if (!Objects.equals(vrifyCodeStr, captchaId)) {
            redirectAttributes.addFlashAttribute("message", "验证码输入错误");
            return "redirect:/login";
        }

        String userName = Base64Utils.decodeData(sysUser.getUserName());
        String password = Base64Utils.decodeData(sysUser.getPassword());

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        if (request.getParameter("rememberMe") != null) {
            token.setRememberMe(true);
        }
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            logger.info("[System] |- User [" + userName + "] Doing Login Action..Authentication Begin!");
            currentUser.login(token);
            logger.info("[System] |- User [" + userName + "] Doing Login Action..Authentication Pass!");
        } catch (UnknownAccountException uae) {
            logger.warn("[System] |- User [" + userName + "] Doing Login Action..Authentication Failure - Unknown Account!");
            redirectAttributes.addFlashAttribute("message", "未知账户");
        } catch (IncorrectCredentialsException ice) {
            logger.warn("[System] |- User [" + userName + "] Doing Login Action..Authentication Failure - Incorrect Credentials!");
            redirectAttributes.addFlashAttribute("message", "密码不正确");
        } catch (LockedAccountException lae) {
            logger.warn("[System] |- User [" + userName + "] Doing Login Action..Authentication Failure - Locked Account!");
            redirectAttributes.addFlashAttribute("message", "账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            logger.warn("[System] |- User [" + userName + "] Doing Login Action..Authentication Failure - Excessive Attempts!");
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            logger.error("[System] |- User [" + userName + "] Doing Login Action..Authentication Failure - Stack Trace");
            ae.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
        }
        //验证是否登录成功
        if (currentUser.isAuthenticated()) {

            session.setAttribute("current_user_name", CurrentUser.getUserName());
            session.setAttribute("current_user_uid", CurrentUser.getUserID());
            session.setAttribute("current_user", CurrentUser.getPrincipal());

            logger.info("[System] |- User [" + userName + "] Login Successfully!");
            sysLogService.record(CurrentUser.getPrincipal(), RequestUtils.getIpAddress(request), "登入系统");

            if (userName.equals("system")) {
                return "redirect:/management";
            } else {
                return "redirect:/home";
            }
        } else {
            token.clear();
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        logger.info("[System] |-  User [" + CurrentUser.getUserName() + "] Logout Successfully!");
        sysLogService.record(CurrentUser.getPrincipal(), RequestUtils.getIpAddress(request), "退出系统");

        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/login";
    }

    @RequestMapping(value = "/kaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();

            Session session = CurrentUser.getSession();
            logger.info("[System] |- KEY_CAPTCHA [" + createText + "]!!!!");
            session.setAttribute("KEY_CAPTCHA", createText);

            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
