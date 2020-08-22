package com.greenlearner.ticketbookingservice.statefulbehavior;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
@WebMvcTest(controllers = TaskTrackerTest.class)
public class TaskTrackerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void toDoListScenario() throws Exception {
        stubFor(get(urlEqualTo("/todo/items")).inScenario("To do list")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                        .withBody("Buy milk")));

        stubFor(post(urlEqualTo("/todo/items")).inScenario("To do list")
                .whenScenarioStateIs(STARTED)
                .withRequestBody(containing("Cancel newspaper subscription"))
                .willReturn(aResponse().withStatus(201))
                .willSetStateTo("Cancel newspaper item added"));

        stubFor(get(urlEqualTo("/todo/items")).inScenario("To do list")
                .whenScenarioStateIs("Cancel newspaper item added")
                .willReturn(aResponse()
                        .withBody("<items>" +
                                "   <item>Buy milk</item>" +
                                "   <item>Cancel newspaper subscription</item>" +
                                "</items>")));

        mockMvc.perform(MockMvcRequestBuilders.get("/todo/items")).andExpect(MockMvcResultMatchers.status().isOk());

//        mockMvc.perform(get(urlPathEqualTo("/todo/items")));
//        WireMockResponse response = testClient.get("/todo/items");
//        assertThat(response.content(), containsString("Buy milk"));
//        assertThat(response.content(), not(containsString("Cancel newspaper subscription")));
//
//        response = testClient.postWithBody("/todo/items", "Cancel newspaper subscription", "text/plain", "UTF-8");
//        assertThat(response.statusCode(), is(201));
//
//        response = testClient.get("/todo/items");
//        assertThat(response.content(), containsString("Buy milk"));
//        assertThat(response.content(), containsString("Cancel newspaper subscription"));
    }
}
