package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import utils.HttpUtils;

public class WebHandler implements HttpHandler {

    private static Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("js", "application/javascript");
        mimeTypes.put("json", "application/json");
        mimeTypes.put("html", "text/html");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("jpeg", "text/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("ico", "image/x-icon");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().toString();
        String publicPath = new File("browser/public").getAbsolutePath();

        if (uri.startsWith("/api")) {
            handleApiRequest(exchange);
            return;
        }

        if (uri.equals("/")) {
            uri = "/index.html";
        }

        HttpUtils.debugInfo("<-  " + uri);

        File target = Paths.get(publicPath, uri).toFile();
        HttpUtils.debugInfo(" -> " + target.getAbsolutePath());
        if (!target.exists()) {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
            return;
        }
        
        InputStream input = new FileInputStream(target);
        String mime;

        if ((mime = mimeTypes.get(getSuffix(target.getAbsolutePath()))) != null) {
            exchange.getResponseHeaders().add("Content-Type", mime);
        }

        exchange.sendResponseHeaders(200, target.length());
        input.transferTo(exchange.getResponseBody());

        input.close();
        exchange.close();
    }
    
    String getSuffix(String name) {
        String arr[] = name.split("\\.");
        if (arr.length < 1) {
            return "";
        }

        return arr[arr.length - 1];
    }

    private ApiHandler apiHandler;
    public void setApiHandler(ApiHandler handler) {
        apiHandler = handler;
    }

    private void handleApiRequest(HttpExchange exchange) {
        apiHandler.handle(exchange);
    }
}
