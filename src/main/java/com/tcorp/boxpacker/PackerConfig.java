package com.tcorp.boxpacker;

import com.tcorp.boxpacker.model.fitness.DefaultFitnessCalculator;
import com.tcorp.boxpacker.model.fitness.FitnessCalculator;
import com.tcorp.boxpacker.model.fitness.PackingCalculator;

public class PackerConfig {
    private int iterations = 1;
    private int populationSize = 30;
    private int bestFitCutoff = 5;
    private double crossoverProbability = 0.5;
    private double mutationProbability = 0.5;
    private boolean verbose = true;
    private boolean plotSolution = true;

    private FitnessCalculator fitnessCalculator = DefaultFitnessCalculator::getFitness;
    private PackingCalculator packingCalculator = DefaultFitnessCalculator::getPackingSolution;

    public int getIterations() {
        return iterations;
    }

    public PackerConfig setIterations(int iterations) {
        this.iterations = iterations;
        return this;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public PackerConfig setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public int getBestFitCutoff() {
        return bestFitCutoff;
    }

    public PackerConfig setBestFitCutoff(int bestFitCutoff) {
        this.bestFitCutoff = bestFitCutoff;
        return this;
    }

    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    public PackerConfig setCrossoverProbability(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
        return this;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public PackerConfig setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
        return this;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public PackerConfig setVerbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public boolean isPlotSolution() {
        return plotSolution;
    }

    public PackerConfig setPlotSolution(boolean plotSolution) {
        this.plotSolution = plotSolution;
        return this;
    }

    public FitnessCalculator getFitnessCalculator() {
        return fitnessCalculator;
    }


    public PackerConfig setFitnessCalculator(FitnessCalculator fitnessCalculator) {
        this.fitnessCalculator = fitnessCalculator;
        return this;
    }

    public PackingCalculator getPackingCalculator() {
        return packingCalculator;
    }

    public PackerConfig setPackingCalculator(PackingCalculator packingCalculator) {
        this.packingCalculator = packingCalculator;
        return this;
    }
}
