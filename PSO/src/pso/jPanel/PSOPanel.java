package pso.jPanel;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import lombok.Getter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import javax.swing.JPanel;
import lombok.Setter;
import pso.config.Compute;
import pso.config.Configuration;
import pso.function.FunctionInvoke;
import pso.function.FunctionType;
import pso.particle.Particle;
/**
 *
 * @author Tink
 */
public class PSOPanel extends JPanel {
    private FunctionType function;
    private FunctionInvoke functionInvoke;
    private Color[] functionColors;
    private BufferedImage background;
    public double[] resource = {1.5, 2.0, 2.5};
    public double perTransferCost = 0.25;
    public double[] DataToBeProcessed = {60, 70, 80};
    public double[] ProcessingTime = null;
    public double[] tempArray = null;
    private double targetLocation = 0.0d;
    private double globalBest = -Double.MAX_VALUE;
    private Particle[] swarm;
    private double frstBest = 0.0;
    private boolean isFrstBest = true;
    @Getter
    private double bestPositionX, bestPositionY;
    @Getter
    private double best;
    /* Best value for function (minimum) */
    @Getter
    private int iteration;
    boolean isFirst = true;
    private Configuration objConfig ;
    @Setter
    @Getter
    public double status = 0.0f;
    public PSOPanel(Configuration config) {
        this.function = FunctionType.PARABOLA;
        this.functionInvoke = new FunctionInvoke();
        this.functionColors = new Color[256];
        for (int i = 255; i >= 0; i--) {
            this.functionColors[i] = new Color(i / 255.0F, i / 255.0F, i / 255.0F);
        }
        this.background = null;
        targetLocation = getRandomNumberInRange(0.35, 0.65);
        this.objConfig = config;
    }
    public void setFunction(FunctionType type) {
        this.function = type;
        this.swarm = null;
        this.background = null;
        this.repaint();
    }
    private double getRandomNumberInRange(double rangeMin, double rangeMax) {
        Random r = new Random();
        if (rangeMin >= rangeMax) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return (double) rangeMin + (rangeMax - rangeMin) * r.nextDouble();
    }
    public boolean checkSwarm() {
        return this.swarm != null;
    }
    public void createSwarm(int numberOfParticles) {
        double tempBestValue;
        Random rnd = new Random();
        this.iteration = 0;
        this.best = 0.0;
        this.swarm = new Particle[numberOfParticles];
        this.isFirst = false;
        for (int i = 0; i < numberOfParticles; i++) {
            Particle p = new Particle(2, rnd, targetLocation);
            this.swarm[i] = p;
            tempBestValue = functionInvoke.calc(p.getPos_X()[0], p.getPos_Y()[0], function);
            p.setBest_Value(tempBestValue);
            if (tempBestValue > this.best) {
                this.best = tempBestValue;
                this.bestPositionX = p.getPos_X()[0];
                this.bestPositionY = p.getPos_Y()[0];
            }
        }
        this.repaint();
    }
    public void changeSwarm(double CONST1, double CONST2) {
        // this.ProcessingTime = tempArray;
        if (this.swarm == null) {
            return;
        }
        double velocity = Math.pow(++this.iteration, -0.015);
        /* little fix for speed for better minimum search */
        for (int i = 0; i < this.swarm.length; i++) {
            Particle particle = this.swarm[i];
            particle.update(velocity, CONST1, CONST2, this.bestPositionX, this.bestPositionY);
            int actualNumber = particle.getActualNumber();
            double fValue = totalApplicationCost(particle.getPos_X()[actualNumber], particle.getPos_Y()[actualNumber], this.ProcessingTime);
            if (isFrstBest == true) {
                frstBest = best;
                isFrstBest = false;
            }
            if (Double.compare(fValue, particle.getBest_Value()) > 0) {
                double fromParticleX = particle.getPos_X()[actualNumber];
                double fromParticleY = particle.getPos_Y()[actualNumber];
                particle.setBest_Value(fValue);
                particle.setBestPos_X(fromParticleX);
                particle.setBestPos_Y(fromParticleY);
                this.best = fValue;
                this.bestPositionX = fromParticleX;
                this.bestPositionY = fromParticleY;
            }
        }
      
        if (tempArray.length == 3) {
            status = Math.round(((best + (resource[0] * (tempArray[0])) + (resource[1] * (tempArray[1])) + (resource[2] * (tempArray[2]))) * 100000000000.0) / 100000000.0);
            if(status >0) System.out.println(" Best fitness: " + Math.round((best + (resource[0] * (tempArray[0])) + (resource[1] * (tempArray[1])) + (resource[2] * (tempArray[2]))) * 100000000000.0) / 100000000.0);
        } else if (tempArray.length == 2) {
            status = Math.round(((best + (resource[0] * (tempArray[0])) + (resource[1] * (tempArray[1]))) * 100000000000.0) / 100000000.0);
            if(status >0)System.out.println(" Best fitness: " + Math.round((best + (resource[0] * (tempArray[0])) + (resource[1] * (tempArray[1]))) * 100000000000.0) / 100000000.0);
        } else if (tempArray.length == 1) {
            status = Math.round(((best + (resource[0] * (tempArray[0]))) * 100000000000.0) / 100000000.0);
            if(status >0)System.out.println(" Best fitness: " + Math.round((best + (resource[0] * (tempArray[0]))) * 100000000000.0) / 100000000.0);
        }
        globalBest = best;
        this.repaint();
    }
    public double costOfExecution(double[] ProcessingTime) {
        double costExecutionResult = 0;
        double[] temp = this.ProcessingTime;
        for (int i = 0; i < temp.length; i++) {
            ArrayList<Double> list = DoubleStream.of(ProcessingTime).boxed().collect(
                    Collectors.toCollection(new Supplier<ArrayList<Double>>() {
                        public ArrayList<Double> get() {
                            return (new ArrayList<Double>());
                        }
                    }));
            //Collections.shuffle(list);
            double value = list.remove(0);
            costExecutionResult += setDistribution(value);
            temp = list.stream().mapToDouble(Double::doubleValue).toArray();
        }
        return costExecutionResult;
    }
    public double totalTransferCost(double[] ProcessingTime) {
        int length = ProcessingTime.length - 1;
        return perTransferCost * length;
    }
    public double totalApplicationCost(double positionx, double positiony, double[] ProcessingTime) {
        double rndom = (double) (new Random().nextInt(5));
        rndom = rndom * 0.0012;
        double totalCost = costOfExecution(ProcessingTime) + totalTransferCost(ProcessingTime) + rndom;
        totalCost = (totalCost / 10000) + (-4) * (positionx * positionx + positiony * positiony);
        return totalCost;
    }
    public double setDistribution(double processingTime) {
        int iproc = (int) (processingTime);
        double sum = 0;
        int i = 0;
        double[] result = new double[3];
        if (processingTime != 0.0) {
            while (i < 2) {
                int temp = new Random().nextInt(iproc);
                result[i] = temp;
                iproc = iproc - temp;
                i++;
            }
            result[2] = iproc;
            
            sum = result[0] * resource[0] + result[1] * resource[1] + result[2] * resource[2];
        }
        Compute objCmp = new Compute();
        int icount =0;
        for (double d: result){
            if(icount==0)
                objCmp.EC2 = d;
            else if(icount ==1)
                objCmp.RDS=d;
            else if(icount == 2)
                objCmp.S3 = d;
            icount++;
        }
            
            objConfig.addmyList(objCmp);
        return sum;
    }
    public void setProcessedData(double[] processedData) {
        this.ProcessingTime = processedData;
        this.tempArray = processedData;
    }
    @Override
    public void paint(Graphics g) {
        Dimension d;
        /* ustawiamy wielkosc rysunku */
        int width, height;
        /* wielkosc tla */
        int xPixel, yPixel;
        /* zmienne iteracyjne dla pixeli */
        Graphics backgroundGraphics;
        /* grafika dla tla  */
        double[][] pixelValues;
        /* tablica pixeli */
        double min, max, tempFunctionVal;/* minimalna, maxymalna wartosc funkcji i tymczasowa zmienna do kolorow pixeli */
        d = this.getSize();
        width = d.width;
        height = d.height;
        if (isWindowSizeChanged(width, height)) {
            this.background = null;
        }
        if (this.background == null) {
            this.background = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            backgroundGraphics = this.background.getGraphics();
            pixelValues = new double[width][height];
            min = Double.MAX_VALUE;
            max = -Double.MAX_VALUE;
            for (xPixel = width - 1; xPixel >= 0; --xPixel) {
                for (yPixel = height - 1; yPixel >= 0; --yPixel) {
                    tempFunctionVal = functionInvoke.calc((xPixel / (double) (width - 1)) - targetLocation, targetLocation - (yPixel / (double) (height - 1)), function);
                    pixelValues[xPixel][yPixel] = tempFunctionVal;
                    if (tempFunctionVal < min) {
                        min = tempFunctionVal;
                    }
                    if (tempFunctionVal > max) {
                        max = tempFunctionVal;
                    }
                }
            }
            paintPixels(width, height, backgroundGraphics, pixelValues, min, max);
        }
        g.drawImage(this.background, 0, 0, null);
        drawSwarm(g, width, height);
        paintBestParticle(g, width, height);
    }
    private void paintPixels(int width, int height, Graphics backgroundGraphics, double[][] pixelValues, double min, double max) {
        double tempFunctionVal;
        int xPixel;
        int yPixel;
        tempFunctionVal = 255.0 / 1.04 / (max - min);
        min -= 0.02 * (max - min);
        /* calculate color scaling factor */
        for (xPixel = width - 1; xPixel >= 0; --xPixel) {
            for (yPixel = height - 1; yPixel >= 0; --yPixel) {
                int colorIndex = (int) (tempFunctionVal * (pixelValues[xPixel][yPixel] - min));
                if (colorIndex < 0) {
                    colorIndex = 0;
                } else if (colorIndex > 255) {
                    colorIndex = 255;
                }
                backgroundGraphics.setColor(this.functionColors[colorIndex]);
                backgroundGraphics.fillRect(xPixel, yPixel, 1, 1);
            }
        }
    }
    private void drawSwarm(Graphics g, int width, int height) {
        if (this.swarm == null) {
            return;
        }
        for (int i = 0; i < this.swarm.length; i++) {
            this.swarm[i].draw(g, width, height);
        }
    }
    private void paintBestParticle(Graphics g, int width, int height) {
        int bestPixelX = (int) ((targetLocation + this.bestPositionX) * (width - 1));
        int bextPixelY = (int) ((targetLocation - this.bestPositionY) * (height - 1));
        if (!isFirst) {
            g.setColor(Color.black);
            g.fillOval(bestPixelX - 4, bextPixelY - 4, 9, 9);
            g.setColor(Color.blue);
            g.fillOval(bestPixelX - 3, bextPixelY - 3, 7, 7);
        }
    }
    private boolean isWindowSizeChanged(int width, int height) {
        return (this.background != null) && ((width != this.background.getWidth()) || (height != this.background.getHeight()));
    }
}