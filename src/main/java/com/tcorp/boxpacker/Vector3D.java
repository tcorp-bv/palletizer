package com.tcorp.boxpacker;

import com.google.gson.JsonArray;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Vector;

public class Vector3D extends Vector<Double> {
    public Vector3D() {
        this(0,0,0);
    }
    public Vector3D(double x, double y, double z) {
        super(Arrays.asList(new Double[]{x, y, z}));
    }
    public Vector3D add(double x, double y, double z){
        return new Vector3D(getX() + x, getY() + y, getZ() + z);
    }
    public Vector3D add(Vector3D v2){
        return new Vector3D(getX() + v2.getX(), getY() + v2.getY(), getZ() + v2.getZ());
    }
    public Vector3D subtract(double x, double y, double z){
        return add(-x, -y, -z);
    }
    public Vector3D copy(){
        return new Vector3D(getX(), getY(), getZ());
    }
    public double getX() {
        return super.get(0);
    }
    public double getSurfaceArea(){
        return getX() * getZ();
    }
    public double getY() {
        return super.get(1);
    }

    public double getZ() {
        return super.get(2);
    }
    public double getVolume(){
        return getX() * getY() * getZ();
    }

    public JsonArray getJsonArray(){
        JsonArray array = new JsonArray();
        array.add(getX());
        array.add(getY());
        array.add(getZ());
        return array;
    }
}
