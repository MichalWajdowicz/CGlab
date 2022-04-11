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
        mainRenderer.drawTriangle(new Vec2f(50,150), new Vec2f(150,54), new Vec2f(25, 50),new Vec3i(0,0,255));


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
