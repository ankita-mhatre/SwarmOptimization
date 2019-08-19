/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso.particle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Tink
 */
@Getter
@Setter
public class Particle {
    double[] pos_X, pos_Y;
    double speed_X, speed_Y;
    double bestPos_X, bestPos_Y;
    double best_Value;
    int size;
    int actualNumber;
    private double targetLocation;
    
     public Particle(int size, Random generator) {
        double randomPositionX, randomPositionY;
        do {
            randomPositionX = (generator.nextDouble() - 0.5) * 0.96;
            randomPositionY = (generator.nextDouble() - 0.5) * 0.96;
        } while (isNotNearInCentrum(randomPositionX, randomPositionY));
        this.init(size, randomPositionX, randomPositionY);
    }

    public Particle(int size, Random generator, double targetLocation) {
        double randomPositionX, randomPositionY;
        this.targetLocation = targetLocation;
        do {
            randomPositionX = (generator.nextDouble() - targetLocation) * 0.96;
            randomPositionY = (generator.nextDouble() - targetLocation) * 0.96;
        } while (isNotNearInCentrum(randomPositionX, randomPositionY));
        this.init(size, randomPositionX, randomPositionY);
    }
    
    private boolean isNotNearInCentrum(double randomPositionX, double randomPositionY) {
        return randomPositionX * randomPositionX + randomPositionY * randomPositionY < 0.09;
    }
    
    private void init(int size, double x, double y) {
        int i;

        this.bestPos_X = x;
        this.bestPos_Y = y;
        this.speed_X = 0;
        this.speed_Y = 0;
        this.actualNumber = 0;

        this.size = ++size;
        this.pos_X = new double[size];
        this.pos_Y = new double[size];

        for (i = 0; i < size; i++) {
            this.pos_X[i] = x;
            this.pos_Y[i] = y;
        }
    }
    
        public void draw(Graphics graphics, int width, int height) {
        int sx = (int) ((targetLocation + this.pos_X[this.actualNumber]) * (width - 1));
        int sy = (int) ((targetLocation - this.pos_Y[this.actualNumber]) * (height - 1));

        graphics.setColor(Color.black);
        graphics.fillOval(sx - 4, sy - 4, 9, 9);
        graphics.setColor(Color.red);
        graphics.fillOval(sx - 3, sy - 3, 7, 7);
    }
    public void update(double omega, double alpha, double beta, double globalBestX, double globalBestY) {
        double currentX = this.pos_X[this.actualNumber];
        double currentY = this.pos_Y[this.actualNumber];

        this.speed_X = omega * this.speed_X + alpha * (this.bestPos_X - currentX) + beta * (globalBestX - currentX);
        this.speed_Y = omega * this.speed_Y + alpha * (this.bestPos_Y - currentY) + beta * (globalBestY - currentY);

        this.pos_X[this.actualNumber] = currentX + this.speed_X;
        this.pos_Y[this.actualNumber] = currentY + this.speed_Y;
    }

}
