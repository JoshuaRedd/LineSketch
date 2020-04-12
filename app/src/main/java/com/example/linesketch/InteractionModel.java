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
    int originalPosition;
    PointF start;
    PointF end;

    public InteractionModel(){
        points = new ArrayList<>();
        subscribers = new ArrayList<>();
        lines = new ArrayList<>();
        selectedLine = null;
        originalPosition = 0;
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
                originalPosition = i;
                notifySubscribers();
                return true;
            }
        }
        return false;
    }

    public void translateLine(float x, float y){
        selectedLine.Start.x += x;
        selectedLine.Start.y += y;
        selectedLine.End.x += x;
        selectedLine.End.y += y;
        notifySubscribers();
    }

    public void startMove(float x, float y){
        System.out.println("start");
        selectedLine.Start.x = x;
        selectedLine.Start.y = y;

        //snap to code
        for(int i=0;i<lines.size();i++){
            if(x <= lines.get(i).Start.x + 20 && x>= lines.get(i).Start.x -20 &&
                    y <= lines.get(i).Start.y +20 & y >= lines.get(i).Start.y -20){
                selectedLine.Start.x = lines.get(i).Start.x;
                selectedLine.Start.y = lines.get(i).Start.y;
            }
            if(x <= lines.get(i).End.x + 20 && x>= lines.get(i).End.x -20 &&
                    y <= lines.get(i).End.y +20 & y >= lines.get(i).End.y -20){
                selectedLine.Start.x = lines.get(i).End.x;
                selectedLine.Start.y = lines.get(i).End.y;
            }
        }
        notifySubscribers();
    };

    public void endMove(float x, float y){
        System.out.println("end");
        selectedLine.End.x = x;
        selectedLine.End.y = y;

        //snap to code
        for(int i=0;i<lines.size();i++){
            if(x <= lines.get(i).Start.x + 20 && x>= lines.get(i).Start.x -20 &&
            y <= lines.get(i).Start.y +20 & y >= lines.get(i).Start.y -20){
                selectedLine.End.x = lines.get(i).Start.x;
                selectedLine.End.y = lines.get(i).Start.y;
            }
            if(x <= lines.get(i).End.x + 20 && x>= lines.get(i).End.x -20 &&
                    y <= lines.get(i).End.y +20 & y >= lines.get(i).End.y -20){
                selectedLine.End.x = lines.get(i).End.x;
                selectedLine.End.y = lines.get(i).End.y;
            }
        }
        notifySubscribers();
    };

    public void updateLines() {
        lines.set(originalPosition,new Line(selectedLine.Start,selectedLine.End));
        selectedLine = lines.get(originalPosition);
        notifySubscribers();
    }

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
