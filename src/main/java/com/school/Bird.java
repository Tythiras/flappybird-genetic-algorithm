package com.school;

import processing.core.PApplet;
import processing.core.PVector;

public class Bird {
    PApplet p;
    PVector location;
    PVector velocity;
    int radius;

    float distValue;

    float openingUpFactor;
    float openingDownFactor;
    float distFactor;


    public Bird(PApplet parent, PVector startLoc, int radius) {
        this.p = parent;
        location = startLoc;
        velocity = new PVector(0, 0);
        this.radius = radius;
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
        //for fitness if relevant
        //openingValue = next.openingHeight / Math.abs(location.y - next.openingY);

        //Get values
        float openingUp = location.y - (next.openingY - next.openingHeight / 2);
        float openingDown = (next.openingY + next.openingHeight / 2) - location.y;
        float distX = (next.x - location.x) +this.radius;
        //return sum with factors
        return openingUp * openingUpFactor + openingDown * openingDownFactor + distX * distFactor;
    }
    void mutate() {
        float changeAmount = 0;
        float r = p.random(1);
        if(r > 0.9) {
            changeAmount = p.random(-10, 10);
        } else if(r > 0.65) {
            changeAmount = p.random(-1, 1);
        } else if(r > 0.4) {
            changeAmount = p.random(-0.1f, 0.1f);
        } else if(r > 0.2) {
            changeAmount = p.random(-0.01f, 0.01f);
        }


    float f = p.random(3);
        if(f > 2) {
            openingUpFactor+=changeAmount;
        } else if(f > 1) {
            openingDownFactor+=changeAmount;
        } else if(f > 0) {
            distFactor+=changeAmount;
        }
    }
    void draw() {
        p.fill(82, 165, 191, 200);
        p.ellipse(location.x, location.y, radius * 2, radius * 2);
    }

    public Bird pair(Bird mate, PVector startLoc) {
        Bird child = new Bird(p, startLoc, this.radius);
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

