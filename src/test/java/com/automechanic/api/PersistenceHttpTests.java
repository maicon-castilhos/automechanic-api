package com.automechanic.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.automechanic.api.repositories.PecaRepository;
import com.automechanic.api.resources.PecaResource;
import com.automechanic.api.resources.VeiculoResource;
import com.automechanic.api.resources.exceptions.ResourceExceptionHandler;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class PersistenceHttpTests {
    @Autowired private PecaResource pecas;
    @Autowired private VeiculoResource veiculos;
    @Autowired private PecaRepository repository;
    @Autowired private ResourceExceptionHandler handler;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(pecas, veiculos).setControllerAdvice(handler).build();
    }

    @Test
    void duplicateCodeReturns409AfterCommit() throws Exception {
        String payload = "{\"codigo\":\"HTTP-CONFLICT\",\"nome\":\"Filtro\",\"precoVenda\":10}";
        try {
            mvc.perform(post("/pecas").contentType(MediaType.APPLICATION_JSON).content(payload))
                    .andExpect(status().isCreated());
            var result = mvc.perform(post("/pecas").contentType(MediaType.APPLICATION_JSON).content(payload))
                    .andExpect(status().isConflict()).andReturn();
            assertFalse(result.getResponse().getContentAsString().contains("insert into"));
        } finally {
            repository.findByCodigo("HTTP-CONFLICT").ifPresent(repository::delete);
        }
    }

    @Test
    void deletingReferencedVehicleReturns409AndPreservesVehicle() throws Exception {
        mvc.perform(delete("/veiculos/1")).andExpect(status().isConflict());
        mvc.perform(get("/veiculos/1")).andExpect(status().isOk());
    }
}
