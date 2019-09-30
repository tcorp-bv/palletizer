package com.tcorp.boxpacker;

import com.google.gson.JsonArray;
import com.tcorp.boxpacker.util.RandomUtils;
import sun.invoke.empty.Empty;

import java.util.*;

public class PackingSolution {
    private Map<Container, List<Box>> solution = new HashMap<>();

    private Map<Container, List<EmptyMaximalSpace>> ems_per_container = new HashMap<>();

    public PackingSolution(List<Container> containers) {
        for (Container container : containers) {
            solution.put(container, new ArrayList<>());
            List<EmptyMaximalSpace> emptyMaximalSpaces = new ArrayList(1);
            emptyMaximalSpaces.add(new EmptyMaximalSpace(container.getOrigin(), container.getDimensions()));
            ems_per_container.put(container, emptyMaximalSpaces);
        }
    }

    public Collection<Container> getContainers() {
        return solution.keySet();
    }

    public double getTotalContainerVolume() {
        double sum = 0;
        for (Container container : solution.keySet())
            sum += container.getDimensions().getVolume();
        return sum;
    }

    public double getTotalBoxesVolume() {
        double sum = 0;
        for (Container container : solution.keySet())
            for (Box box : solution.get(container))
                sum += box.getDimensions().getVolume();
        return sum;
    }

    public List<EmptyMaximalSpace> getEMS(Container container) {
        return ems_per_container.get(container);
    }

    public List<Box> getSolution(Container container) {
        return solution.get(container);
    }

    public boolean addBoxToContainer(Container container, Box box) {
        int targetEmsIndex = getLargestEMSIndex(ems_per_container.get(container));
        EmptyMaximalSpace targetEms = ems_per_container.get(container).get(targetEmsIndex);
        Box orientedBox = getOrientedBox(targetEms, box);
        if (orientedBox == null)
            return false;
        placeOrientedBoxInEms(container, orientedBox, targetEmsIndex);
        return true;
    }

    public boolean addBoxToContainer(Container container, Box box, long gene) {
//        int targetEmsIndex = getLargestEMSIndex(ems_per_container.get(container));
//        EmptyMaximalSpace targetEms = ems_per_container.get(container).get(targetEmsIndex);
//        Box orientedBox = getLuckyOrientation(targetEms, box, gene);
//        if (orientedBox == null)
//            return false;
//        placeOrientedBoxInEms(container, orientedBox, targetEmsIndex);
//        return true;
        return handleWithLuckyEMS(container, box, gene);
    }

    private static int getLargestEMSIndex(List<EmptyMaximalSpace> emptyMaximalSpaces) {
        int maxIndex = 0;
        double maxVolume = emptyMaximalSpaces.get(0).getDimensions().getVolume();
        for (int index = 0; index < emptyMaximalSpaces.size(); index++) {
            EmptyMaximalSpace emptyMaximalSpace = emptyMaximalSpaces.get(index);
            if (emptyMaximalSpace.getDimensions().getVolume() < maxVolume) {
                continue;
            } else if (emptyMaximalSpace.getDimensions().getVolume() == maxVolume) {
                //todo: Pick the smallest coordinates x first y second z last
            } else {
                maxIndex = index;
                maxVolume = emptyMaximalSpace.getDimensions().getVolume();
            }
        }
        return maxIndex;
    }

    private boolean handleWithLuckyEMS(Container container, Box box, long gene) {
        List<EmptyMaximalSpace> emptyMaximalSpaces = ems_per_container.get(container);
        Random geneRandom = new Random(gene);
        //todo generate permutation to avoid repeats
        EmptyMaximalSpace space;
        int tries = 0;
        int emsIndex;
        Box orientation;
        do {
            emsIndex = geneRandom.nextInt(emptyMaximalSpaces.size());
            space = emptyMaximalSpaces.get(emsIndex);
            orientation = getLuckyOrientation(space, box, gene);
            tries++;
        } while (orientation == null && tries != 3 && tries != emptyMaximalSpaces.size());

        if (orientation == null) {
            emsIndex = getLargestEMSIndex(emptyMaximalSpaces);
            EmptyMaximalSpace targetEms = emptyMaximalSpaces.get(emsIndex);
            orientation = getLuckyOrientation(space, box, gene);
        }
        if (orientation == null)
            return false;
        placeOrientedBoxInEms(container, orientation, emsIndex);
        return true;
    }

    private static Box getOrientedBox(EmptyMaximalSpace ems, Box box) {
        //currently we do not orient
        Box min = null;
        double minD = 0;
        for (int indexX = 0; indexX < 3; indexX++) {
            for (int indexY = 0; indexY < 3; indexY++) {
                inner:
                for (int indexZ = 0; indexZ < 3; indexZ++) {
                    if (indexX == indexY || indexY == indexZ || indexX == indexZ)
                        continue inner;//not unique
                    Box anOrientation = box.getReorientClone(indexX, indexY, indexZ);
                    double dX = ems.getDimensions().getX() - anOrientation.getDimensions().getX();
                    if (dX < 0)
                        continue inner;
                    double dY = ems.getDimensions().getY() - anOrientation.getDimensions().getY();
                    if (dY < 0)
                        continue inner;
                    double dZ = ems.getDimensions().getZ() - anOrientation.getDimensions().getZ();
                    if (dZ < 0)
                        continue inner;
                    double closestD = Math.min(dX, Math.min(dY, dZ));
                    if (min == null) {
                        min = anOrientation;
                        minD = closestD;
                    } else if (closestD < minD) {
                        min = anOrientation;
                        minD = closestD;
                    }
                }
            }
        }

        return min;
        //loop over all orientations and find the best one.
    }

    private static Box getLuckyOrientation(EmptyMaximalSpace ems, Box box, long seedGene) {
        //currently we do not orient
        List<Box> orientations = new ArrayList<>();
        Random geneRandom = new Random(seedGene);
        for (int indexX = 0; indexX < 3; indexX++) {
            for (int indexY = 0; indexY < 3; indexY++) {
                inner:
                for (int indexZ = 0; indexZ < 3; indexZ++) {
                    if (indexX == indexY || indexY == indexZ || indexX == indexZ)
                        continue inner;//not unique
                    Box anOrientation = box.getReorientClone(indexX, indexY, indexZ);
                    double dX = ems.getDimensions().getX() - anOrientation.getDimensions().getX();
                    if (dX < 0)
                        continue inner;
//                    if(geneRandom.nextBoolean())
//                        anOrientation = anOrientation.add(dX, 0,0);
                    double dY = ems.getDimensions().getY() - anOrientation.getDimensions().getY();
                    if (dY < 0)
                        continue inner;
                    double dZ = ems.getDimensions().getZ() - anOrientation.getDimensions().getZ();
                    if (dZ < 0)
                        continue inner;
//                    if(geneRandom.nextBoolean())
//                        anOrientation = anOrientation.add(0, 0,dZ);
                    orientations.add(anOrientation);
                }
            }
        }
        if (orientations.size() == 0)
            return null;
        return orientations.get(geneRandom.nextInt(orientations.size()));
    }

    private void placeOrientedBoxInEms(Container container, Box orientedBox, int ems_index) {
        List<EmptyMaximalSpace> container_ems = ems_per_container.get(container);
        EmptyMaximalSpace containerEms = container_ems.get(ems_index);
        EmptyMaximalSpace[] newEmses = getSurroundingEMSes(containerEms, orientedBox);
        container_ems.remove(ems_index);
        container_ems.addAll(Arrays.asList(newEmses));
        solution.get(container).add(new Box(containerEms.getOrigin().add(orientedBox.getOrigin()), orientedBox.getDimensions(), orientedBox.getWeight()));
    }


    public EmptyMaximalSpace[] getSurroundingEMSes(EmptyMaximalSpace containerEms, Box box) {
        return getSurroundingEMSes(containerEms, new ArrayList<>(3), new ArrayList<>(3), box);
    }

    private EmptyMaximalSpace[] getSurroundingEMSes(EmptyMaximalSpace containerEms, List<Integer> maxDims, List<EmptyMaximalSpace> max, Box box) {
        if (maxDims.size() == 3) {
            for (int i = max.size() - 1; i >= 0; i--)
                if (max.get(i).getDimensions().getVolume() == 0)
                    max.remove(i);
            return max.toArray(new EmptyMaximalSpace[max.size()]);
        }
        for (int dim = 0; dim < 3; dim++) {
            if (maxDims.contains(dim))
                continue;
            Vector3D newOrigin = containerEms.getOrigin().copy();
            Vector3D newDimensions = containerEms.getDimensions().copy();
            if(box.getOrigin().get(dim) == 0){ //right side
                newOrigin.set(dim, newOrigin.get(dim));
                newDimensions.set(dim, newDimensions.get(dim) - box.getDimensions().get(dim));
            }else { //left side
                newOrigin.set(dim, newOrigin.get(dim) + box.getDimensions().get(dim));
                newDimensions.set(dim, newDimensions.get(dim) - box.getDimensions().get(dim));
            }

            for (int maxDim : maxDims)
                newDimensions.set(maxDim, box.getDimensions().get(maxDim));
            max.add(new EmptyMaximalSpace(newOrigin, newDimensions));
            maxDims.add(dim);
            return getSurroundingEMSes(containerEms, maxDims, max, box);
        }
        return null;
    }

    public JsonArray toCubesArray(Container container) {
        JsonArray cubes = new JsonArray();
        for (Box box : solution.get(container))
            cubes.add(box.getJsonObject());
        return cubes;
    }
}
