package bronuh.shit;

/*import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;*/

import io.prometheus.client.exporter.HTTPServer;

public class MetricsServer {

    private final int port;
    HTTPServer server;

    public MetricsServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        server = new HTTPServer(port);
    }

    public void stop() throws Exception {
        server.stop();
    }
}
