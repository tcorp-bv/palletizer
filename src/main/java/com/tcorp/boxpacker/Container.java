package com.tcorp.boxpacker;

public class Container {
    private Vector3D origin;
    private Vector3D dimensions;

    public Container(Vector3D origin, Vector3D dimensions) {
        this.origin = origin;
        this.dimensions = dimensions;
    }

    public Vector3D getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    public Vector3D getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vector3D dimensions) {
        this.dimensions = dimensions;
    }
}
