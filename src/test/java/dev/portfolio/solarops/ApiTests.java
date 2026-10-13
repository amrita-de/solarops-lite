package dev.portfolio.solarops;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
@SpringBootTest @AutoConfigureMockMvc class ApiTests {
 @Autowired MockMvc mvc;
 @Test void anonymousDenied()throws Exception{mvc.perform(get("/api/inventory")).andExpect(status().isUnauthorized());}
 @Test void viewerReads()throws Exception{mvc.perform(get("/api/inventory").with(user("viewer").roles("VIEWER"))).andExpect(status().isOk());}
 @Test void viewerCannotReserve()throws Exception{mvc.perform(post("/api/reservations").with(csrf()).with(user("viewer").roles("VIEWER")).contentType("application/json").content("{\"itemId\":1,\"quantity\":1,\"requestKey\":\"test\"}")).andExpect(status().isForbidden());}
 @Test void csrfRequired()throws Exception{mvc.perform(post("/api/reservations").with(user("manager").roles("MANAGER")).contentType("application/json").content("{}")).andExpect(status().isForbidden());}
 @Test void validation()throws Exception{mvc.perform(post("/api/reservations").with(csrf()).with(user("manager").roles("MANAGER")).contentType("application/json").content("{\"itemId\":1,\"quantity\":0,\"requestKey\":\"\"}")).andExpect(status().isBadRequest());}
}
