package org.tmf.dsmapi.subscriber.resource;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmf.dsmapi.subscriber.Config;
import org.tmf.dsmapi.subscriber.Server;
import org.tmf.dsmapi.utils.DateUtils;

public class Resource implements HttpHandler {

    private static long index = 0;

    public Resource() {
    }

    public synchronized String getIndex() {
        return Long.toString(index++);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Headers requestHeaders = httpExchange.getRequestHeaders();
        Headers responseHeaders = httpExchange.getResponseHeaders();
        Server.setHeaders(requestHeaders, responseHeaders);

        String method = httpExchange.getRequestMethod();

        String fileName = Config.DIRECTORY
                .concat("/")
                .concat(getIndex())
                .concat("-")
                .concat(DateUtils.getDateAsString())
                .concat(Config.EXTENSION);

        try {

            FileOutputStream outputStream = new FileOutputStream(fileName, true);

            if ("POST".equalsIgnoreCase(method)) {
                outputStream.write("\n".getBytes());
                InputStream bodyS = httpExchange.getRequestBody();
                byte[] buffer = new byte[256];
                int bytesRead = 0;
                while ((bytesRead = bodyS.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                outputStream = null;
                httpExchange.sendResponseHeaders(201, 0);
                httpExchange.getResponseBody().close();
                Current.setCurrent(fileName);

                Logger.getLogger(Resource.class.getName())
                        .log(Level.INFO, "created:" + fileName);
            }

        } catch (FileNotFoundException fnf) {
            Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, fnf);
        }

    }
}
