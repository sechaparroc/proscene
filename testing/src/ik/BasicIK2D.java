package ik;

import processing.core.*;
import remixlab.dandelion.constraint.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;
import remixlab.proscene.*;
import remixlab.dandelion.ik.Solver.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicIK2D extends PApplet {
    Scene scene;
    PFont myFont;
    ArrayList<GenericFrame> unconstrainedFrames = new ArrayList<GenericFrame>();
    ArrayList<GenericFrame> constrainedFrames = new ArrayList<GenericFrame>();
    InteractiveFrame target;

    int num_joints = 10;
    //float constraint_factor = 50;
    float lenght = 20;
    float max = 0;
    float min = 10;

    ChainSolver solverUnconstrained;
    ChainSolver solverConstrained;
    boolean auto = true;

    public void settings() {
        size(800, 800, P2D);
    }

    public void setup() {
        scene = new Scene(this);
        scene.setRadius(250);
        scene.showAll();
        scene.setAxesVisualHint(true);
        Vec v = new Vec(lenght,lenght);
        if(scene.is3D()) v = new Vec(lenght,lenght,lenght);
        InteractiveFrame prev = null;
        //Unconstrained Chain
        for(int i = 0; i < num_joints; i++){
            InteractiveFrame j;
            j = new InteractiveFrame(scene);
            if(prev != null){   j.setReferenceFrame(prev);
                j.setTranslation(v.get());}
            unconstrainedFrames.add(j);
            prev = j;
        }
        //Fix hierarchy
        unconstrainedFrames.get(0).setupHierarchy();
        prev = null;
        //Constrained Chain
        for(int i = 0; i < num_joints; i++){
            InteractiveFrame j;
            j = new InteractiveFrame(scene);
            if(prev != null){   j.setReferenceFrame(prev);
                j.setTranslation(v.get());}
            constrainedFrames.add(j);
            prev = j;
        }
        //Fix hierarchy
        constrainedFrames.get(0).setupHierarchy();
        //Add constraints

        for(int i = 0; i < constrainedFrames.size(); i++){
            Hinge hinge = new Hinge();
            hinge.setRestRotation(constrainedFrames.get(i).rotation());
            hinge.setMax(radians(max));
            hinge.setMin(radians(min));
            hinge.setAxis(constrainedFrames.get(i).transformOf(new Vec(1,-1,0)));
            constrainedFrames.get(i).setConstraint(hinge);
        }


        target = new InteractiveFrame(scene);
        target.translate(new Vec(50, 50*noise(0)));

        solverConstrained = new ChainSolver(constrainedFrames, target);
        solverConstrained.setTIMESPERFRAME(1);
        solverUnconstrained = new ChainSolver(unconstrainedFrames, target);
        solverUnconstrained.setTIMESPERFRAME(1);
    }

    public void draw() {
        background(0);
        for(GenericFrame j : unconstrainedFrames){
            pushMatrix();
            pushStyle();
            j.applyWorldTransformation();
            scene.drawAxes(3);
            fill(0,255,0,50);
            strokeWeight(5);
            stroke(0,100,100,100);
            if(j.referenceFrame() != null){
                Vec v = j.coordinatesOfFrom(new Vec(), j.referenceFrame());
                line(0,0,0, v.x(), v.y(), v.z());
            }
            popStyle();
            popMatrix();
        }
        for(GenericFrame j : constrainedFrames){
            pushMatrix();
            pushStyle();
            j.applyWorldTransformation();
            scene.drawAxes(3);
            fill(0,255,0,50);
            strokeWeight(5);
            stroke(100,0,100,100);
            if(j.referenceFrame() != null){
                Vec v = j.coordinatesOfFrom(new Vec(), j.referenceFrame());
                line(0,0,0, v.x(), v.y(), v.z());
            }
            popStyle();
            popMatrix();
        }

        pushMatrix();
        pushStyle();
        noStroke();
        fill(255,0,0,200);
        if(scene.is3D()) {
            translate(target.position().x(),target.position().y(),target.position().z());
            sphere(5);
        }
        else{
            translate(target.position().x(),target.position().y());
            ellipse(0,0,5,5);
        }
        popStyle();
        popMatrix();
        if(auto){
            solverConstrained.solve();
            solverUnconstrained.solve();
        }

        //if(forward != null)drawChain(forward, color(0,255,0,30));
        //if(backward != null)drawChain(backward, color(0,0,255,30));
    }

    /*
    float counter = 0;
    boolean enableBack = false;
    Vec initial = null;
    ChainSolver solver = null;
    HashMap<Integer, Vec> forward = null;
    HashMap<Integer, Vec> backward = null;

    boolean inv = false;

    public void keyPressed(){
        if(key == 'v'){
            counter+=3;
            float val = inv ? -1 : 1;
            target.translate(3*val, 3*noise(counter));
            if(target.position().x() > 130) inv = true;
            if(target.position().x() < -130) inv = false;
        }
        if(key == 'c'){
            //create solver
            ChainSolver solver = new ChainSolver(unconstrainedFrames, target);
            solver.setTIMESPERFRAME(1);
            solver.solve();
        }
        if(key == 'd'){
            //create solver
            ChainSolver solver = new ChainSolver(constrainedFrames, target);
            solver.setTIMESPERFRAME(1);
            solver.solve();
        }
        if(key == 'j'){
            backward = null;
            enableBack = false;
            //create solver
            solver = new ChainSolver(constrainedFrames, target);
            solver.setTIMESPERFRAME(1);
            float length = solver.getLength();
            GenericFrame root = constrainedFrames.get(0);
            GenericFrame end   = constrainedFrames.get(constrainedFrames.size()-1);
            Vec target = solver.getTarget().position().get();
            //Get the distance between the Root and the Target
            float dist = Vec.distance(root.position(), target);
            initial = solver.getPositions().get(root.id()).get();
            if(Vec.distance(end.position(), target) <= solver.getERROR()) return;
            enableBack = true;
            solver.getPositions().put(end.id(), target.get());
            //Stage 1: Forward Reaching
            forward = new HashMap<Integer, Vec>();
            solver.executeForwardReaching(solver.getChain());
            for(Map.Entry<Integer, Vec> entry : solver.getPositions().entrySet()){
                forward.put(entry.getKey(), entry.getValue());
            }
        }
        if(key == 'k'){
            if(!enableBack) return;
            solver.getPositions().put(constrainedFrames.get(0).id(), initial);
            solver.executeBackwardReaching(solver.getChain());
            backward = solver.getPositions();
            solver.update();
            enableBack = false;
        }
        if(key == 'z'){
            auto = !auto;
        }
    }

    //DEBUG METHODS
    public void drawChain(HashMap<Integer, Vec> positions, int c){
        PShape p;
        if(scene.is3D()) p = createShape(SPHERE,5);
        else p = createShape(ELLIPSE,0,0,5,5);
        p.setStroke(false);
        int tr = 30;
        for(Vec v : positions.values()){
            p.setFill(color(red(c),green(c),blue(c), tr));
            pushMatrix();
            if(scene.is3D()){
                translate(v.x(),v.y(),v.z());
                shape(p);
            }else{
                translate(v.x(),v.y());
                shape(p);
            }
            popMatrix();
            tr +=20;
        }
    }
    //*/

    public static void main(String args[]) {
        PApplet.main(new String[]{"ik.BasicIK2D"});
    }
}
