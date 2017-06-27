package ik;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PShape;
import remixlab.dandelion.constraint.BallAndSocket;
import remixlab.dandelion.core.Camera;
import remixlab.dandelion.core.GenericFrame;
import remixlab.dandelion.geom.Quat;
import remixlab.dandelion.geom.Vec;
import remixlab.dandelion.ik.Solver;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.Scene;

import java.util.ArrayList;

/**
 * Created by sebchaparr on 25/06/17.
 */
public class MultipleEndEffectorIK3D extends PApplet {
    Scene scene;
    PFont myFont;
    float distanceBtwnSliblings = 30.f;
    int numSliblings = 6;
    int numJoints = 8;

    float constraint_factor = 50;
    float boneLength = 20;

    Solver.TreeSolver solverUnconstrained;
    boolean auto = false;
    boolean showSteps = true;

    int TimesPerFrame = 40;
    InteractiveFrame root;
    ArrayList<InteractiveFrame> targets = new ArrayList<InteractiveFrame>();//one target per Leaf


    public void settings() {
        size(500, 500, P3D);
    }

    public ArrayList<InteractiveFrame> createBranch(GenericFrame root, int numSliblings, float boneLength){
        ArrayList<InteractiveFrame> frames = new ArrayList<InteractiveFrame>();
        //Generate children
        float step = PI/numSliblings;
        //float offset = PI;
        for(int j = 0; j < numSliblings; j++){
            InteractiveFrame dummy = new InteractiveFrame(scene);
            dummy.setReferenceFrame(root);
            Vec vec = new Vec(0,0, boneLength);
            Quat q = new Quat(new Vec(1,0,0), step*j - PI);
            vec = q.multiply(vec);
            InteractiveFrame child = new InteractiveFrame(scene);
            child.setReferenceFrame(dummy);
            child.translate(vec);
            frames.add(child);
        }
        return frames;
    }

    public void setup() {
        scene = new Scene(this);
        scene.setCameraType(Camera.Type.ORTHOGRAPHIC);
        scene.setAxesVisualHint(true);

        for(int i = 0; i < numSliblings; i++)
            targets.add(new InteractiveFrame(scene));

        root = new InteractiveFrame(scene);
        ArrayList<InteractiveFrame> leaves;
        leaves = createBranch(root,  numSliblings, boneLength);
        int idx = leaves.size()/2;
        leaves = createBranch(leaves.get(idx),  numSliblings, boneLength);
        idx = leaves.size()/2;
        leaves = createBranch(leaves.get(idx),  numSliblings, boneLength);

        //Fix hierarchy
        root.setupHierarchy();
        solverUnconstrained = new Solver.TreeSolver(root);

        for(int i = 0; i < leaves.size(); i++){
            targets.get(i).setPosition(leaves.get(i).position());
            targets.get(i).setOrientation(leaves.get(i).orientation());
            boolean cond = solverUnconstrained.addTarget(leaves.get(i), targets.get(i));
            System.out.println("Idx : " + leaves.get(i).id());
            if(cond) System.out.println("Target added");
        }
        solverUnconstrained.setTIMESPERFRAME(TimesPerFrame);
        solverUnconstrained.setMINCHANGE(0.001f);
    }


    public void draw() {
        background(0);
        for(InteractiveFrame j : scene.branch(root)){
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

        for(GenericFrame target : targets) {
            pushMatrix();
            pushStyle();
            noStroke();
            fill(255, 0, 0, 200);
            translate(target.position().x(), target.position().y(), target.position().z());
            sphere(5);
            popStyle();
            popMatrix();
        }

        if(auto){
            solverUnconstrained.solve();
        }
    }

    public void keyPressed(){
        if(key == 'z'){
            auto = !auto;
        }
    }
    /*
    float counter = 0;
    boolean enableBack = false;
    Vec initial = null;
    Solver.ChainSolver solver = null;
    ArrayList<Vec> forward = null;
    ArrayList<Vec> backward = null;
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
            Solver.ChainSolver solver = new Solver.ChainSolver("unconstrained", joints, target);
            solver.setTIMESPERFRAME(1);
            solver.solve();
        }

        if(key == 'd'){
            //create solver
            Solver.ChainSolver solver = new Solver.ChainSolver("Constrained", jointsConstrained, target);
            solver.setTIMESPERFRAME(1);
            solver.solve();
            printChange();
        }

        if(key == 'j'){
            backward = null;
            enableBack = false;
            //create solver
            solver = new Solver.ChainSolver("Constrained",jointsConstrained, target);
            solver.setTIMESPERFRAME(1);
            GenericFrame root = jointsConstrained.get(0);
            GenericFrame end   = jointsConstrained.get(jointsConstrained.size()-1);
            Vec target = solver.getTarget().position().get();
            //Get the distance between the Root and the Target
            initial = solver.getPositions().get(root.id()).get();
            if(Vec.distance(end.position(), target) <= solver.getERROR()) return;
            enableBack = true;
            solver.getPositions().set(jointsConstrained.size()-1, target.get());
            //Stage 1: Forward Reaching
            forward = new ArrayList<Vec>();
            solver.executeForwardReaching(solver.getChain());
            for(Vec v : solver.getPositions()){
                forward.add(v);
            }
        }
        if(key == 'k'){
            if(!enableBack) return;
            solver.getPositions().set(0, initial);
            solver.executeBackwardReaching(solver.getChain());
            backward = solver.getPositions();
            solver.update();
            enableBack = false;
        }
        if(key == 'z'){
            auto = !auto;
        }
        if(key == 'x'){
            showSteps = !showSteps;
        }

        if(key == 'n'){
            TimesPerFrame++;
            solverConstrained.setTIMESPERFRAME(TimesPerFrame);
            solverUnconstrained.setTIMESPERFRAME(TimesPerFrame);
            println("Times Per FRAME : " + TimesPerFrame);
        }

    }

    //DEBUG METHODS

    public void drawChain(ArrayList<Vec> positions, int c){
        PShape p = createShape(SPHERE,5);
        p.setStroke(false);
        int tr = 30;
        for(Vec v : positions){
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
        PApplet.main(new String[]{"ik.MultipleEndEffectorIK3D"});
    }
}
