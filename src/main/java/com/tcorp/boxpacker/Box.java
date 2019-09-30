package com.tcorp.boxpacker;

import com.google.gson.JsonObject;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Box {
    private Vector3D origin;
    private Vector3D dimensions;
    private double weight;

    public Box(Vector3D origin, Vector3D dimensions, double weight) {
        this.origin = origin;
        this.dimensions = dimensions;
        this.weight = weight;
    }
    public Box add(double x, double y, double z){
        return new Box(origin.add(x, y, z), dimensions, weight);
    }
    public Rectangle2D.Double getSurfaceAsRectangle2D(){
        return new Rectangle.Double(origin.getX(), origin.getZ(), dimensions.getX(), dimensions.getZ());
    }
    /**
     * newXAxis, newYAxis and newZAxis should be permutation of 0,1,2
     * @param newXAxis the axis that should become the new x axis
     * @param newYAxis ""
     * @param newZAxis ""
     * @return
     */
    public Box getReorientClone(int newXAxis,int newYAxis, int newZAxis){
        return new Box(getOrigin(), new Vector3D(dimensions.get(newXAxis), dimensions.get(newYAxis), dimensions.get(newZAxis)), getWeight());
    }
    public double getWeight() {
        return weight;
    }
    public Vector3D getOrigin() {
        return origin;
    }

    public Vector3D getDimensions() {
        return dimensions;
    }

    public JsonObject getJsonObject(){
        JsonObject object = new JsonObject();
        object.add("pos", origin.getJsonArray());
        object.add("dim", dimensions.getJsonArray());
        return object;
    }
}
