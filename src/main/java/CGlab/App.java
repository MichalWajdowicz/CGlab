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
        mainRenderer.drawLine(320,240,480,480);//1
        mainRenderer.drawLine(320,240,160,480);//2
        mainRenderer.drawLine(320,240,0,360);//3
        mainRenderer.drawLine(320,240,0,120);//4
        mainRenderer.drawLine(320,240,160,0);//5
        mainRenderer.drawLine(320,240,480,0);//6
        mainRenderer.drawLine(320,240,639,120);//7
        mainRenderer.drawLine(320,240,639,360);//8

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
