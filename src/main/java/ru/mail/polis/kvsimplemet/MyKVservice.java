package ru.mail.polis.kvsimplemet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

public class MyKVservice implements KVService {
    private static final String PREF = "id=";
    @NotNull
    private final HttpServer myServer;



    public static String getID(@NotNull final String query) {
        if (!query.startsWith(PREF)) {
            throw new IllegalArgumentException("Bad ID");
        }

        final String id = query.substring(PREF.length());
        if (id.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return id;
    }

    public MyKVservice(@NotNull final MyDao dao, int port) throws IOException {
        final int[] operationId = new int[1];
        InetSocketAddress isa = new InetSocketAddress(port);
        this.myServer =
                HttpServer.create(isa, 0);

        this.myServer.createContext(
                "/v0/status",
                http -> {
                    operationId[0] = 200;
                    final String res = "ONLINE";
                    http.sendResponseHeaders(operationId[0], res.length());
                    http.getResponseBody().write(res.getBytes());
                    http.close();
                });

        this.myServer.createContext(
                "/v0/entity",
                new ErrorHandler(
                        http -> {
                            final String id = getID(http.getRequestURI().getQuery());
                            switch (http.getRequestMethod()) {
                                case "GET":
                                    operationId[0] = 200;
                                    final byte[] getValue = dao.get(id);
                                    http.sendResponseHeaders(operationId[0], getValue.length);
                                    http.getResponseBody().write(getValue);
                                    break;
                                case "DELETE":
                                    operationId[0] = 202;
                                    dao.delete(id);
                                    http.sendResponseHeaders(operationId[0], 0);
                                    break;
                                case "PUT":
                                    operationId[0] = 201;

                                    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                                    InputStream instream = http.getRequestBody();
                                    byte[] buf = new byte[8192];
                                    int lgth;
                                    while ((lgth = instream.read(buf)) > 0) {
                                        outstream.write(buf, 0, lgth);
                                    }
                                    final byte[] putValue = outstream.toByteArray();

                                    dao.upsert(id, putValue);
                                    http.sendResponseHeaders(operationId[0], putValue.length);
                                    http.getResponseBody().write(putValue);
                                    break;
                                default:
                                    operationId[0] = 405;
                                    http.sendResponseHeaders(operationId[0], 0);
                                    break;
                            }

                            http.close();

                        }));
    }

    @Override
    public void start() {
        if (myServer==null) {
            this.myServer.start();
        }
        this.myServer.start();
    }

    @Override
    public void stop() {
        int delay = 0;
        this.myServer.stop(delay);
    }

    private static class ErrorHandler implements HttpHandler {
        private final HttpHandler delegate;

        private ErrorHandler(HttpHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                delegate.handle(httpExchange);
            } catch (NoSuchElementException e) {
                httpExchange.sendResponseHeaders(404, 0);
                httpExchange.close();
            } catch (IllegalArgumentException e) {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }
}
