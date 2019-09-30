package com.tcorp.boxpacker.model.fitness;


import com.tcorp.boxpacker.Box;
import com.tcorp.boxpacker.Container;
import com.tcorp.boxpacker.PackingSolution;
import com.tcorp.boxpacker.model.Chromosome;

import java.util.List;

@FunctionalInterface
public interface FitnessCalculator {
    double getFitness(List<Box> boxes, List<Container> containers, Chromosome chromosome);


}
