package com.tcorp.boxpacker.render;

import com.google.gson.JsonArray;
import com.tcorp.boxpacker.*;
import com.tcorp.boxpacker.model.ModelController;
import com.tcorp.boxpacker.model.Population;
import com.tcorp.boxpacker.model.fitness.DefaultFitnessCalculator;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExampleRender {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        Renderer renderer = new Renderer(port);


        List<Container> containers = new ArrayList<>();
        for(int i = 0; i <1;i++)
        containers.add(new Container(new Vector3D(0, 0, 0), new Vector3D(1200, 1800 - 144, 800)));

        List<Box> boxes = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Box box = new Box(new Vector3D(), new Vector3D(100 +500 * Math.random(), 200 + 300 * Math.random(), 200 + 300 * Math.random()), 0);
//            boxes.add(box);
//        }
        for (int i = 0; i < 24; i++) {
            Box box = new Box(new Vector3D(), new Vector3D(540, 420, 240), 0);
            boxes.add(box);
        }
        BoxPacker boxPacker = new BoxPacker();
        PackingSolution solution = boxPacker.pack(boxes, containers, new PackerConfig().setIterations(200).setPopulationSize(5000).setBestFitCutoff(1000).setMutationProbability(0.5)
                .setMutationPercentage(0.1).setCrossoverProbability(0.5).setMutateGeneProbability(0.5).setMutateGenesPercentage(0.5));
//        PackingSolution solution2 = boxPacker.pack(boxes, containers, new PackerConfig().setIterations(100).setPopulationSize(3000).setBestFitCutoff(100).setMutationProbability(0.5).setMutationPercentage(0.05)
//                .setCrossoverProbability(0.5).setMutateGeneProbability(0.5).setMutateGenesPercentage(0.3));
        System.out.println("solution 1: ");
        System.out.println("- fitness: " + DefaultFitnessCalculator.getFitness(solution));
        System.out.println("- stability" + DefaultFitnessCalculator.getStabilityModifierAggregate(solution));
//        System.out.println("solution 2: ");
//        System.out.println("- fitness: " + DefaultFitnessCalculator.getFitness(solution2));
//        System.out.println("- stability" + DefaultFitnessCalculator.getStabilityModifierAggregate(solution2));
//        JsonArray res = new JsonArray();
//        for (EmptyMaximalSpace ems : solution.getEMS(container)) {
//            res.add(ems.getJsonObject());
//        }
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            for (Container container : containers)
                Desktop.getDesktop().browse(new URI("http://localhost:" + port + "/render?cubes=" + URLEncoder.encode(solution.toCubesArray(container).toString(), StandardCharsets.UTF_8.toString())));
        }
    }
}
