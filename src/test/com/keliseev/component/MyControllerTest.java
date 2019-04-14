package com.keliseev.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keliseev.config.WebAppConfig;
import com.keliseev.to.AccountTO;
import com.keliseev.to.SendRequestTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebAppConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class MyControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private ObjectMapper mapper;


    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void whenServletContext_thenProvidesController() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("myController"));
    }

    @Test
    public void whenCreateAccount_thenReturnAccount() throws Exception {
        //given
        AccountTO to = new AccountTO("TestGuy", 1200);

        //when
        ResultActions result = mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(to)));

        //then
        result.andDo(print())
                .andExpect(content()
                        .json("{\"id\":8,\"name\":\"TestGuy\",\"balance\":1200,\"active\":true}"))
                .andExpect(status()
                        .isOk());
    }

    @Test
    public void whenGetAllAccounts_thenReturnListOfThree() throws Exception {
        //given
        AccountTO to1 = new AccountTO("TestGuy1", 120);
        AccountTO to2 = new AccountTO("TestGuy2", 200);
        AccountTO to3 = new AccountTO("TestGuy3", 100);

        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(to1)));
        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(to2)));
        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(to3)));

        //when
        ResultActions result = mockMvc.perform(get("/accounts")
                .contentType(APPLICATION_JSON));

        //then
        result.andDo(print())
                .andExpect(content()
                        .json("[ 1, 2, 3, 4, 5]"))
                .andExpect(status()
                        .isOk());
    }

    @Test
    public void whenGetAccountInfo_thenReturnAccountInfo() throws Exception {
        //given
        AccountTO to = new AccountTO("SomePerson", 120000);
        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(to)));

        //when
        ResultActions result = mockMvc.perform(get("/account/1")
                .contentType(APPLICATION_JSON));

        //then
        result.andDo(print())
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"SomePerson\",\"balance\":50,\"active\":false}"))
                .andExpect(status()
                        .isOk());
    }

    @Test
    public void whenCloseAccount_thenCloseAccount() throws Exception {
        //given
        AccountTO to = new AccountTO("SomeOtherPerson", 10000);
        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(to)));

        //when
        ResultActions result = mockMvc.perform(delete("/account/1")
                .contentType(APPLICATION_JSON));

        //then
        result.andDo(print())
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"SomePerson\",\"balance\":50,\"active\":false}"))
                .andExpect(status()
                        .isOk());
    }

    @Test
    public void whenTransferEnough_thenTransfer() throws Exception {
        //given
        AccountTO richGuy = new AccountTO("SomePerson", 100);
        AccountTO poorGuy = new AccountTO("SomeOtherPerson", 0);
        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(richGuy)));
        mockMvc.perform(post("/account")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(poorGuy)));

        //when
        SendRequestTO requestTO = new SendRequestTO(1, 2, 1);

        for (int i = 0; i < 49; i++) {
            new Thread(() -> {
                try {
                    mockMvc.perform(post("/transfer")
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestTO)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        //otherwise Threads from above could not finish transactions
        Thread.sleep(500);

        ResultActions result = mockMvc.perform(post("/transfer")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestTO)));

        //then
        result.andDo(print())
                .andExpect(content()
                        .json("[{\"id\":1,\"name\":\"SomePerson\",\"balance\":50,\"active\":true}," +
                                "{\"id\":2,\"name\":\"SomeOtherPerson\",\"balance\":50,\"active\":true}]"))
                .andExpect(status()
                        .isOk());
    }
}