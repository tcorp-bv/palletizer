package com.tcorp.boxpacker;

import com.tcorp.boxpacker.model.Population;
import com.tcorp.boxpacker.model.fitness.DefaultFitnessCalculator;

import java.util.List;

public class BoxPacker {
    /**
     * Packs with default values of packerconfig (iterations=1, populationSize = 30, elitismSize = 5, crossoverProbability = 0.5, mutationProbability = 0.5, verbose = true,  plotSolution = true)
     * @param boxes the list of boxes you wish to optimally place in the containers
     * @param containers the list of containers (eg. pallets)
     * @return
     */
    public PackingSolution pack(List<Box> boxes, List<Container> containers){
        return pack(boxes, containers, new PackerConfig());
    }
    public PackingSolution pack(List<Box> boxes, List<Container> containers, PackerConfig packerConfig){
        Population population = new Population(packerConfig, boxes, containers);
        population.evolve(packerConfig.getIterations());
        return packerConfig.getPackingCalculator().getPackingSolution(boxes, containers, population.getElite());
    }
}
