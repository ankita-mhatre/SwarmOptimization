/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso.config;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pso.function.FunctionType;

/**
 *
 * @author Tink
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Configuration {

    private int iterations;
    private int particles;
    private double velocity;
    private double const1;
    private double const2;
    private double[] tasksData;
    private FunctionType functionType;
    private List<Compute> myList;

    /**
     * Initial values for algorithm configuration
     */
    @SuppressWarnings("empty-statement")
    public Configuration() {
        particles = 30;
        iterations = 200;
        velocity = 0.5;
        const1 = 0.3;
        const2 = 0.3;
        tasksData = new double[3];
        for (int i = 0; i < 3; i++) {
            tasksData[i] = 0.0d;
        }
        functionType = FunctionType.PARABOLA;
        myList = new ArrayList<>();
    }

    public void addmyList(Compute c) {
        myList.add(0, c);
    }

    public ArrayList<Compute> getTop3Compute() {
        ArrayList<Compute> objlst = new ArrayList<>();
        
            objlst.add(myList.get(0));
        

        //objlst.add(myList.get(1));
        //objlst.add(myList.get(2));
        return objlst;
    }
}
