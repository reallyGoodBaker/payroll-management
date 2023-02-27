package utils;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.net.httpserver.HttpExchange;

public class HttpUtils {

    public static Map<String, String> parseQuery(String str) throws URISyntaxException {
        str = new URI(str).getPath();
        String[] queryPairs = str.split("\\&");
        Map<String, String> queries = new HashMap<>();

        for (String pairStr : queryPairs) {
            String[] pair = pairStr.split("=");
            if (pair == null) {
                throw new URISyntaxException("", "");
            }

            queries.put(pair[0], pair[1]);
        }
        
        return queries;
    }

    public static String toQueryString(Map<String, String> query) throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<>();
        for (Entry<String, String> entry : query.entrySet()) {
            list.add(entry.getKey() + "=" + entry.getValue());
        }

        String queryStr = ArrayUtils.join(list.toArray(new String[0]), "&");
        return URLEncoder.encode(queryStr, "utf-8");
    }

    public static String getResponseBodyString(HttpExchange exchange) {
        try {
            return new String(exchange.getRequestBody().readAllBytes());
        } catch (Exception e) {
            return null;
        }
    }

    public static void addJsonHeader(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
    }

    private static boolean showingDebugInfo = true;
    public static boolean debugInfo() {
        return showingDebugInfo;
    }

    public static void debugInfo(boolean bool) {
        showingDebugInfo = bool;
    }

    public static void debugInfo(String info) {
        if (showingDebugInfo) {
            System.out.println(info);
        }
    }

}