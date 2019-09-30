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
        for(int i = 0; i <11;i++)
        containers.add(new Container(new Vector3D(0, 0, 0), new Vector3D(1200, 1800 - 144, 800)));

        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Box box = new Box(new Vector3D(), new Vector3D(1200 * Math.random(), 800 * Math.random(), 800 * Math.random()), 0);
            boxes.add(box);
        }

        BoxPacker boxPacker = new BoxPacker();
        PackingSolution solution = boxPacker.pack(boxes, containers, new PackerConfig().setIterations(20).setPopulationSize(1000).setBestFitCutoff(200).setMutationProbability(0.5).setCrossoverProbability(0.5));
        System.out.println(DefaultFitnessCalculator.getFitness(solution));
        System.out.println(DefaultFitnessCalculator.getStabilityModifierAggregate(solution));
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
