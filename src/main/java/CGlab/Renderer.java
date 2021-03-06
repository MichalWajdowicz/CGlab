package CGlab;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static java.lang.Math.round;

public class Renderer {

    public enum LineAlgo { LINE_NAIVE, LINE_DDA, LINE_BRESENHAM, LINE_BRESENHAM_INT; }

    private BufferedImage render;
    public int h = 200;
    public int w = 200;

    private String filename;
    private LineAlgo lineAlgo;

    public Renderer(String filename,int w, int h, LineAlgo lineAlgo) {
        this.w = w;
        this.h = h;
        render = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
        this.lineAlgo=lineAlgo;
    }

    public void drawPoint(int x, int y) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);
        render.setRGB(x, y, white);
    }

    public void drawLine(int x0, int y0, int x1, int y1) {
        if (lineAlgo == LineAlgo.LINE_NAIVE) drawLineNaive(x0, y0, x1, y1);
        if (lineAlgo == LineAlgo.LINE_DDA) drawLineDDA(x0, y0, x1, y1);
        if (lineAlgo == LineAlgo.LINE_BRESENHAM) drawLineBresenham(x0, y0, x1, y1);
        if (lineAlgo == LineAlgo.LINE_BRESENHAM_INT) drawLineBresenhamInt(x0, y0, x1, y1);
    }

    public void drawLineNaive(int x0, int y0, int x1, int y1) {
        int x;
        double dy = y1 - y0;
        double dx = x1 - x0;
        double m = dy / dx;
        double y = y0;

        for (x = x0; x <= x1; x++) {
            drawPoint(x, (int) round(y));
            y = y + m;
        }
    }

    public void drawLineDDA(int x0, int y0, int x1, int y1) {
        // TODO: zaimplementuj
    }

    public void drawLineBresenham(int x0, int y0, int x1, int y1) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1-x0;
        int dy = y1-y0;
        float derr = Math.abs(dy/(float)(dx));
        float err = 0;

        int y = y0;

        for (int x=x0; x<=x1; x++) {
            render.setRGB(x, y, white);
            err += derr;
            if (err > 0.5) {
                y += (y1 > y0 ? 1 : -1);
                err -= 1.;
            }
        } // Oktanty: 8 , 7
    }

    public void drawLineBresenhamInt(int x0, int y0, int x1, int y1) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1-x0;
        int dy = y1-y0;
        int derr = Math.abs(dy/(dx));
        int err = 0;

        int y = y0;

        for (int x=x0; x<=x1; x++) {
            render.setRGB(x, y, white);
            err += (2*derr);
            if (err > 0.5*(2*derr)) {
                y += (y1 > y0 ? 1 : -1);
                err -= 1;
            }
        }
    }

    public Vec3f barycentric(Vec2f A, Vec2f B, Vec2f C, Vec2f P) {
        Vec3f v1 = new Vec3f(B.x - A.x, C.x - A.x, A.x - P.x);// tutaj potrzebujemy wektora sk??adaj??cego si?? ze wsp????rz??dnych
        // x wektor??w AB, AC ora PA.

        Vec3f v2 = new Vec3f(B.y - A.y, C.y - A.y, A.y - P.y);
        // tutaj potrzebujemy wektora sk??adaj??cego si?? ze wsp????rz??dnych
        // y wektor??w AB, AC ora PA.

        Vec3f cross = iloczyn(v1, v2);// iloczyn wektorowy v1 i v2. Wskaz??wka: zaimplementuj do tego oddzieln?? metod??

        Vec2f uv = new Vec2f(cross.x / cross.z, cross.y / cross.z);// wektor postaci: cross.x / cross.z, cross.y / cross.z

        //
        Vec3f barycentric = new Vec3f(uv.x, uv.y, 1 - uv.x - uv.y);// wsp????rz??dne barycentryczne, uv.x, uv.y, 1- uv.x - uv.y
        return barycentric;
    }

    public Vec3f iloczyn(Vec3f v1, Vec3f v2) {
        Vec3f iloczyn = new Vec3f(((v1.y * v2.z) - (v1.z * v2.y)), ((v1.z * v2.x) - (v1.x * v2.z)), ((v1.x * v2.y) - (v1.y * v2.x)));

        return iloczyn;
    }

    public void drawTriangle(Vec2f A, Vec2f B, Vec2f C,Vec3i rgb) {

        float a = min(A.x, B.x, C.x);
        float b = min(A.y, B.y, C.y);
        float c = max(A.x, B.x, C.x);
        float d = max(A.y, B.y, C.y);

        for(float i=a; i<c;i++){
            for(float j=a; j<c; j++){
                Vec2f P = new Vec2f(i,j);
                Vec3f s = barycentric(A,B,C,P);

                if(s.x > 0 && s.y > 0 && s.z > 0){
                    if (rgb.x > 255)
                        rgb.x = 255;
                    if (rgb.y > 255)
                        rgb.y = 255;
                    if (rgb.z > 255)
                        rgb.z = 255;
                    int color = rgb.z | (rgb.y << 8) | (rgb.x << 16) | (255 << 24);
                    render.setRGB((int) i, (int) j, color);
                }

            }
        }
    }

    public void save() throws IOException {
        File outputfile = new File(filename);
        render = Renderer.verticalFlip(render);
        ImageIO.write(render, "png", outputfile);
    }

    public void clear() {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int black = 0 | (0 << 8) | (0 << 16) | (255 << 24);
                render.setRGB(x, y, black);
            }
        }
    }

    public static BufferedImage verticalFlip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return flippedImage;
    }

    public float min(float x, float y, float z) {
        float min = Math.min(x, Math.min(y, z));
        return min;
    }

    public float max(float x, float y, float z) {
        float max = Math.max(x, Math.max(y, z));
        return max;
    }
}
