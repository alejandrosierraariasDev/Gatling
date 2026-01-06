package simulations;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import config.Config;
import requests.Requests;


import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class UserSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl(Config.BASE_URL)
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    int users = Integer.parseInt(System.getProperty("users", String.valueOf(Config.USERS)));
    int rampDuration = Integer.parseInt(System.getProperty("rampDuration", String.valueOf(Config.RAMP_DURATION)));

    ScenarioBuilder userScenario = scenario("Video Game API Test")
            .exec(Requests.authenticate)
            .pause(1)
            .exec(Requests.createNewGame)
            .pause(1)
            .exec(Requests.getAllGames)
            .pause(2);

    {
        setUp(
                userScenario.injectOpen(
                        nothingFor(2),
                        rampUsers(users).during(rampDuration)
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().responseTime().percentile3().lt(600),
                        global().successfulRequests().percent().gt(99.0)
                );
    }
}
