import java.io.IOException;
import network.Server;
import network.WebHandler;
import persistence.Storage;
import persistence.Store;
import service.Initialize;
import utils.HttpUtils;

public class App {
    public static void main(String[] args)  {
        App app = new App();
        try {
            app.startApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void startApp() throws ClassNotFoundException, IOException {
        Store store = Storage.open("./store");
        HttpUtils.debugInfo(false);
        startServer(store);
    }

    void startServer(Store store) throws IOException {
        WebHandler handler = new WebHandler();
        Initialize serviceEntry = new Initialize();

        serviceEntry.init(store);
        handler.setApiHandler(serviceEntry);

        Server.listen(8888, handler);
    }
}
