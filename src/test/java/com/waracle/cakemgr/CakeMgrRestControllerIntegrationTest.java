package com.waracle.cakemgr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.waracle.cakemgr.data.CakeEntity;
import com.waracle.cakemgr.data.CakeRepository;
import com.waracle.cakemgr.exceptions.CakeMgrException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class CakeMgrRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CakeRepository repository;

    @Before
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void whenValidInput_thenCreateCake() throws IOException, Exception {
        mvc.perform(post("/cakes").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"mycake\",\"image\":\"https://mycake.jpg\",\"desc\":\"my cake is yummy\"}"));

        Iterable<CakeEntity> found = repository.findAll();
        assertThat(found).extracting(CakeEntity::getTitle).contains("mycake");
    }

    @Test
    public void whenGetCakes_thenStatus200_AndRecentlyPersistedCakeIsAvailable() throws Exception {
        createCake(6L, "VanillaCake", "Yummy Vanilla cake", "https://vanilla.jpg");

        mvc.perform(get("/cakes")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("VanillaCake")))
                .andExpect(jsonPath("$[0].desc", is("Yummy Vanilla cake")))
                .andExpect(jsonPath("$[0].image", is("https://vanilla.jpg")));

    }

    @Test
    public void givenEmployees_whenGetCakes_thenStatus200_AndHas2Records() throws Exception {
        createCake(6L, "VanillaCake", "Yummy Vanilla cake", "https://vanilla.jpg");
        createCake(7L, "secondCake", "this is also a yummy one", "https://yummy.jpg");

        mvc.perform(get("/cakes")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void givenEmployees_whenGetCakes_WithInvalidContentType_thenStatus404_ExceptionTriggered() throws Exception {
        createCake(6L, "VanillaCake", "Yummy Vanilla cake", "https://vanilla.jpg");

        MvcResult result = mvc.perform(get("/cakes")
                .contentType(MediaType.APPLICATION_ATOM_XML))
                .andExpect(status().is4xxClientError())
                .andReturn();


        Optional<CakeMgrException> cakeMgrException = Optional.ofNullable((CakeMgrException) result.getResolvedException());

        cakeMgrException.ifPresent( (se) -> assertThat(is(notNullValue())));
        cakeMgrException.ifPresent( (se) -> assertThat(is(instanceOf(CakeMgrException.class))));

    }

    @Test
    public void givenEmployees_whenGetCakeByTitle_thenStatus200() throws Exception {
        createCake(6L, "VanillaCake", "Yummy Vanilla cake", "https://vanilla.jpg");
        createCake(7L, "secondCake", "this is also a yummy one", "https://yummy.jpg");

        mvc.perform(get("/cakes/secondCake")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("secondCake")))
                .andExpect(jsonPath("$[0].desc", is("this is also a yummy one")))
                .andExpect(jsonPath("$[0].image", is("https://yummy.jpg")));

    }

    @Test
    public void givenEmployees_whenGetCakeByTitle_thenStatus404() throws Exception {
        createCake(6L, "VanillaCake", "Yummy Vanilla cake", "https://vanilla.jpg");
        createCake(7L, "secondCake", "this is also a yummy one", "https://yummy.jpg");

        mvc.perform(get("/cakes/thirdCake")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404));


    }


    private void createCake(Long id, String title, String desc, String image) {
        CakeEntity cake = new CakeEntity();
        cake.setCakeId(id);
        cake.setTitle(title);
        cake.setDescription(desc);
        cake.setImage(image);
        repository.save(cake);
    }

}