package com.example.linesketch;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;

public class SketchModel {

    ArrayList<PointF> points;
    ArrayList<SketchListener> subscribers;
    ArrayList<Line> lines;
    Line selectedLine;
    int reference;

    public SketchModel(){
        points = new ArrayList<>();
        subscribers = new ArrayList<>();
        lines = new ArrayList<>();
        selectedLine = null;
        reference = 0;
    };

    public void addDot(float x, float y) {
        this.points.add(new PointF(x,y));
        notifySubscribers();
    }

    public void addLine(PointF x, PointF y) {
        this.lines.add(new Line(x,y));
        notifySubscribers();
    }

    public boolean lineSelection(ArrayList<Line> lines,PointF point) {
        for(int i=0;i<lines.size();i++){
            if (Math.abs(lines.get(i).distanceFromLine(point.x, point.y)) < 0.05){
                selectedLine = lines.get(i);
                this.reference = i;
                notifySubscribers();
                return true;
            }
        }
        return false;
    }

    public void startMove(int x, int y){
        System.out.println("start");
        selectedLine.Start.x = x;
        selectedLine.Start.y = y;
        notifySubscribers();
    };

    public void endMove(int x, int y){
        System.out.println("end");
        selectedLine.End.x = x;
        selectedLine.End.y = y;
        notifySubscribers();
    };

    public void clearSelection(){
        selectedLine = null;
        notifySubscribers();
        return;
    };

    public void addSubscribers(SketchListener subscriber) { subscribers.add(subscriber);}

    public void notifySubscribers() {
        for (SketchListener sl : subscribers){
            sl.modelChanged();
        }
    }

}
