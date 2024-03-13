package com.example.restapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RestapiApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	void init() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	@WithMockUser(roles = "USERS_VIEWER")
	public void testRoleUsersViewer() throws Exception {

		mockMvc.perform(get("/users/1"))
				.andExpect(status().isOk());
		mockMvc.perform(delete("/users/1"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/posts"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/albums"))
				.andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(roles = "USERS_EDITOR")
	public void testRoleUsersEditor() throws Exception {

		mockMvc.perform(get("/users/1"))
				.andExpect(status().isOk());
		mockMvc.perform(delete("/users/1"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/posts"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/albums"))
				.andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(roles = "POSTS_VIEWER")
	public void testRolePostsViewer() throws Exception {

		mockMvc.perform(get("/posts/1"))
				.andExpect(status().isOk());
		mockMvc.perform(delete("/posts/1"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/users"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/albums"))
				.andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(roles = "POSTS_EDITOR")
	public void testRolePostsEditor() throws Exception {

		mockMvc.perform(get("/posts/1"))
				.andExpect(status().isOk());
		mockMvc.perform(delete("/posts/1"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/users"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/albums"))
				.andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(roles = "ALBUMS_VIEWER")
	public void testRoleAlbumsViewer() throws Exception {

		mockMvc.perform(get("/albums/1"))
				.andExpect(status().isOk());
		mockMvc.perform(delete("/albums/1"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/posts"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/users"))
				.andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(roles = "ALBUMS_EDITOR")
	public void testRoleAlbumsEditor() throws Exception {

		mockMvc.perform(get("/albums/1"))
				.andExpect(status().isOk());
		mockMvc.perform(delete("/albums/1"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/posts"))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/users"))
				.andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testRoleAdmin() throws Exception {

		mockMvc.perform(get("/users/1"))
				.andExpect(status().isOk());
		mockMvc.perform(delete("/users/1"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/posts"))
				.andExpect(status().isOk());
		mockMvc.perform(delete("/posts/1"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/albums"))
				.andExpect(status().isOk());
		mockMvc.perform(delete("/albums/1"))
				.andExpect(status().isOk());

	}

	@Test
	@WithAnonymousUser
	public void testAnonymous() throws Exception {

		mockMvc.perform(get("/users/1"))
				.andExpect(status().isUnauthorized());
		mockMvc.perform(delete("/users/1"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(get("/posts"))
				.andExpect(status().isUnauthorized());
		mockMvc.perform(delete("/posts/1"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(get("/albums"))
				.andExpect(status().isUnauthorized());
		mockMvc.perform(delete("/albums/1"))
				.andExpect(status().isUnauthorized());

	}

}
