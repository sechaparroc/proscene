package ik;

import processing.core.*;
import remixlab.dandelion.constraint.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;
import remixlab.dandelion.ik.Solver;
import remixlab.proscene.*;
import remixlab.dandelion.ik.Solver.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/*
* Two different chains (With different kind of constraints) are pursuing the same Target
* */
public class BasicIK2D extends PApplet {
    Scene scene;
    InteractiveFrame target;

    int num_joints = 10;

    float boneLength = 20;

    //Hinge Constraint
    float max = 20;
    float min = 20;

    public void settings() {
        size(800, 800, P2D);
    }

    public void setup() {
        scene = new Scene(this);
        scene.setRadius(250);
        scene.showAll();
        scene.setAxesVisualHint(true);
        target = new InteractiveFrame(scene, "targetGraphics");
        Vec targetPosition = new Vec(1,1);
        targetPosition.normalize();
        targetPosition.multiply(boneLength*num_joints*1.1f);
        target.translate(targetPosition);

        //Three identical chains that will have different constraints
        ArrayList<GenericFrame> unconstrainedChain = generateChain(num_joints, boneLength, new Vec(-10,10));
        ArrayList<GenericFrame> constrainedChain = generateChain(num_joints, boneLength, new Vec());

        //Apply constraints
        for(int i = 0; i < constrainedChain.size()-1; i++){
            Hinge hinge = new Hinge();
            hinge.setRestRotation(constrainedChain.get(i).rotation());
            hinge.setMax(radians(max));
            hinge.setMin(radians(min));
            hinge.setAxis(constrainedChain.get(i).transformOf(new Vec(1,-1,0)));
            constrainedChain.get(i).setConstraint(hinge);
        }

        //Register Solver
        Solver solverConstrained = new ChainSolver(constrainedChain, target);
        solverConstrained.setTIMESPERFRAME(1);
        scene.executeIKSolver(solverConstrained);
        Solver solverUnconstrained = new ChainSolver(unconstrainedChain, target);
        solverUnconstrained.setTIMESPERFRAME(1);
        scene.executeIKSolver(solverUnconstrained);
    }

    public ArrayList<GenericFrame> generateChain(int num_joints, float boneLength, Vec translation){
        InteractiveFrame prevFrame = null;
        InteractiveFrame chainRoot = null;
        for (int i = 0; i < num_joints; i++) {
            InteractiveFrame iFrame;
            iFrame = new InteractiveFrame(scene, "frameGraphics");
            if (i == 0)
                chainRoot = iFrame;
            if (prevFrame != null) iFrame.setReferenceFrame(prevFrame);
            Vec translate = new Vec(1, 1);
            translate.normalize();
            translate.multiply(boneLength);
            iFrame.setTranslation(translate);
            iFrame.setPickingPrecision(InteractiveFrame.PickingPrecision.FIXED);
            prevFrame = iFrame;
        }
        //Consider Standard Form: Parent Z Axis is Pointing at its Child
        chainRoot.translate(translation);
        chainRoot.setupHierarchy();
        return scene.branch(chainRoot, false);
    }

    public void frameGraphics(InteractiveFrame iFrame, PGraphics pg) {
        pg.pushStyle();
        scene.drawAxes(pg, 3);
        pg.fill(0, 255, 0);
        pg.strokeWeight(5);
        pg.stroke(0, 100, 100, 100);
        if (iFrame.referenceFrame() != null) {
            Vec v = iFrame.coordinatesOfFrom(new Vec(), iFrame.referenceFrame());
            pg.line(0, 0, v.x(), v.y());
        }
        pg.popStyle();
    }

    public void targetGraphics(PGraphics pg) {
        pg.pushStyle();
        pg.noStroke();
        pg.fill(255, 0, 0, 200);
        pg.ellipse(0, 0, 5, 5);
        pg.popStyle();
    }

    public void draw() {
        background(0);
        //Draw Constraints
        for (InteractiveFrame j : scene.frames()) {
            j.draw();
            pushMatrix();
            pushStyle();
            Frame frame = new Frame(j.position(), Rot.compose(j.orientation(), j.rotation().inverse()));
            if(j.constraint() instanceof Hinge){
                frame.rotate(((Hinge)j.constraint()).getRestRotation());
                scene.applyWorldTransformation(frame);
                drawLimits(boneLength, max, min);
            }
            popStyle();
            popMatrix();
        }
    }

    public void drawLimits(float length, float max, float min) {
        pushStyle();
        stroke(246,117,19,80);
        fill(246,117,19,80);
        strokeWeight(1);
        arc(0, 0, 2*length, 2*length, -radians(min), radians(max));
        popStyle();
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"ik.BasicIK2D"});
    }
}
