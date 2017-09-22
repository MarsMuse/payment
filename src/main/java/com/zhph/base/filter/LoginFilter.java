package com.zhph.base.filter;

import com.zhph.base.utils.DataUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        String stringUrl = req.getRequestURI();
        //查看是否是SpringMVC的资源
        if(stringUrl.indexOf("/action")!= -1) {
            String[] urlArray = stringUrl.split("/");
            if (urlArray != null && urlArray.length > 0) {
                stringUrl = urlArray[urlArray.length - 1];
            }
            if (session.getAttribute("user") != null || stringUrl.contains(DataUtil.exceptUrlOne) || stringUrl.contains(DataUtil.exceptUrlTwo) || stringUrl.contains(DataUtil.exceptUrlThree)) {
                chain.doFilter(request, response);
            } else {
                HttpServletResponse res = (HttpServletResponse) response;
                res.sendRedirect(req.getContextPath() + DataUtil.DEFAULT_LOGIN_URL);
            }
        }else{
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
