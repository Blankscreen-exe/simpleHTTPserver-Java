package org.example;

import java.io.IOException;
import java.io.OutputStream; // used to write response body to network socket
import java.io.InputStream; // used to read request body from network socket

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Main {
    // static configuration for the server
    static short PORT = 8000;
    static String HOST = "" ;

    static Utilities utilities = new Utilities();

    public static void main(String[] args) throws Exception {
        // fetching the host name
        InetAddress address = InetAddress.getLocalHost();
        HOST = address.getHostName();

        // create an HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // routes for the API
        server.createContext("/", new Home());
        server.createContext("/parse", new ParseToString());
        server.createContext("/parse/json", new ParseToJSON());
//        server.createContext("/parse/xml", new ParseToXML());

        // print the link to the server
        System.out.println(String.format(">> Server Running On: http://%s:%d ", HOST, PORT));

        // start the server
        server.setExecutor(null);
        server.start();
    }

    // Class for handling the Home/base route
    static class Home implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // response string
            String response = "THIS IS HOME";

            // sending response code
            exchange.sendResponseHeaders(200, response.getBytes().length);

            // sending the actual response to the browser/client
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class ParseToString implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // fetching request elements
            String requestMethod = exchange.getRequestMethod();
            Headers requestHeaders = exchange.getRequestHeaders();
            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getQuery();
            String path = requestURI.getPath();

            // parsing the request body
            InputStream requestBody = exchange.getRequestBody();
            String responseBodyString = null;
            try {
                responseBodyString = utilities.ReqBodyToByteArray(requestBody);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // declaring a string response
            String response = "PARSED!\n";
            response += String.format("Request method:          %s \n", requestMethod);
            response += String.format("Request header(s):       %s \n", requestHeaders);
            response += String.format("Query parameter(s):      %s \n", query);
            response += String.format("Query path:              %s \n", path);
            response += String.format("Response body string:    %s \n", responseBodyString);

            // sending the response code
            exchange.sendResponseHeaders(200, response.getBytes().length);
            // sending the actual response
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class ParseToJSON implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // fetching request elements
            String requestMethod = exchange.getRequestMethod();
            Headers requestHeaders = exchange.getRequestHeaders();
            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getQuery();
            String path = requestURI.getPath();


            // parsing the request body
            InputStream requestBody = exchange.GetRequestBody();
            String responseBodyString = null;
            try {
                responseBodyString = utilities.ReqBodyToByteArray(requestBody);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            String jsonResponse = utilities.GetJSONResponse(
                    requestMethod=requestMethod,
                    requestHeaders=requestHeaders,
                    responseBodyString=responseBodyString,
                    query=query,
                    path=path
            );

            // sending the response code
            exchange.sendResponseHeaders(
                    200,
                    jsonResponse.getBytes().length
            );
            // sending the actual response
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }
    }
}