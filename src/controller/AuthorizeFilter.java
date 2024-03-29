package controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import util.JWTUtils;
import util.ResponseUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 对于所有需要登录的Api，进行过滤
@WebFilter(filterName = "AuthorizeFilter", urlPatterns = "/auth/*")
public class AuthorizeFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            //解析Token
            String token = ((HttpServletRequest) servletRequest).getHeader("token");
            DecodedJWT decodedJWT = JWTUtils.decodeRsa(token);
            System.out.println("认证过滤器：用户已登录，放行，用户ID为" + decodedJWT.getClaim("userID").asString());
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            System.out.println("解析失败");
            System.out.println(e.getMessage());
            System.out.println("认证过滤器：用户未登录，拒绝访问");
            ResponseUtils.responseJson(405, "用户未登录或没有权限访问该功能", (HttpServletResponse) servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
