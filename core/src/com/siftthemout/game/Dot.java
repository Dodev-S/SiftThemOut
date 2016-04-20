package com.siftthemout.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;



/**
 * Created by PC on 17-Apr-16.
 */
public class Dot {
    private Texture picture;
    private com.badlogic.gdx.math.Circle circle;
    private Color color;

   public Dot(int x, int y, Color color, Texture picture) {
        this.picture = picture;
        this.circle = new Circle(x, y,picture.getHeight() / 2);
        this.color = color;
    }

    public Circle getCircle() {
        return circle;
    }

    public Color getColor() {
        return color;
    }

    public Texture getPicture() {
        return this.picture;
    }

    public int getX() {
        return (int)this.circle.x;
    }

    public int getY() {
        return (int)this.circle.y;
    }

    public void setX(int x) {
        this.circle.x = x;
    }

    public void setY(int y) {
        this.circle.y = y;
    }
}
