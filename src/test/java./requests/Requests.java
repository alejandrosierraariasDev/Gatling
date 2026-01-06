package requests;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class Requests {


    public static ChainBuilder authenticate =
            exec(http("Auth - Login")
                    .post("/authenticate")
                    .body(StringBody("{\"password\": \"admin\", \"username\": \"admin\"}"))
                    .asJson()
                    .check(jsonPath("$.token").saveAs("jwtToken")));


    public static ChainBuilder getAllGames =
            exec(http("Get All Games")
                    .get("/videogame")
                    .header("Authorization", "Bearer #{jwtToken}")
                    .check(status().is(200)));


    public static ChainBuilder getSingleGame =
            exec(http("Get Game ID: #{createdGameId}")
                    .get("/videogame/#{createdGameId}")
                    .header("Authorization", "Bearer #{jwtToken}")
                    .check(status().is(200))
                    .check(jsonPath("$.name").exists()));


    public static ChainBuilder createNewGame =
            exec(session -> session.set("gameName", "Game-" + java.util.UUID.randomUUID()))
                    .exec(http("Create New Game: #{gameName}")
                            .post("/videogame")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .body(ElFileBody("bodies/newGame.json")).asJson()
                            .check(status().is(200))
                            .check(jsonPath("$.id").saveAs("createdGameId")));
}
