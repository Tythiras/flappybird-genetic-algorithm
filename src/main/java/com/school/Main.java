package com.school;

import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends PApplet {
    int frameRate = 1000;
    int populationSize = 100;
    float gravity = 0.2f;
    float jumpFactor = 6f;
    float pillarCount = 4;
    float openingHeight = 130;
    float birdX = 250;
    PVector startPoint = new PVector(birdX, 250);
    int pillarThickness = 60;
    int birdRadius = 15;
    float pillarDist;
    int score;
    int topScore = 0;
    int generation;

    int nextPillarIndex;
    //jump trigger point for ai
    float triggerPoint = 0f;

    ArrayList<Pillar> pillars;
    ArrayList<Bird> birds;
    ArrayList<Bird> killedBirds;

    public static void main(String[] args) {
        PApplet.main("com.school.Main");
    }
    public void settings(){
        size(1000,500);
    }
    public void setup(){
        pillarDist = width / pillarCount;
        birds = new ArrayList<Bird>();
        //start population
        for(int i = 0; i < populationSize; i++) {
            Bird bird = new Bird(this, new PVector(startPoint.x, startPoint.y), birdRadius);
            bird.openingUpFactor = random(-10, 10);
            bird.openingDownFactor = random(-10, 10);
            bird.distFactor = random(-10, 10);
            birds.add(bird);
        }
        killedBirds = new ArrayList<Bird>();
        generation = 1;

        generate();
        frameRate(frameRate);

    }
    public void newPopulation() {
        //make correct pairing
        for(int i = 0; i < populationSize; i++) {
            birds.add(getKilledBird().pair(getKilledBird(), new PVector(startPoint.x, startPoint.y)));
        }
        killedBirds = new ArrayList<Bird>();
        generation++;
        generate();
    }
    Bird getKilledBird() {
        //select random solution through rotation wheel method
        float fitnessSum = 0;
        for(Bird bird : killedBirds) {
            fitnessSum += bird.getFitness();
        }
        float r = random(fitnessSum);
        float tempSum = 0;
        for(Bird bird : killedBirds) {
            tempSum += bird.getFitness();
            if(tempSum >= r) {
                return bird;
            }
        }
        return killedBirds.get(0);
    }
    public void generate() {
        score = 0;
        pillars = new ArrayList<Pillar>();
        for(int i = 0; i < pillarCount; i++) {
            pillars.add(new Pillar(this, birdX + 200 + pillarDist * i, openingHeight, pillarThickness));
        }
        nextPillarIndex = 0;
    }

    public void draw(){
        clear();

        textSize(20);
        text("Current: "+score, 50, 20);
        text("Best: "+topScore, 50, 40);
        text("Alive: "+birds.size(), 50, 65);
        text("Generation: "+generation, 50, 85);

        runFrame();
    }
    public void runFrame() {
        //nextPillar detection
        Pillar nextPillar = pillars.get(nextPillarIndex);
        if(nextPillar.x + pillarThickness < (birdX - birdRadius)) {
            if(nextPillarIndex+1 >= pillars.size()) {
                nextPillarIndex = 0;
            } else {
                nextPillarIndex++;
            }
            nextPillar = pillars.get(nextPillarIndex);
        }

        for(int i = birds.size(); i > 0; i--) {
            Bird bird = birds.get(i - 1);

            //ai stuff
            float trigger = bird.getSum(nextPillar);
            if(trigger < triggerPoint) {
                bird.setVelocity(new PVector(0, -jumpFactor));
            }


            //bird movement
            bird.applyAcceleration(new PVector(0, gravity));
            bird.update();
            bird.draw();
            bird.distValue++;


            //collision and update pillar
            boolean collision = false;
            if(bird.location.y + bird.radius > height) collision = true;
            if(!collision) {
                for (Pillar pillar : pillars) {
                    if (pillar.x + pillarThickness / 2 < 0) {
                        score++;
                        if(score > topScore) topScore = score;
                        pillar.x = width + pillarThickness / 2;
                        pillar.generateOpening();
                    } else if (pillar.detectCollision(bird)) {
                        collision = true;
                    }
                }
            }
            if(collision) {
                birds.remove(bird);
                killedBirds.add(bird);
            }
        }
        for(Pillar pillar : pillars) {
            pillar.movePillar(1.5f);
            pillar.draw();
        }
        if(birds.size()==0) {
            System.out.println("Generation done");
            System.out.println(killedBirds.get(killedBirds.size() - 1).distFactor);
            System.out.println(killedBirds.get(killedBirds.size() - 1).openingDownFactor);
            System.out.println(killedBirds.get(killedBirds.size() - 1).openingUpFactor);
            newPopulation();
        }
    }
    //public void keyPressed() {
    //    if(keyCode==32) {
    //        bird.setVelocity(new PVector(0, -jumpFactor));
    //    }
    //}
}
