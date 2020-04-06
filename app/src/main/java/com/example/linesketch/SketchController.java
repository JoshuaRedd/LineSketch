package com.example.linesketch;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

public class SketchController implements View.OnTouchListener {

    SketchModel model;
    InteractionModel iModel;
    Line Line;

    public SketchController(){

    }

    private enum State {READY , SELECTING, DRAGGING, ENDPOINTMOVE}

    private State currentState = State.READY;

    public void setModel(SketchModel sModel) {
        model = sModel;
    }

    public void setIModel(InteractionModel im) {
        iModel = im;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (currentState) {
            case READY:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iModel.addStart(new PointF(motionEvent.getX(),motionEvent.getY()));

                        if(model.lineSelection(model.lines,new PointF(motionEvent.getX(),motionEvent.getY()))){
                            currentState = State.SELECTING;
                        }
                }


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //add dots to view
                        model.addDot(motionEvent.getX(),motionEvent.getY());
                }

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //store coordinates of last point
                        iModel.addLast(new PointF(motionEvent.getX(),motionEvent.getY()));
                        //if points create valid line draw the line
                        if((new Line(iModel.start,iModel.end).isValidLine(model.points))){
                            model.addLine(iModel.start,iModel.end);
                        }
                        //else just clear points
                        model.points.clear();
                }

            case SELECTING:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (model.selectedLine != null) {

                            //if the touchdown is on the selected line start handle - switch to the ENDPOINTMOVE state
                            if (model.selectedLine.Start.x >= motionEvent.getX() + 10 && model.selectedLine.Start.x >= motionEvent.getX() - 10 &&
                                    model.selectedLine.Start.y >= motionEvent.getX() + 10 && model.selectedLine.Start.y >= motionEvent.getX() - 10) {
                                currentState = State.ENDPOINTMOVE;
                            }

                            //if the touchdown is on the selected line end handle - switch to the ENDPOINTMOVE state
                            else if (model.selectedLine.End.x >= motionEvent.getX() + 10 && model.selectedLine.End.x >= motionEvent.getX() - 10 &&
                                    model.selectedLine.End.y >= motionEvent.getX() + 10 && model.selectedLine.End.y >= motionEvent.getX() - 10) {
                                currentState = State.ENDPOINTMOVE;
                            }

                            //if the touchdown is on the selected line - switch to the DRAGGING state
                            else if (model.selectedLine.distanceFromLine(motionEvent.getX(), motionEvent.getY()) > 0.4) {
                                currentState = State.DRAGGING;
                            }

                            //we can assume the touch is on the background p switch to the READY state
                            else {
                                currentState = State.READY;
                            }
                        }
                }

            case DRAGGING:

            case ENDPOINTMOVE:

        }
    return true;
    }


    public void rotateBar(int position){
        System.out.println(position);
    };

    public void scaleBar(int position){
        System.out.println(position);
    };
}
