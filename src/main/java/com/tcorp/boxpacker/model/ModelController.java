package com.tcorp.boxpacker.model;


import com.tcorp.boxpacker.Box;
import com.tcorp.boxpacker.Container;
import com.tcorp.boxpacker.model.fitness.FitnessCalculator;
import com.tcorp.boxpacker.util.RandomUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ModelController {
    private static Random r = new Random();

    public static List<Chromosome> step(List<Box> boxes, List<Container> containers, List<Chromosome> population, FitnessCalculator fitnessCalculator, int bestFitCutoff, double mutationProbability,
                                        double mutationPercentage, double mutateGeneProbability, double mutateGenePercentage, double crossOverProb, double crossoverPercentage) {

        List<Chromosome> bestFit = calculateBestFit(boxes, containers, population, bestFitCutoff, fitnessCalculator);
        System.out.println("Current best fit: " + fitnessCalculator.getFitness(boxes, containers, bestFit.get(0)));
        List<Chromosome> populationFromBestFit = getClonedPopulationFromBestFit(bestFit, population.size());
        populationFromBestFit = mutate(populationFromBestFit, mutationProbability, mutationPercentage);
        populationFromBestFit = mutateGenes(populationFromBestFit, mutateGeneProbability, mutateGenePercentage);
        populationFromBestFit = crossover(populationFromBestFit, mutationProbability, crossoverPercentage);
        return populationFromBestFit;
    }

    public static List<Chromosome> calculateBestFit(List<Box> boxes, List<Container> containers, List<Chromosome> population, int bestFitCutoff, FitnessCalculator fitnessCalculator) {
        System.out.println("start sort");
        List<Chromosome> sortedChromosomes = new ArrayList<>(population);
        Map<Chromosome, Double> fitnessCache = population.parallelStream().collect(Collectors.toMap(
                chromosome -> chromosome,
                chromosome -> fitnessCalculator.getFitness(boxes, containers, chromosome),
                (x1, x2) -> Math.max(x1, x2)
        ));

        sortedChromosomes.sort((c1, c2) -> {
            double f1 = fitnessCache.get(c1);
            double f2 = fitnessCache.get(c2);
            return f1 > f2 ? 1 : (f2 == f1 ? 0 : -1);
        });
        System.out.println("end sort");
        return new ArrayList<Chromosome>(sortedChromosomes.subList(0, bestFitCutoff));
    }

    public static List<Chromosome> getClonedPopulationFromBestFit(List<Chromosome> bestFit, int populationSize) {
        int bestFitSize = bestFit.size();
        ArrayList<Chromosome> newPopulation = new ArrayList<>(populationSize);

        int index = r.nextInt(bestFit.size());//random starting index then start adding to population in a round robin manner
        for (int i = 0; i < populationSize; i++) {
            newPopulation.add(bestFit.get(index));
            index++;
            if (index == bestFitSize)
                index = 0;
        }
        return newPopulation;

    }

    public static List<Chromosome> mutate(List<Chromosome> population, double mutationProbability, double mutationPercentage) {

        List<Chromosome> newPopulation = new ArrayList<>(population.size());
        for (Chromosome chromosome : population) {
            if (r.nextDouble() < mutationProbability)
                newPopulation.add(chromosome.mutate(mutationPercentage));
            else
                newPopulation.add(chromosome);

        }
        return newPopulation;
    }
    public static List<Chromosome> mutateGenes(List<Chromosome> population, double geneProbability, double genePercentage) {

        List<Chromosome> newPopulation = new ArrayList<>(population.size());
        for (Chromosome chromosome : population) {
            if (r.nextDouble() < geneProbability)
                newPopulation.add(chromosome.mutateGene(genePercentage));
            else
                newPopulation.add(chromosome);

        }
        return newPopulation;
    }
    public static List<Chromosome> crossover(List<Chromosome> population, double crossoverProbability, double crossoverPercentage) {
        List<Chromosome> mating_pool = new ArrayList<>(population);
        List<Chromosome> nextPopulation = new ArrayList<>(mating_pool.size());
        while (mating_pool.size() > 0) {
            List<Integer> indices = RandomUtils.getPermutationSample(mating_pool.size(), 2);
            indices.sort(Comparator.comparingInt(value -> value));
            if (r.nextDouble() < crossoverProbability) {
                Chromosome child1 = mating_pool.get(indices.get(0)).crossoverWith(mating_pool.get(indices.get(1)), crossoverPercentage);
                Chromosome child2 = mating_pool.get(indices.get(1)).crossoverWith(mating_pool.get(indices.get(0)), crossoverPercentage);
                nextPopulation.add(child1);
                nextPopulation.add(child2);
            } else {
                nextPopulation.add(mating_pool.get(indices.get(0)));
                nextPopulation.add(mating_pool.get(indices.get(1)));
            }
            mating_pool.remove(indices.get(1).intValue());
            mating_pool.remove(indices.get(0).intValue());
        }
        return nextPopulation;
    }

}
