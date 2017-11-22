package test.project1;

import io.vertx.core.*;
import io.vertx.core.http.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.*;
import io.vertx.ext.web.handler.BodyHandler;

public class Server extends AbstractVerticle {

	private WordsDB myDB; 
	//private String currWord;

	@Override
	public void start(Future<Void> fut) throws Exception {

		// create the database of words
		myDB = new WordsDB();
		// create the router and allow to get the body of the HTTP request
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		HttpServer httpServer = vertx.createHttpServer();

		// handle only http POST requests from "analyze" URL with content-type header set to `json'
		//router.route(HttpMethod.POST ,"/analyze/:text").consumes("json/*").handler(this::handlWord);
		router.post("/analyze/").consumes("json/*").handler(this::handlWord);

		//handle other requests
		router.route().handler(routingContext -> {
			int statusCode = routingContext.statusCode();
			routingContext.fail(statusCode);
		});

		httpServer.requestHandler(router::accept)
		.listen(
				config().getInteger("http.port", 8080),	result -> {
					if (result.succeeded()) {
						fut.complete();
					} else {
						fut.fail(result.cause());
					}
				});

	}
	/**
	 * This handler handles HTTP request from "/analyze" URL that are POST requests and contains "text" string property 
	 * @param routingContext
	 */
	private void handlWord(RoutingContext routingContext){

		HttpServerResponse response = routingContext.response();

		// get the body of the request
		JsonObject reqBody = routingContext.getBodyAsJson();
		//check if it has a "text" string property
		if(reqBody.containsKey("text")){
			String currWord = reqBody.getString("text");		
			//currWord = routingContext.request().getParam("text");

			// check the content of "text"- if null, fail with "bad request" status code
			if (currWord == null)
				routingContext.fail(400);

			else{
				//find the needed words and prepare the response
				JsonObject words = new JsonObject();
				words.put("value", myDB.findValue(currWord));
				words.put("lexical", myDB.findLexical(currWord));
				// save the word in the database
				myDB.add(currWord);

				response.setStatusCode(201)
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(words)); 
			}
		}
		else{
			//no "text" field in the body - fail with "bad request" status code
			routingContext.fail(400);
		}
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();

		vertx.deployVerticle(new Server());
	}

}
