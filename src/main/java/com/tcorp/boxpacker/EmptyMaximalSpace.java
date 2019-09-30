package com.tcorp.boxpacker;

import com.google.gson.JsonObject;

public class EmptyMaximalSpace {
    private Vector3D origin;
    private Vector3D dimensions;

    public EmptyMaximalSpace(Vector3D origin, Vector3D dimensions) {
        this.origin = origin;
        this.dimensions = dimensions;
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
