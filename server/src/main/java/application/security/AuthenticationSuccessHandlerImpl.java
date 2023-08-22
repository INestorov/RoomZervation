package application.security;

import application.entities.ServerResponse;
import application.entities.User;
import application.entities.UserType;
import application.repositories.BikeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Autowired
    private BikeRepository bikeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Logger logger;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        User user = (User) authentication.getDetails();
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(ServerResponse.data(
            objectMapper.createObjectNode().put("isAdmin", user.getType() == UserType.Admin)
                .put("hasBike", bikeRepository.findBikeByUser_Id(user.getId()).isPresent())));
        response.getWriter().flush();
        logger.info("{} logged in", user.getUsername());
    }
}
