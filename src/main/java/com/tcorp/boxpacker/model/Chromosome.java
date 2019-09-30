package com.tcorp.boxpacker.model;

import com.tcorp.boxpacker.util.ArrayUtils;
import com.tcorp.boxpacker.util.RandomUtils;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;

import java.util.*;

import static java.lang.Long.max;

public class Chromosome {
    Random r = new Random();
    //original_index=>new_index
    private Integer[] boxSequence;
    //original_index=>new_index
    private Integer[] containerSequence;
    private HashMap<Object, Long> genes;
    private List<Object> genesList;

    public Integer[] getBoxSequence() {
        return boxSequence;
    }

    public Chromosome(Integer[] boxSequence, Integer[] containerSequence, HashMap<Object, Long> genes, List<Object> genesList) {
        this.boxSequence = boxSequence;
        this.containerSequence = containerSequence;
        this.genes = genes;
        this.genesList = genesList;
    }

    public Long getOrGenerateGene(Object key) {
        Long gene = genes.get(key);
        if (gene == null) {
            genes.put(key, r.nextLong());
            genesList.add(key);
            gene = genes.get(key);
        }
        return gene;
    }

    public Chromosome setBoxSequence(Integer[] boxSequence) {
        this.boxSequence = boxSequence;
        return this;
    }

    public Integer[] getContainerSequence() {
        return containerSequence;
    }

    public Chromosome setContainerSequence(Integer[] containerSequence) {
        this.containerSequence = containerSequence;
        return this;
    }

    public static Chromosome createRandom(int nBoxes, int nContainers) {
        return new Chromosome(
                RandomUtils.getRandomPermutation(nBoxes),
                RandomUtils.getRandomPermutation(nContainers),
                new HashMap<>(),
                new ArrayList<>()
        );
    }

    public static List<Chromosome> getRandomChromosomes(int populationSize, int numBoxes, int numContainers) {
        List<Chromosome> chromosomes = new ArrayList(populationSize);
        for (int i = 0; i < populationSize; i++)
            chromosomes.add(Chromosome.createRandom(numBoxes, numContainers));
        return chromosomes;
    }


    public Chromosome mutate(double mutationPercentage) {
        //todo: regenerate two genes
        Chromosome c = this;
        long numgen = c.getOrGenerateGene("MUTNUM");
        double actualMutationPercentage = RandomUtils.getRandomWithMean(RandomGeneratorFactory.createRandomGenerator(new Random(numgen)), mutationPercentage);
        for (int i = 0; i < max(1l, Math.round(getBoxSequence().length * actualMutationPercentage)); i++)
            c = new Chromosome(swapRandom(c.getBoxSequence()), swapRandom(c.getContainerSequence()), genes, genesList);
        c.setGenesAndList((HashMap<Object, Long>) genes.clone(), new ArrayList<>(genesList));
        return c;
    }

    public Chromosome mutateGene(double genePercentage) {
        genes.size();
        if (genes.size() == 0)
            return this;
        HashMap<Object, Long> genesClone = (HashMap<Object, Long>) genes.clone();
        Chromosome c = this;

        long numgen = c.getOrGenerateGene("MUTGENENUMGEN");
        double actualGenePercentage = RandomUtils.getRandomWithMean(RandomGeneratorFactory.createRandomGenerator(new Random(numgen)), genePercentage);

        for (int i = 0; i < max(1l, Math.round(genes.size() * actualGenePercentage)); i++)
            genesClone.put(genesList.get(i), r.nextLong());
        return new Chromosome(getBoxSequence(), getContainerSequence(), genesClone, new ArrayList<>(genesList));
    }

    public Chromosome crossoverWith(Chromosome parent, double crossoverPercentage) {

        Chromosome c = this;

        long numgen = c.getOrGenerateGene("CROSNUMGEN");
        RandomGenerator rng = RandomGeneratorFactory.createRandomGenerator(new Random(numgen));
        double actualBoxGenePercentage = RandomUtils.getRandomWithMean(rng, crossoverPercentage);
        double actualContGenePercentage = RandomUtils.getRandomWithMean(rng, crossoverPercentage);

        for (int i = 0; i < max(1l, Math.round(getBoxSequence().length * actualBoxGenePercentage)); i++)
            c = new Chromosome(crossOverRandom(getBoxSequence(), parent.getBoxSequence()),
                    getContainerSequence(),
                    genes, genesList);
        for (int i = 0; i < max(1l, Math.round(getContainerSequence().length * actualContGenePercentage)); i++)
            c = new Chromosome(getBoxSequence(),
                    crossOverRandom(getContainerSequence(), parent.getContainerSequence()),
                    genes, genesList);
        c.setGenesAndList((HashMap<Object, Long>) genes.clone(), new ArrayList<>(genesList));
        return c;
    }

    public Chromosome setGenesAndList(HashMap<Object, Long> genes, List<Object> genesList) {
        this.genes = genes;
        this.genesList = genesList;
        return this;
    }

    private static Integer[] crossOverRandom(Integer[] sequence1, Integer[] sequence2) {
        if (sequence1.length == 1)
            return sequence1;
        int numItems = sequence1.length;
        List<Integer> cutIndices = RandomUtils.getPermutationSample(numItems, 2);
        cutIndices.sort(Integer::compareTo);
        Integer[] firstSegment = new Integer[cutIndices.get(0)];
        Integer[] middleSegment = Arrays.copyOfRange(sequence1, cutIndices.get(0) + 1, cutIndices.get(1) + 1);
        Integer[] lastSegment = new Integer[numItems - cutIndices.get(1)];

        List<Integer> missing = getMissing(middleSegment, numItems);
        Integer[] randomizedIndex = RandomUtils.getRandomPermutation(missing.size());
        for (int i = 0; i < firstSegment.length; i++)
            firstSegment[i] = missing.get(randomizedIndex[i]);

        if (lastSegment.length != 0)
            for (int i = 0; i < lastSegment.length; i++)
                lastSegment[i] = missing.get(randomizedIndex[i + firstSegment.length]);
        return ArrayUtils.concatenate(firstSegment, middleSegment, lastSegment);
    }

    private static List<Integer> getMissing(Integer[] segment, int length) {
        Set<Integer> segmentSet = new HashSet<>(Arrays.asList(segment));
        List<Integer> missing = new ArrayList<>(length);
        for (int i = 0; i < length; i++)
            if (!segmentSet.contains(i))
                missing.add(i);
        return missing;
    }

    private static Integer[] swapRandom(Integer[] sequence) {
        if (sequence.length == 1)
            return sequence;
        Integer[] newSequence = Arrays.copyOf(sequence, sequence.length);
        int length = newSequence.length;
        // swap two random elements in sequence
        List<Integer> swapIndices = RandomUtils.getPermutationSample(length, 2);

        int e1 = newSequence[swapIndices.get(0)];
        int e2 = newSequence[swapIndices.get(1)];
        newSequence[swapIndices.get(0)] = e2;
        newSequence[swapIndices.get(1)] = e1;

        return newSequence;
    }

}
