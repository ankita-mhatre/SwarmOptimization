/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso.function;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tink
 */
public class FunctionInvoke {
    
    private static final Map<FunctionType, Function> functions = createMap();
    private static Map<FunctionType, Function> createMap() {
        Map<FunctionType, Function> functions = new HashMap<>();

        functions.put(FunctionType.PARABOLA, new ParabolaFunction());

        return functions;
    }

    public double calc(double x, double y, FunctionType type) {
        return functions.get(type).calculation(x, y);
    }
    
}
