package com.CezaryZal.api.meal;

import com.CezaryZal.api.meal.manager.MealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MealController.class)
public class MealControllerTest {

    private MockMvc mockMvc;
    private SimpleDateFormat dateFormat;

    @Resource
    private WebApplicationContext webApplicationContext;

    @MockBean
    private MealService mealService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
    }

    @Test
    public void ShouldReturnOkStatusFromGetDailyDietByDayIdMethod() throws Exception {

        mockMvc.perform(get("/api/meal/dto/day-id/{dayId}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void ShouldReturnCreateStatusFromAddMealMethod() throws Exception {

        String mealDtoInJson = "{\"dateTimeOfEat\": \"" + dateFormat.format(new Date()) + "\"," +
                " \"dayId\": 9," +
                " \"description\": \"jaja1\"," +
                " \"kcal\": 80," +
                " \"type\": \"sniadanie1\"}";

        mockMvc.perform(post("/api/meal/current/{userId}", 1L)
                .content(mealDtoInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void ShouldReturnCreateStatusFromUpdateMealMethod() throws Exception {

        String mealDtoInJson = "{\"dateTimeOfEat\": \"" + dateFormat.format(new Date()) + "\"," +
                " \"dayId\": 9," +
                " \"description\": \"jaja2\"," +
                " \"kcal\": 200," +
                " \"type\": \"sniadanie2\"}";

        mockMvc.perform(put("/api/meal/{id}", 1L)
                .content(mealDtoInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void ShouldReturnOkStatusFromDeleteMealMethod() throws Exception {

        mockMvc.perform(delete("/api/meal/{id}/{userId}", 1L, 1L))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    public void checkIncorrectDateTimeOfEatInPostMethod() throws Exception {

        String mealDtoInJson = "{\"dateTimeOfEat\": \"2020-04-08_10:10\",\n" +
                " \"dayId\": 9,\n" +
                " \"description\": \"kanapki\",\n" +
                " \"kcal\": 600,\n" +
                " \"type\": \"obiad\"}";

        mockMvc.perform(post("/api/meal/current/{userId}", 1L)
                .content(mealDtoInJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.path", is("uri=/api/meal/current/1")))
                .andExpect(jsonPath("$.httpMethod", is("POST")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message",
                        is("Validation failed for argument [0] in public org.springframework.http." +
                                "ResponseEntity<java.lang.String> com.CezaryZal.api.meal.MealController." +
                                "addMeal(com.CezaryZal.api.meal.model.MealDto,java.lang.Long): [Field error " +
                                "in object 'mealDto' on field 'dateTimeOfEat': rejected value " +
                                "[2020-04-08T10:10]; codes [ActualDate.mealDto.dateTimeOfEat,ActualDate." +
                                "dateTimeOfEat,ActualDate.java.time.LocalDateTime,ActualDate]; arguments " +
                                "[org.springframework.context.support.DefaultMessageSourceResolvable: codes " +
                                "[mealDto.dateTimeOfEat,dateTimeOfEat]; arguments []; default message " +
                                "[dateTimeOfEat]]; default message [The date time should be current]] ")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("The date time should be current")));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}