package com.school;

import processing.core.PApplet;
import processing.core.PVector;

public class Pillar {
    PApplet p;
    float x;
    float openingY;
    float openingHeight;

    public Pillar(PApplet parent, float xOffset, float openingHeight) {
        this.p = parent;
        this.x = xOffset;
        this.openingHeight = openingHeight;
        this.generateOpening();
    }
    void generateOpening() {
        openingY = p.random(openingHeight / 2, p.height - openingHeight / 2);
    }
    void movePillar(float dist) {
        x -= dist;
    }
    Boolean detectCollision(Bird bird) {
        float xDiff = Math.abs(bird.location.x - x);
        if(xDiff < bird.radius) {
            float yDiff = Math.abs(bird.location.y - openingY) + bird.radius;
            return yDiff > openingHeight / 2;
        }
        return false;
    }
    void draw() {
        p.stroke(255);
        p.line(x, 0, x, openingY - openingHeight / 2);
        p.line(x, openingY + openingHeight / 2, x, p.height);
    }
}
