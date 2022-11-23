package de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.xapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.InetSocketAddress;

import java.util.List;

import org.json.JSONArray;

import com.sun.net.httpserver.HttpServer;

import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.graph.Triple;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.tbox_service.TBoxService;
import de.hsuifa.xapi.xapi_plugin.xapi_plugin_base.triple_service.TripleService;



public abstract class Xapi {
    
    private TBoxService tboxService;
    private TripleService tripleService;
    private TripleConverter tripleConverter; 
    private HttpServer httpServer;
    
    /**
     * creates a new xAPI.
     * @param tripleService the service handling any triple requests
     * @param tboxService the service handling the TBox reuqest
     */
    public Xapi(TripleService tripleService) {   

        this.tripleService = tripleService;
        this.tboxService = new TBoxService(tripleService.getTypeList());
        this.tripleConverter = new TripleConverter();

    }

    /**
     * activates the api
     * @throws IOException
     */
    public void activate() throws IOException {

        // create server
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        // create POST route to request triple patterns
        httpServer.createContext("/triple", (exchange -> {

            // respond if method equals POST
            if ("POST".equals(exchange.getRequestMethod())) { 
                if (!(tripleServiceAvailable())) {
                    exchange.sendResponseHeaders(500, -1);
                } else {
                    // read reqest
                    InputStreamReader inputStreamReader =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    int b;
                    StringBuilder stringBuilder = new StringBuilder(512);
                    while ((b = bufferedReader.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                    String requestString = stringBuilder.toString();
                    // close 
                    bufferedReader.close();
                    inputStreamReader.close();
                    // send response
                    String responseString = this.getTriple(requestString);
                    exchange.sendResponseHeaders(200, responseString.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(responseString.getBytes());
                    output.flush();
                }
            // reject (code 405) if any other method than POST is chosen    
            } else {
                exchange.sendResponseHeaders(405, -1);
            }

            // close
            exchange.close();

        }));

        // create GET route to get the tbox 
        httpServer.createContext("/tbox", (exchange -> { 
            
            // respond if method equals GET
            if ("GET".equals(exchange.getRequestMethod())) {
                if (!(tboxServiceAvailable())) {
                    exchange.sendResponseHeaders(500, -1);
                } else {
                    // get tbox as string and send
                    String responseText = this.getTbox();
                    exchange.sendResponseHeaders(200, responseText.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(responseText.getBytes());
                    output.flush();
                }
            // reject (code 405) if any other method than GET is chosen    
            } else {
                exchange.sendResponseHeaders(405, -1);
            }

            exchange.close();

        }));

        // create a default executor and start server
        httpServer.setExecutor(null); 
        httpServer.start();

    };

    /**
     * deactivates the api
     */
    public void deactivate() {
        httpServer.stop(0);
    };

    /**
     * helper method to
     * <ul> 
     * <li>convert requestString to JSON
     * <li>convert JSON to triple
     * <li>send triple to tripleService
     * <li>recieve answer
     * <li>convert answer to JSON
     * <li>convert JSON to string
     * </ul>
     * -
     * @param requestString
     * @return
     */
    private String getTriple(String requestString) {

        // create json array from string
        JSONArray tripleAsJson = new JSONArray(requestString);

        // convert to triple
        Triple triple = tripleConverter.jsonToTriple(tripleAsJson);

        // get triples from triple service
        List<Triple> resultTripleList = tripleService.findTriple(triple);

        // convert back to json
        JSONArray tripleListAsJson = tripleConverter.tripleListToJson(resultTripleList);

        // convert to string and return 
        return tripleListAsJson.toString();

    }

    /**
     * helper method to
     * <ul> 
     * <li>recieve tbox as string
     * <li>convert answer to JSON
     * <li>convert JSON to string
     * </ul>
     * - 
     * @return
     */
    private String getTbox() {

        // get tbox triples from tbox service
        List<Triple> tboxTripleList = tboxService.getTboxTripleList();

        // convert to json
        JSONArray tboxTripleListAsJson = tripleConverter.tripleListToJson(tboxTripleList);

        // convert to string and return 
        return tboxTripleListAsJson.toString();

    }

    /**
     * method to check if tbox service is already available.<p>
     * should be used to deny any requests if not availably.<p>
     * returns true by default, can be overidden by implementations to introduce some checks.
     * @return
     */
    protected abstract Boolean tboxServiceAvailable();

    /**
     * method to check if tbox service is already available.<p>
     * should be used to deny any requests if not availably.<p>
     * returns true by default, can be overidden by implementations to introduce some checks.
     * @return
     */
    protected abstract Boolean tripleServiceAvailable();

}