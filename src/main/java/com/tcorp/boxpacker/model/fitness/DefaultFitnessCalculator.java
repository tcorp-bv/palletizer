package com.tcorp.boxpacker.model.fitness;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import com.tcorp.boxpacker.Box;
import com.tcorp.boxpacker.Container;
import com.tcorp.boxpacker.PackingSolution;
import com.tcorp.boxpacker.model.Chromosome;

import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DefaultFitnessCalculator {

    public static double getFitness(List<Box> boxes, List<Container> containers, Chromosome chromosome) {
        return getFitness(getPackingSolution(boxes, containers, chromosome));
    }
    //lower fitness is better
    public static double getFitness(PackingSolution packingSolution) {
        double fitness = 1 - (packingSolution.getTotalBoxesVolume() / packingSolution.getTotalContainerVolume());
        fitness =   fitness*getStabilityModifierAggregate(packingSolution);
        return fitness;
    }

    public static PackingSolution getPackingSolution(List<Box> boxes, List<Container> containers, Chromosome chromosome) {
        List<Container> arrangedContainers = Arrays.asList(new Container[containers.size()]);

        for (int i = 0; i < chromosome.getContainerSequence().length; i++)
            arrangedContainers.set(chromosome.getContainerSequence()[i], containers.get(i));
        List<Box> arrangedBoxes = Arrays.asList(new Box[boxes.size()]);

        List<Long> boxGenes = Arrays.asList(new Long[arrangedBoxes.size()]);
        for (int i = 0; i < chromosome.getBoxSequence().length; i++) {
            boxGenes.set(chromosome.getBoxSequence()[i], chromosome.getOrGenerateGene("BO_" + i));
            arrangedBoxes.set(chromosome.getBoxSequence()[i], boxes.get(i));
        }
        return getPackingSolution(arrangedContainers, arrangedBoxes, boxGenes);
    }

    private static PackingSolution getPackingSolution(List<Container> arrangedContainers, List<Box> arrangedBoxes, List<Long> boxGenes) {
        PackingSolution packingSolution = new PackingSolution(arrangedContainers);

        int containerIndex = 0;
        for (int i =0; i < arrangedBoxes.size(); i ++) {
            Box arrangedBox = arrangedBoxes.get(i);
            Container container = arrangedContainers.get(containerIndex);
            while (!packingSolution.addBoxToContainer(container, arrangedBox, boxGenes.get(i))) {
                containerIndex++;
                if (arrangedContainers.size() <= containerIndex)
                    containerIndex = 0;
                    break;
            }
        }
        return packingSolution;
    }

    public static double getStabilityModifierAggregate(PackingSolution packingSolution) {
        double modifier = 1;
        for (Container container : packingSolution.getContainers()) {
            modifier *= getStabilityModifier(packingSolution.getSolution(container));
        }
        return 1 + modifier/packingSolution.getContainers().size();
    }
    //value between 0 and 1 => 1 is good, 0 is bad
    private static double getStabilityModifier(List<Box> boxes) {
        HashMap<BigDecimal, List<Box>> roofsAtLevel = new HashMap<>(boxes.size());
        for (Box box : boxes) {
            BigDecimal roofLevel = new BigDecimal(box.getDimensions().getY() + box.getOrigin().getY()).round(MathContext.DECIMAL32);
            if (!roofsAtLevel.containsKey(roofLevel))
                roofsAtLevel.put(roofLevel, new ArrayList<>());
            roofsAtLevel.get(roofLevel).add(box);
        }
        double modifier = 1;
        for (Box box : boxes) {
            BigDecimal y = new BigDecimal(box.getOrigin().getY()).round(MathContext.DECIMAL32);
            if (!roofsAtLevel.containsKey(y)) {
                if (box.getOrigin().getY() != 0) {
                    modifier *=0;
                    //System.out.println("Box is floating?");
                }else
                    modifier *= 1;
            } else {
                double surface = 0;
                for (Box roofBox : roofsAtLevel.get(y)) {
                    Rectangle2D intersect = roofBox.getSurfaceAsRectangle2D().createIntersection(box.getSurfaceAsRectangle2D());
                    surface += intersect.getWidth() * intersect.getHeight();
                }
                double surfaceCovered = Math.max(0, surface / box.getDimensions().getSurfaceArea());
                modifier *= surfaceCovered;
            }
        }
        return modifier;

    }

}
