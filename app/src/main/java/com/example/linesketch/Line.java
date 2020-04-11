package com.example.linesketch;

import android.graphics.PointF;

import java.util.ArrayList;


public class Line {
    PointF Start;
    PointF End;
    float length;
    float ratioA;
    float ratioB;
    float ratioC;

    public float dist(float xstart,float ystart,float xend,float yend){
        length = (float) Math.pow(Math.abs(xstart-xend),2) + (float) Math.pow(Math.abs(ystart-yend),2);
        return length;
    }

    public Line(PointF start,PointF end){
        Start = start;
        End = end;
        length = dist(start.x, start.y, end.x, end.y);
        ratioA = (start.y - end.y) / length;
        ratioB = (end.x - start.x) / length;
        ratioC = -1 * ((start.y - end.y) * start.x + (end.x - start.x) * start.y) / length;
    }

    float distanceFromLine(float x, float y) {
        return ratioA * x + ratioB * y + ratioC;
    }

    public boolean isValidLine(ArrayList<PointF> points) {
        for(int i=0;i<points.size();i++) {
            if (Math.abs(distanceFromLine(points.get(i).x, points.get(i).y)) > 0.2){
                return false;
            }
        }
        return true;
    }

    public void translocateLine(int x, int y){

    };
}
