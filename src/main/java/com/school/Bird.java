package com.school;

import processing.core.PApplet;
import processing.core.PVector;

public class Bird {
    PApplet p;
    PVector location;
    PVector velocity;
    int radius = 15;

    float distValue;
    float openingValue;

    float openingUpFactor;
    float openingDownFactor;
    float distFactor;


    public Bird(PApplet parent, PVector startLoc) {
        this.p = parent;
        location = startLoc;
        velocity = new PVector(0, 0);
    }

    float getFitness() {
        //return distValue + distValue / openingValue*10;
        return distValue;
    }
    void applyAcceleration(PVector acc) {
        velocity.add(acc);
    }
    void setVelocity(PVector vel) {
        velocity = vel;
    }
    void update() {
        location.add(velocity);
    }

    float getSum(Pillar next) {
        //for fitness
        openingValue = next.openingHeight / Math.abs(location.y - next.openingY);

        //Get values
        float openingUp = location.y - (next.openingY - next.openingHeight / 2);
        float openingDown = (next.openingY + next.openingHeight / 2) - location.y;
        float distX = next.x - location.x;
        //return sum with factors
        return openingUp * openingUpFactor + openingDown * openingDownFactor + distX *distFactor;
    }
    void mutate() {
        if(p.random(1) > 0.9) {
            float changeAmount = p.random(-1, 1);
            float f = p.random(3);
            if(f > 2) {
                openingUpFactor+=changeAmount;
            } else if(f > 1) {
                openingDownFactor+=changeAmount;
            } else if(f > 0) {
                distFactor+=changeAmount;
            }
        }
    }
    void draw() {
        p.ellipse(location.x, location.y, radius * 2, radius * 2);
    }

    public Bird pair(Bird mate, PVector startLoc) {
        Bird child = new Bird(p, startLoc);
        child.openingDownFactor = avg(this.openingDownFactor, mate.openingDownFactor);
        child.openingUpFactor = avg(this.openingUpFactor, mate.openingUpFactor);
        child.distFactor = avg(this.distFactor, mate.distFactor);

        child.mutate();
        return child;
    }
    public float avg(float a, float b) {
        return (a + b) / 2;
    }
}

