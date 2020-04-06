package com.example.linesketch;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

public class InteractionModel {
    ArrayList<SketchListener> subscribers;
    PointF start;
    PointF end;


    public InteractionModel() {
        subscribers = new ArrayList<>();
        start = new PointF();
        end = new PointF();
    }

    public void addStart(PointF first){
        start = first;
    }

    public void addLast(PointF last){
        end = last;
    }

    public void addSubscribers(SketchListener subscriber) { subscribers.add(subscriber);}

    public void notifySubscribers() {
        for (SketchListener sl : subscribers) {
            sl.modelChanged();
        }
    }
}
