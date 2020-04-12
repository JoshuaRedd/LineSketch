package com.example.linesketch;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

public class InteractionModel {
    ArrayList<PointF> points;
    ArrayList<SketchListener> subscribers;
    ArrayList<Line> lines;
    Line selectedLine;
    PointF start;
    PointF end;

    public InteractionModel(){
        points = new ArrayList<>();
        subscribers = new ArrayList<>();
        lines = new ArrayList<>();
        selectedLine = null;
        start = new PointF();
        end = new PointF();
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
                selectedLine = this.lines.get(i);
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
