package cn.com.felix.core.utils;

import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RequestUtils {

    private static boolean isHeaderContainMediaType(ServletRequest request, String headerType, String mediaType) {
        String header = WebUtils.toHttp(request).getHeader(headerType);
        return StringUtils.isNotEmpty(header) && header.contains(mediaType);
    }

    private static boolean isHeaderContainJson(ServletRequest request, String headerType) {
        return isHeaderContainMediaType(request, headerType, MediaType.APPLICATION_JSON_VALUE);
    }

    private static boolean isContentTypeHeaderContainJson(ServletRequest request) {
        return isHeaderContainJson(request, HttpHeaders.CONTENT_TYPE);
    }

    private static boolean isAcceptHeaderContainJson(ServletRequest request) {
        return isHeaderContainJson(request, HttpHeaders.ACCEPT);
    }

    private static boolean isContainAjaxFlag(ServletRequest request) {
        String xRequestedWith = WebUtils.toHttp(request).getHeader(HttpHeaders.X_REQUESTED_WITH);
        return HttpConstants.XML_HTTP_REQUEST.equalsIgnoreCase(xRequestedWith);
    }

    public static boolean isAjaxRequest(ServletRequest request) {

        //使用HttpServletRequest中的header检测请求是否为ajax, 如果是ajax则返回json, 如果为非ajax则返回view(即ModelAndView)
        if (isContentTypeHeaderContainJson(request) || isAcceptHeaderContainJson(request) || isContainAjaxFlag(request)) {
            log.trace("[Request Utils] |- Is Ajax Request!!!!!");
            return true;
        }

        log.trace("[Request Utils] |- Not a Ajax Request!!!!!");
        return false;
    }

    /**
     * 获取登录用户的IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = WebUtils.toHttp(request).getHeader(HttpConstants.HEADER_X_FORWARDED_FOR);
        if (StringUtils.isEmpty(ip) || HttpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = WebUtils.toHttp(request).getHeader(HttpConstants.HEADER_PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ip) || HttpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = WebUtils.toHttp(request).getHeader(HttpConstants.HEADER_WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ip) || HttpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = WebUtils.toHttp(request).getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }

        log.debug("[Request Utils] |- Fetch IP Address : [{}]", ip);

        return ip;
    }

    public static boolean shouldUseNormalHttpRequestToProcess(ServletRequest request, boolean serviceOriented) {
        return !isAjaxRequest(request) && !serviceOriented;
    }

    public static boolean isStaticResourcesRequest(ServletRequest request) {
        String requestPath = WebUtils.toHttp(request).getServletPath();
        if (StringUtils.endsWith(requestPath, "html")) {
            return false;
        } else {
            return ServletUtils.isStaticFile(requestPath);
        }
    }

}
