package zeev.fraiman.obliquethrow;

import androidx.annotation.NonNull;

public class Point {
    private float h;
    private float l;
    private float t;
    private float vx;
    private float vy;
    private String direction;


    public Point(float h, float l, float t, float vx, float vy, String direction) {
        this.h = h;
        this.l = l;
        this.t = t;
        this.vx = vx;
        this.vy = vy;
        this.direction = direction;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public float getL() {
        return l;
    }

    public void setL(float l) {
        this.l = l;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @NonNull
    @Override
    public String toString() {
        return "Point{" +
                "h=" +h+", l="+l+", \nt="+t+", \nvx="+vx+", vy="+vy+", "+
                "direction='" + direction+"}";
    }
}
