package bg.softuni.cinevault.controller;

import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.service.recommendation.RecommendationService;
import bg.softuni.cinevault.web.RecommendationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecommendationController.class)
public class RecommendationControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommendationService recommendationService;

    @Test
    void getRecommendations_shouldReturnOk() throws Exception {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        when(recommendationService.getRecommendations(userId))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/recommendations")
                                .with(authentication(auth))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("recommendations"))
                .andExpect(model().attributeExists("recommendations"));
    }

    @Test
    void getRecommendations_shouldRedirectWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/recommendations"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRecommendations_shouldReturnOkForAdmin() throws Exception {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setUsername("admin");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        when(recommendationService.getRecommendations(userId))
                .thenReturn(List.of());

        mockMvc.perform(get("/recommendations").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("recommendations"));
    }
}