package com.tcorp.boxpacker.model.packing;

import com.tcorp.boxpacker.Box;
import com.tcorp.boxpacker.Container;
import com.tcorp.boxpacker.PackingSolution;
import com.tcorp.boxpacker.model.Chromosome;

import java.util.List;

public class DefaultPackingCalculator {
    public static PackingSolution getPackingSolution(List<Box> boxes, List<Container> containers, Chromosome chromosome){
        PackingSolution packingSolution = new PackingSolution(containers);
        int next_box_index = 0;
        for(int chromosome_container_index = 0; chromosome_container_index < chromosome.getContainerSequence().length; chromosome_container_index++){
            Container container = containers.get(chromosome.getContainerSequence()[chromosome_container_index]);

            for(int chromosome_box_index = next_box_index; chromosome_box_index < chromosome.getBoxSequence().length; chromosome_box_index++){
                Box box = boxes.get(chromosome.getBoxSequence()[chromosome_box_index]);
                next_box_index = chromosome_box_index + 1;
                if(next_box_index == chromosome.getBoxSequence().length)
                    return packingSolution;
            }
            //create clone of box with correct origin and add to packingsolution

        }
        return packingSolution;
    }
}
