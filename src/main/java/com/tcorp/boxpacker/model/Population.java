package com.tcorp.boxpacker.model;

import com.tcorp.boxpacker.Box;
import com.tcorp.boxpacker.Container;
import com.tcorp.boxpacker.PackerConfig;
import com.tcorp.boxpacker.model.fitness.FitnessCalculator;

import java.util.List;

public class Population {
    private List<Chromosome> chromosomes;
    private int bestFitCutoff;
    private double mutationProbability;
    private double mutationPercentage;
    private double crossoverProbability;
    private  double crossoverPercentage;
    private double mutateGeneProbability;
    private double mutateGenePercentage;
    private FitnessCalculator fitnessCalculator;
    private List<Box> boxes;
    private List<Container> containers;

    public Population(PackerConfig config, List<Box> boxes, List<Container> containers) {
        this(Chromosome.getRandomChromosomes(config.getPopulationSize(), boxes.size(), containers.size()),
                boxes,
                containers,
                config.getFitnessCalculator(),
                config.getBestFitCutoff(),
                config.getMutationProbability(),
                config.getMutationPercentage(),
                config.getMutateGeneProbability(),
                config.getMutateGenesPercentage(),
                config.getCrossoverProbability(),
                config.getCrossoverPercentage());
    }

    public Population(List<Chromosome> chromosomes, List<Box> boxes, List<Container> containers,
                      FitnessCalculator fitnessCalculator,
                      int bestFitCutoff,
                      double mutationProbability,
                      double mutationPercentage,
                      double mutateGeneProbability,
                      double mutateGenePercentage,
                      double crossoverProbability,
                      double crossoverPercentage) {
        this.chromosomes = chromosomes;
        this.boxes = boxes;
        this.containers = containers;
        this.fitnessCalculator = fitnessCalculator;
        this.bestFitCutoff = bestFitCutoff;
        this.mutationProbability = mutationProbability;
        this.mutationPercentage = mutationPercentage;
        this.mutationProbability = mutateGeneProbability;
        this.mutateGenePercentage = mutateGenePercentage;
        this.crossoverProbability = crossoverProbability;
        this.crossoverPercentage = crossoverPercentage;
    }

    public void evolve(int cycles){
        for(int i = 0; i < cycles; i++) {
            System.out.println("Running evolution " + (i + 1) + "...");
            evolve();
        }
    }
    /**
     * Go to the next iteration
     */
    public void evolve(){
        chromosomes = ModelController.step(boxes, containers, chromosomes, fitnessCalculator, bestFitCutoff, mutationProbability,
                mutationPercentage, mutateGeneProbability, mutateGenePercentage, crossoverProbability, crossoverPercentage);
    }

    /**
     * Calculate one final elite
     * @return
     */
    public Chromosome getElite(){
        return ModelController.calculateBestFit(boxes, containers, chromosomes, bestFitCutoff, fitnessCalculator).get(0);
    }
    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }
    public int getSize(){
        return chromosomes.size();
    }
}
