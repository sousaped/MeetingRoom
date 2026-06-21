package br.com.meetingroom.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void deveBuscarUsuario() throws Exception {
        //ARRANGE
        String json = """
                {
                    "nome": "Alex Sousa",
                    "email": "alex@email.com",
                    "telefone": "11999999999"
                }
                """;

        //ACT


        //ASSERT


    }

}