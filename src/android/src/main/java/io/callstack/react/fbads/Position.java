package io.callstack.react.fbads;

public class Position {
    private int px,py,dx,dy;
    public Position(){

    }
    public Position(int px,int py,int dx,int dy){
        this.dx = dx;
        this.dy = dy;
        this.px = px;
        this.py = py;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getPx() {
        return px;
    }

    public int getPy() {
        return py;
    }
}
