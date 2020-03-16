package cn.com.felix.core.extend.exception.handler;

import cn.com.felix.core.extend.json.Result;
import cn.com.felix.core.extend.json.ResultUtils;
import cn.com.felix.core.extend.exception.SystemException;
import cn.com.felix.core.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Spring Boot提供的ErrorController是一种全局性的容错机制。
 * 此外，还可以用@ControllerAdvice注解和@ExceptionHandler注解实现对指定异常的特殊处理。
 * <p>
 * 介绍两种情况：
 * <p>
 * (1)局部异常处理 @Controller + @ExceptionHandler
 * (2)全局异常处理 @ControllerAdvice + @ExceptionHandler
 * <p>
 * 一、局部异常处理 @Controller + @ExceptionHandler
 * 局部异常主要用到的是@ExceptionHandler注解，此注解注解到类的方法上，当此注解里定义的异常抛出时，此方法会被执行。如果@ExceptionHandler所在的类是@Controller，则此方法只作用在此类。如果@ExceptionHandler所在的类带有@ControllerAdvice注解，则此方法会作用在全局。
 * <p>
 * 该注解用于标注处理方法处理那些特定的异常。被该注解标注的方法可以有以下任意顺序的参数类型：
 * <p>
 * Throwable、Exception 等异常对象；
 * <p>
 * ServletRequest、HttpServletRequest、ServletResponse、HttpServletResponse；
 * HttpSession 等会话对象；
 * org.springframework.web.context.request.WebRequest；
 * java.util.Locale；
 * java.io.InputStream、java.io.Reader；
 * java.io.OutputStream、java.io.Writer；
 * org.springframework.ui.Model；
 * <p>
 * 并且被该注解标注的方法可以有以下的返回值类型可选：
 * <p>
 * ModelAndView；
 * org.springframework.ui.Model；
 * java.util.Map；
 * org.springframework.web.servlet.View；
 *
 * @ResponseBody 注解标注的任意对象；
 * HttpEntity<?> or ResponseEntity<?>；
 * void；
 * <p>
 * 以上罗列的不完全，更加详细的信息可参考
 * @link https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/ExceptionHandler.html
 * <p>
 * 二、全局异常处理 @ControllerAdvice + @ExceptionHandler
 * 在spring 3.2中，新增了@ControllerAdvice 注解，可以用于定义@ExceptionHandler、@InitBinder、@ModelAttribute，并应用到所有@RequestMapping中。
 * 简单的说，进入Controller层的错误才会由@ControllerAdvice处理，
 * 拦截器抛出的错误以及访问错误地址的情况@ControllerAdvice处理不了，由SpringBoot默认的异常处理机制处理。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static final String DEFAULT_ERROR_VIEW = "/error"; // 定义错误显示页，error.html

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder
     */
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("[System] |- Request contain paramter!");
//        }
//
//    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     *
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "felix");
    }

    @ExceptionHandler(Exception.class) // 所有的异常都是Exception子类
    public Object defaultErrorHandler(HttpServletRequest request, Exception e) { // 出现异常之后会跳转到此方法

        if (RequestUtils.isAjaxRequest(request)) {
            if (e instanceof SystemException) {
                return ResultUtils.error((SystemException) e);
            } else {
                return ResultUtils.error(e);
            }
        } else {
            Result result = ResultUtils.error(e);
            result.setPath(request.getRequestURL());
            if (e instanceof SystemException) {
                result.setStatus(((SystemException) e).getCode()); // 将异常对象传递过去
            }

            if (logger.isDebugEnabled()) {
                logger.debug("[System] |- Error Message is：[{}]", result.toString());
                logger.debug("[System] |- Full Info", e);
            }

            return errorView(result);
        }
    }

    public static ModelAndView errorView(Result result) {
        ModelAndView modelAndView = new ModelAndView(DEFAULT_ERROR_VIEW); // 设置跳转路径
        modelAndView.addAllObjects(result); // 将异常对象传递过去
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);
        return modelAndView;
}
}
