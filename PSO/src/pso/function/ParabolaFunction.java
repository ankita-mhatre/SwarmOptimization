/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso.function;

/**
 *
 * @author Tink
 */
class ParabolaFunction implements Function{
    @Override
    public double calculation(double x, double y) {
        return -4 * (x * x + y * y);
    }
    
}
