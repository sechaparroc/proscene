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

public class BasicIK3D extends PApplet {
    Scene scene;
    PFont myFont;
    ArrayList<GenericFrame>  joints = new ArrayList<GenericFrame>();
    ArrayList<GenericFrame>  jointsConstrained = new ArrayList<GenericFrame>();
    InteractiveFrame target;

    int num_joints = 12;
    float constraint_factor = 50;

    ChainSolver solverUnconstrained;
    ChainSolver solverConstrained;
    boolean auto = true;

    int TimesPerFrame = 1;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        scene = new Scene(this);
        scene.setCameraType(Camera.Type.ORTHOGRAPHIC);
        scene.setAxesVisualHint(true);
        Vec v = new Vec(10,10,10);
        InteractiveFrame prev = null;
        //Unconstrained Chain
        for(int i = 0; i < num_joints; i++){
            InteractiveFrame j;
            j = new InteractiveFrame(scene);
            if(prev != null)   j.setReferenceFrame(prev);
            j.setTranslation(v.get());
            joints.add(j);
            prev = j;
        }
        //Fix hierarchy
        joints.get(0).setupHierarchy();
        prev = null;
        //Constrained Chain
        for(int i = 0; i < num_joints; i++){
            InteractiveFrame j;
            j = new InteractiveFrame(scene);
            if(prev != null)   j.setReferenceFrame(prev);
            j.setTranslation(v.get());
            jointsConstrained.add(j);
            prev = j;
        }
        //Fix hierarchy
        jointsConstrained.get(0).setupHierarchy();
        //Add constraints
        for(int i = 0; i < jointsConstrained.size(); i++){
            BallAndSocket constraint = new BallAndSocket(radians(constraint_factor),
                    radians(constraint_factor),radians(constraint_factor),radians(constraint_factor));
            constraint.setRestRotation((Quat)jointsConstrained.get(i).rotation().get());
            jointsConstrained.get(i).setConstraint(constraint);
        }
        target = new InteractiveFrame(scene);
        target.translate(new Vec(50, 50*noise(0)));

        solverConstrained = new ChainSolver(jointsConstrained, target);
        solverConstrained.setTIMESPERFRAME(TimesPerFrame);
        solverConstrained.setMINCHANGE(999);
        solverUnconstrained = new ChainSolver(joints, target);
        solverUnconstrained.setTIMESPERFRAME(TimesPerFrame);
        solverUnconstrained.setMINCHANGE(999);
    }


    public void draw() {
        background(0);
        for(GenericFrame j : joints){
            pushMatrix();
            pushStyle();
            j.applyWorldTransformation();
            scene.drawAxes(3);
            fill(0,255,0);
            strokeWeight(5);
            stroke(0,100,100,100);
            if(j.referenceFrame() != null){
                Vec v = j.coordinatesOfFrom(new Vec(), j.referenceFrame());
                line(0,0,0, v.x(), v.y(), v.z());
            }
            popStyle();
            popMatrix();
        }
        for(GenericFrame j : jointsConstrained){
            pushMatrix();
            pushStyle();
            j.applyWorldTransformation();
            scene.drawAxes(3);
            fill(0,255,0);
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
        translate(target.position().x(),target.position().y(),target.position().z());
        sphere(5);
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
            ChainSolver solver = new ChainSolver(joints, target);
            solver.setTIMESPERFRAME(1);
            solver.solve();
        }

        if(key == 'd'){
            //create solver
            ChainSolver solver = new ChainSolver(jointsConstrained, target);
            solver.setTIMESPERFRAME(1);
            solver.solve();
        }

        if(key == 'j'){
            backward = null;
            enableBack = false;
            //create solver
            solver = new ChainSolver(jointsConstrained, target);
            solver.setTIMESPERFRAME(1);
            GenericFrame root = jointsConstrained.get(0);
            GenericFrame end   = jointsConstrained.get(jointsConstrained.size()-1);
            Vec target = solver.getTarget().position().get();
            //Get the distance between the Root and the Target
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
            solver.getPositions().put(jointsConstrained.get(0).id(), initial);
            solver.executeBackwardReaching(solver.getChain());
            backward = solver.getPositions();
            solver.update();
            enableBack = false;
        }
        if(key == 'z'){
            auto = !auto;
        }
        if(key == 'n'){
            TimesPerFrame++;
            solverConstrained.setTIMESPERFRAME(TimesPerFrame);
            solverUnconstrained.setTIMESPERFRAME(TimesPerFrame);
            println("Times Per FRAME : " + TimesPerFrame);
        }

    }

    //DEBUG METHODS

    public void drawChain(HashMap<Integer, Vec> positions, int c){
        PShape p = createShape(SPHERE,12);
        p.setStroke(false);
        int tr = 30;
        for(Vec v : positions.values()){
            p.setFill(color(red(c),green(c),blue(c), tr));
            pushMatrix();
            translate(v.x(),v.y(),v.z());
            shape(p);
            popMatrix();
            tr +=20;
        }
    }
    //*/

    public static void main(String args[]) {
        PApplet.main(new String[]{"ik.BasicIK3D"});
    }
}
