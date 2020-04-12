package com.example.linesketch;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;

public class SketchModel {

    ArrayList<PointF> points;
    ArrayList<SketchListener> subscribers;
    ArrayList<Line> lines;
    Line selectedLine;

    public SketchModel(){
        points = new ArrayList<>();
        subscribers = new ArrayList<>();
        lines = new ArrayList<>();
        selectedLine = null;
    };

    public void addSubscribers(SketchListener subscriber) { subscribers.add(subscriber);}

    public void notifySubscribers() {
        for (SketchListener sl : subscribers){
            sl.modelChanged();
        }
    }
}
