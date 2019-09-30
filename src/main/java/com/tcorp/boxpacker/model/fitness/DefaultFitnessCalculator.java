package com.tcorp.boxpacker.model.fitness;

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

import static java.lang.Double.max;

public class DefaultFitnessCalculator {

    public static double getFitness(List<Box> boxes, List<Container> containers, Chromosome chromosome) {
        return getFitness(getPackingSolution(boxes, containers, chromosome));
    }
    //lower fitness is better
    public static double getFitness(PackingSolution packingSolution) {
        double maxThis = packingSolution.getTotalBoxesVolume() / packingSolution.getTotalContainerVolume();
        double fitness = 1 - maxThis;
        fitness *=1;
        fitness *= (getStabilityModifierAggregate(packingSolution) + 1);
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
    //1 is bad, 0 is good
    public static double getStabilityModifierAggregate(PackingSolution packingSolution) {
        double modifier = 0;
        for (Container container : packingSolution.getContainers()) {
            double containerStabModifier =  getStabilityModifier(packingSolution.getSolution(container));;
            modifier += containerStabModifier;
            if(containerStabModifier > 0.5)
                modifier+= packingSolution.getContainers().size() * 1000;
        }
        return modifier / packingSolution.getContainers().size();
    }
    //value between 0 and 1 => 1 is bad, 0 is good
    private static double getStabilityModifier(List<Box> boxes) {
        HashMap<BigDecimal, List<Box>> roofsAtLevel = new HashMap<>(boxes.size());
        for (Box box : boxes) {
            BigDecimal roofLevel = new BigDecimal(box.getDimensions().getY() + box.getOrigin().getY()).round(MathContext.DECIMAL32);
            if (!roofsAtLevel.containsKey(roofLevel))
                roofsAtLevel.put(roofLevel, new ArrayList<>());
            roofsAtLevel.get(roofLevel).add(box);
        }
        double modifier = 0;
        for (Box box : boxes) {
            BigDecimal y = new BigDecimal(box.getOrigin().getY()).round(MathContext.DECIMAL32);
            if (!roofsAtLevel.containsKey(y)) {
                if (box.getOrigin().getY() != 0) {
                    modifier += boxes.size() * 1000;
                    //System.out.println("Box is floating?");
                }else
                    modifier += 0;
            } else {
                double surface = 0;
                for (Box roofBox : roofsAtLevel.get(y)) {
                    Rectangle2D intersect = roofBox.getSurfaceAsRectangle2D().createIntersection(box.getSurfaceAsRectangle2D());
                    surface += intersect.getWidth() * intersect.getHeight();
                }
                double surfaceCovered = Math.max(0, surface / box.getDimensions().getSurfaceArea());
                modifier += 1- surfaceCovered;
                if(surfaceCovered < 0.8)
                    modifier += boxes.size() * 1000;
            }
        }
        return  modifier / boxes.size();
    }

}
