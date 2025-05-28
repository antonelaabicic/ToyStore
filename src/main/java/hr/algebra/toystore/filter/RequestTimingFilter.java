package hr.algebra.toystore.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RequestTimingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        long duration = System.currentTimeMillis() - startTime;
        HttpServletRequest req = (HttpServletRequest) request;
        log.info("REQUEST TO {} TOOK {} ms.", req.getRequestURI(), duration);
    }
}
