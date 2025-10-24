package fr.delivrooom.application.model;

import java.io.Serializable;
import java.util.List;

public class TourSolutionSerialiser implements Serializable {
    public List<String> mapNodes;
    public List<Long> verticesOrder;
    public List<Long> deliveryOrder;
    public List<Long> deliveryOrderTime;
    public float totalLength;

    public TourSolutionSerialiser(List<Long> verticesOrder, List<Long> deliveryOrder, List<Long> deliveryOrderTime, float totalLength) {
        this.verticesOrder = verticesOrder;
        this.deliveryOrder = deliveryOrder;
        this.deliveryOrderTime = deliveryOrderTime;
        this.totalLength = totalLength;
    }
}

