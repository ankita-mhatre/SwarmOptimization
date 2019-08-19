# ParticleSwarmOptimization

Optimizing Resource Scheduling in Cloud Computing using Particle Swarm Optimization

We present a particle swarm optimized approach to schedule applications to cloud resources that takes into account both computation cost and data transmission cost. We experiment with a workflow application by varying its computation and communication costs. We compare the cost savings when using PSO. Our results show that PSO can achieve: a) optimum cost , and b) good distribution of workload onto resources.
The mapping of tasks of an application workflow to distributed resources can have several objectives. We focus on minimizing the total cost of computation of an application workflow.

# Steps

1: Set particle dimension as equal to the size of tasks in {ti} ∈ T

2: Initialize particles position randomly from C = 1, ..., j and velocity vi randomly.

3: For each particle, calculate its fitness value as in Equation 4.

4: If the fitness value is better than the previous best pbest, set the current fitness value as the new pbest.

5: After Steps 3 and 4 for all particles, select the best particle as gbest.

6: For all particles, calculate velocity using Equation 6 and update their positions using Equation 7.

7: If the stopping criteria or maximum iteration is not satisfied, repeat from Step 3.


# Output


![psooutput](https://user-images.githubusercontent.com/32042786/63285896-c51d1580-c284-11e9-98de-f368079f23a6.gif)
