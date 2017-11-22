package test.projec1;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import test.project1.Server;

import org.junit.*;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;

@RunWith(VertxUnitRunner.class)
public class MyServerTest {
	
	private Vertx vertx;
	private Integer port;
	
	@Before
	public void setUp(TestContext context) throws IOException {
		
	  vertx = Vertx.vertx();
	  // get a free port and deploys the verticle with the right configuration
	  ServerSocket socket = new ServerSocket(0);
	  port = socket.getLocalPort();
	  socket.close();
	  DeploymentOptions options = new DeploymentOptions()
	      .setConfig(new JsonObject().put("http.port", port)
	      );
	  vertx.deployVerticle(Server.class.getName(), options, context.asyncAssertSuccess());
	}
	
	@After
	public void tearDown(TestContext context) {
	 //un-deploys the verticles
	  vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void checkNewWord (TestContext contex){
		Async async = context.async();
		vertx.createHttpClient().post(port, "localhost", "/analyze")
	      .putHeader("content-type", "application/json")
	      .handler(response -> {
	    	  // check the result: first check the status and that we received the response as JSON
	        context.assertEquals(response.statusCode(), 201);
	        context.assertTrue(response.headers().get("content-type").contains("application/json"));
	        // second, check the response body
	        response.bodyHandler(body -> {
	          final String responseValues = Json.decodeValue(body.toString(),String);
	          context.assertTrue(responseValues.contains("value"));
	          context.assertTrue(responseValues.contains("lexical"));
	          
	          async.complete();
	        });
	      })
	      .write(responseValues)
	      .end();
	}

}
