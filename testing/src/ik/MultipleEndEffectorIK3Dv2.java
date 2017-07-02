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
 * Created by sebchaparr on 27/06/17.
 */
public class MultipleEndEffectorIK3Dv2 extends PApplet{

    Scene scene;
    PFont myFont;
    float distanceBtwnSliblings = 30.f;
    int numSliblings = 6;
    int numJoints = 8;

    float constraint_factor = 50;
    float boneLength = 20;

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
            Vec vec = new Vec(0,boneLength, 0);
            Quat q = new Quat(new Vec(0,0,1), step*j - PI);
            vec = q.multiply(vec);
            InteractiveFrame child = new InteractiveFrame(scene);
            child.setReferenceFrame(root);
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
        idx = leaves.size()/2;
        leaves = createBranch(leaves.get(idx),  numSliblings, boneLength);
        //Fix hierarchy
        //root.setupHierarchy();
        Solver solver = scene.setIKStructure(root);

        for(int i = 0; i < leaves.size(); i++){
            targets.get(i).setPosition(leaves.get(i).position());
            targets.get(i).setOrientation(leaves.get(i).orientation());
            scene.addIKTarget(leaves.get(i), targets.get(i));
        }
        solver.setTIMESPERFRAME(TimesPerFrame);
        solver.setMINCHANGE(0.001f);
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
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"ik.MultipleEndEffectorIK3Dv2"});
    }
}
