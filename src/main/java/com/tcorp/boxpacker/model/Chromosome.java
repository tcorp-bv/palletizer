package com.tcorp.boxpacker.model;

import com.tcorp.boxpacker.util.ArrayUtils;
import com.tcorp.boxpacker.util.RandomUtils;

import java.util.*;

public class Chromosome {
    Random r = new Random();
    //original_index=>new_index
    private Integer[] boxSequence;
    //original_index=>new_index
    private Integer[] containerSequence;
    private HashMap<Object, Long> genes;

    public Integer[] getBoxSequence() {
        return boxSequence;
    }

    public Chromosome(Integer[] boxSequence, Integer[] containerSequence, HashMap<Object, Long> genes) {
        this.boxSequence = boxSequence;
        this.containerSequence = containerSequence;
        this.genes = genes;
    }

    public Long getOrGenerateGene(Object key) {
        Long gene = genes.get(key);
        if(gene == null){
            genes.put(key, r.nextLong());
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
                new HashMap<>()
        );
    }

    public static List<Chromosome> getRandomChromosomes(int populationSize, int numBoxes, int numContainers) {
        List<Chromosome> chromosomes = new ArrayList(populationSize);
        for (int i = 0; i < populationSize; i++)
            chromosomes.add(Chromosome.createRandom(numBoxes, numContainers));
        return chromosomes;
    }


    public Chromosome mutate() {
        //todo: regenerate two genes
        genes.size();
        return new Chromosome(swapRandom(boxSequence), swapRandom(containerSequence), genes);
    }
    public Chromosome mutateGene(double percentage) {
        //todo: regenerate two genes
        genes.size();
        return new Chromosome(swapRandom(boxSequence), swapRandom(containerSequence), genes);
    }
    public Chromosome crossoverWith(Chromosome parent){
        return new Chromosome(
                crossOverRandom(getBoxSequence(), parent.getBoxSequence()),
                crossOverRandom(getContainerSequence(), parent.getContainerSequence()),
                genes
        );


    }
    private static Integer[] crossOverRandom(Integer[] sequence1, Integer[] sequence2){
        if(sequence1.length == 1)
            return sequence1;
        int numItems = sequence1.length;
        List<Integer> cutIndices =  RandomUtils.getPermutationSample(numItems, 2);
        cutIndices.sort(Integer::compareTo);
        Integer[] firstSegment = new Integer[cutIndices.get(0)];
        Integer[] middleSegment = Arrays.copyOfRange(sequence1, cutIndices.get(0) + 1, cutIndices.get(1) + 1);
        Integer[] lastSegment = new Integer[numItems - cutIndices.get(1)];

        List<Integer> missing = getMissing(middleSegment, numItems);
        Integer[] randomizedIndex = RandomUtils.getRandomPermutation(missing.size());
        for(int i = 0; i < firstSegment.length; i++)
            firstSegment[i] = missing.get(randomizedIndex[i]);

        if(lastSegment.length != 0)
        for(int i = 0; i < lastSegment.length; i++)
            lastSegment[i] = missing.get(randomizedIndex[i + firstSegment.length]);
        return ArrayUtils.concatenate(firstSegment, middleSegment, lastSegment);
    }
    private static List<Integer> getMissing(Integer[] segment, int length){
        Set<Integer> segmentSet = new HashSet<>(Arrays.asList(segment));
        List<Integer> missing = new ArrayList<>(length);
        for(int i = 0; i < length; i++)
            if(!segmentSet.contains(i))
                missing.add(i);
        return missing;
    }
    private static Integer[] swapRandom(Integer[] sequence) {
        if(sequence.length == 1)
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
