package com.school;

import processing.core.PApplet;
import processing.core.PVector;

public class Pillar {
    PApplet p;
    float x;
    float openingY;
    float openingHeight;
    float thickness;

    public Pillar(PApplet parent, float xOffset, float openingHeight, int thickness) {
        this.p = parent;
        this.x = xOffset;
        this.openingHeight = openingHeight;
        this.generateOpening();
        this.thickness = thickness;
    }
    void generateOpening() {
        openingY = p.random(openingHeight / 2, p.height - openingHeight / 2);
    }
    void movePillar(float dist) {
        x -= dist;
    }
    Boolean detectCollision(Bird bird) {
        float xDiff = Math.abs(bird.location.x - x);
        if(xDiff < bird.radius + thickness / 2) {
            float yDiff = Math.abs(bird.location.y - openingY) + bird.radius;
            return yDiff > openingHeight / 2;
        }
        return false;
    }
    void draw() {
        p.fill(50, 255, 128);
        p.rect(x - thickness / 2, 0, thickness, openingY - openingHeight / 2);
        p.rect(x - thickness / 2, openingY + openingHeight / 2, thickness, p.height);
    }
}
