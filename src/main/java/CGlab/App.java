package CGlab;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    String version = "0.02";

    public static void main(String[] args) {

        String path = args[0];
        int w = Integer.parseInt(args[1]);
        int h = Integer.parseInt(args[2]);
        Renderer.LineAlgo l = Renderer.LineAlgo.valueOf(args[3]);

        Renderer mainRenderer = new Renderer(path, w, h, l);
        mainRenderer.clear();
        mainRenderer.drawLine(20,20,200,300);

        try {
            mainRenderer.save();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getVersion() {
	return this.version;
    }
}
