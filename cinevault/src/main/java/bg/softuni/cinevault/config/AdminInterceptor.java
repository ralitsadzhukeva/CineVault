package bg.softuni.cinevault.config;

import bg.softuni.cinevault.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null||session.getAttribute("user_role") != Role.ADMIN) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
