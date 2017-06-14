/**************************************************************************************
 * dandelion_tree
 * Copyright (c) 2014-2017 National University of Colombia, https://github.com/remixlab
 * @author Sebastian Chaparro, https://github.com/sechaparroc
 * @author Jean Pierre Charalambos, http://otrolado.info/
 *
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 **************************************************************************************/

package remixlab.dandelion.ik;

import remixlab.dandelion.constraint.BallAndSocket;
import remixlab.dandelion.constraint.Hinge;
import remixlab.dandelion.core.GenericFrame;
import remixlab.dandelion.geom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A Solver is a convenient class to solve IK problem
 * Given a Chain or a Tree Structure of Frames, this class will
 * solve the configuration that the frames must have to reach
 * a desired position
 */

/*
* TODO: Consider Target with Orientation constraint
* TODO: Add an auxiliar chain to avoid update in the real one
* TODO: Add solver in scene
* TODO: Specify a Constraint of Type Free
* TODO: Copy and assign properly Ref Frame
* */

public  abstract class Solver {
    /*Convenient String to register/unregister solvers in an Abstract Scene*/
    protected String name;
    protected float ERROR = 0.1f;
    protected int MAXITER = 200;
    protected float MINCHANGE = 0.01f;
    protected float TIMESPERFRAME = 1.f;
    protected float FRAMECOUNTER = 0;
    protected int iterations = 0;

    /*Store Joint's desired position*/
    protected HashMap<Integer,Vec> positions = new HashMap<Integer,Vec>();

    public HashMap<Integer,Vec> getPositions(){ return positions;}

    public void restartIterations(){
        iterations = 0;
    }

    public float getERROR() {
        return ERROR;
    }

    public void setERROR(float ERROR) {
        this.ERROR = ERROR;
    }

    public int getMAXITER() {
        return MAXITER;
    }

    public void setMAXITER(int MAXITER) {
        this.MAXITER = MAXITER;
    }

    public float getMINCHANGE() {
        return MINCHANGE;
    }

    public void setMINCHANGE(float MINCHANGE) {
        this.MINCHANGE = MINCHANGE;
    }

    public float getTIMESPERFRAME() {
        return TIMESPERFRAME;
    }

    public void setTIMESPERFRAME(float TIMESPERFRAME) {
        this.TIMESPERFRAME = TIMESPERFRAME;
    }

    public float getFRAMECOUNTER() {
        return FRAMECOUNTER;
    }

    public void setFRAMECOUNTER(float FRAMECOUNTER) {
        this.FRAMECOUNTER = FRAMECOUNTER;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /*
    * Performs First Stage of FABRIK Algorithm, receives a chan of Frames, being the Frame at i
    * the reference frame of the Frame at i + 1
    * */

    public void executeForwardReaching(ArrayList<GenericFrame> chain, Frame target, int endEffectors){
        for(int i = chain.size()-2; i >= 0; i--){
            Vec pos_i = positions.get(chain.get(i).id());
            Vec pos_i1 = positions.get(chain.get(i+1).id());
            /*Check constrictions*/
            Vec pos_i1_constrained = applyConstraints(chain.get(i+1), chain.get(i+1).referenceFrame(), pos_i, pos_i1);
            Vec diff = Vec.subtract(pos_i1, pos_i1_constrained);
            pos_i.add(diff);
            positions.put(chain.get(i).id(), pos_i);
            float r_i = Vec.distance(pos_i, pos_i1);
            float dist_i = chain.get(i+1).translation().magnitude()/chain.get(i+1).magnitude();
            if(dist_i == 0){
                positions.put(chain.get(i).id(), pos_i1.get());
                continue;
            }
            float lambda_i =  dist_i/r_i;
            Vec new_pos = Vec.multiply(pos_i1, 1.f - lambda_i);
            new_pos.add(Vec.multiply(pos_i, lambda_i));
            if(i <= 1 && endEffectors > 1){
                if(i == 1)
                    target.setPosition(
                            Vec.add(target.position(),
                                    Vec.multiply(new_pos, 1.f/endEffectors)));
            }else{
                positions.put(chain.get(i).id(), new_pos);
            }
        }
    }

    public void executeForwardReaching(ArrayList<GenericFrame> chain){
        executeForwardReaching(chain, null, 0);
    }

    public void executeBackwardReaching(ArrayList<GenericFrame> chain){
        for(int i = 0; i < chain.size()-1; i++){
            Vec pos_i = positions.get(chain.get(i).id());
            Vec pos_i1 = positions.get(chain.get(i+1).id());

            /*Check constraints*/
            pos_i1 = applyConstraints(chain.get(i+1), chain.get(i+1).referenceFrame(), pos_i, pos_i1);
            positions.put(chain.get(i+1).id(), pos_i1);
            //Get the distance between Joint i and the Target
            float r_i = Vec.distance(pos_i, pos_i1);
            float dist_i = chain.get(i+1).translation().magnitude()/chain.get(i+1).magnitude();
            if(dist_i == 0){
                positions.put(chain.get(i+1).id(), pos_i.get());
                continue;
            }
            float lambda_i =  dist_i/r_i;
            Vec new_pos = Vec.multiply(pos_i, 1.f - lambda_i);
            new_pos.add(Vec.multiply(pos_i1, lambda_i));
            positions.put(chain.get(i+1).id(), new_pos);

            Rotation delta = null;
            if(chain.get(i+1).is3D())
                delta = posToQuat(chain.get(i+1), chain.get(i+1).referenceFrame(), chain.get(i+1).referenceFrame().position(), positions.get(chain.get(i+1).id()));
            else
                delta = posToRot(chain.get(i+1), chain.get(i+1).referenceFrame(), chain.get(i+1).referenceFrame().position(), positions.get(chain.get(i+1).id()));

            chain.get(i+1).referenceFrame().rotate(delta);
            positions.put(chain.get(i+1).id(), chain.get(i+1).position().get());
        }
    }
    /*Set Joint's Parent orientation assuming that
     * new Joint position is v*/
    public Rot posToRot(Frame j, Frame parent, Vec o, Vec p){
        Rot rot = new Rot();
        Vec diff = Vec.subtract(p, o);
        diff.add(parent.position());
        return new Rot(j.translation(), parent.coordinatesOf(diff));
    }

    public Quat posToQuat(Frame j, Frame parent, Vec o, Vec p){
        Vec diff = Vec.subtract(p, o);
        diff.add(parent.position());
        return new Quat(j.translation(), parent.coordinatesOf(diff));
    }

    /*
    * Check the type of the constraint related to the Frame Parent,
    * Frame j is the frame used to verify if the orientation of Parent is appropriate,
    * Vec o is a Vector
    *
    * */

    public Vec applyConstraints(Frame j, Frame parent, Vec o, Vec p){
        if(parent.constraint() instanceof BallAndSocket){
            BallAndSocket constraint = (BallAndSocket) parent.constraint();
            Quat desired = (Quat) Quat.compose(parent.rotation(), posToQuat(j, parent, o, p));
            Vec target = Quat.multiply(desired, j.translation());
            target = constraint.getConstraint(target);
            target.normalize();
            target.multiply(Vec.subtract(p,o).magnitude());
            return parent.inverseCoordinatesOf(Quat.multiply((Quat)parent.rotation().inverse(), target));
        } else if(parent.constraint() instanceof Hinge){
            if(parent.is2D()){
                Hinge constraint = (Hinge) parent.constraint();
                Rot desired = posToRot(j,parent, o, p);
                Rot constrained = (Rot) constraint.constrainRotation(desired, parent);
                Vec target = constrained.rotate(j.translation());
                target.normalize();
                target.multiply(Vec.subtract(p,o).magnitude());
            }
        }
        return p;
    }

    /*Performs a FABRIK ITERATION*/
    public abstract boolean execute();
    public abstract void update();
    public abstract boolean stateChanged();
    public abstract void reset();

    public boolean solve(){
        //Reset counter
        if(stateChanged()){
            reset();
        }

        if(iterations == MAXITER) return true;
        FRAMECOUNTER += TIMESPERFRAME;
        while(Math.floor(FRAMECOUNTER) > 0){
            //Returns a boolean that indicates if a termination condition has been accomplished
            if(execute()){
                iterations = MAXITER;
                break;
            }
            else iterations+=1;
            FRAMECOUNTER -= 1;
        }
        //update positions
        update();
        return false;
    }

    public float change(ArrayList<GenericFrame> chain){
        float change = 0.f;
        for(GenericFrame joint : chain){
            change += Vec.distance(joint.position(), positions.get(joint.id()));
        }
        return change;
    }

    public static class ChainSolver extends Solver{
        protected ArrayList<GenericFrame> chain;
        private ArrayList<GenericFrame> bestSolution;
        private ArrayList<GenericFrame> chainCopied;
        private GenericFrame head; //Pointer to head of the chain
        private GenericFrame tail; //Pointer to tail of the chain

        protected Frame target;
        private Frame prevTarget;

        public ArrayList<GenericFrame> getChain() {
            return chain;
        }

        private ArrayList<GenericFrame> copyChain(ArrayList<GenericFrame> list){
            ArrayList<GenericFrame> copy = new ArrayList<GenericFrame>();
            GenericFrame reference = list.get(0).referenceFrame();
            for(GenericFrame joint : list){
                GenericFrame newJoint = joint.get();
                newJoint.setReferenceFrame(reference);
                newJoint.setConstraint(newJoint.constraint());
                copy.add(newJoint);
                reference = newJoint;
            }
            return copy;
        }

        public void setChain(ArrayList<GenericFrame> chain) {
            this.chain = chain;
            chainCopied = chain;
            bestSolution = chain;
            //bestSolution = copyChain(chain);
        }

        public Frame getTarget() {
            return target;
        }

        public void setTarget(Frame target) {
            this.target = target;
        }

        public ChainSolver(String name, ArrayList<GenericFrame> chain, Frame target){
            this.name = name;
            setChain(chain);
            positions = new HashMap<Integer, Vec>();
            for(GenericFrame joint : chainCopied){
                positions.put(joint.id(), joint.position().get());
            }
            this.target = target;
            this.prevTarget = target.get();
        }
        /*Get maximum length of a given chain*/
        public float getLength(){
            float dist = 0;
            for(int i = 1; i < chain.size(); i++){
                dist += chain.get(i).translation().magnitude()/chain.get(i).magnitude();
            }
            return dist;
        }

        public void stretchChain(ArrayList<GenericFrame> chain, Vec target){
            for(int i = 0; i < chain.size()-1; i++){
                //Get the distance between Joint i and the Target
                Vec pos_i = positions.get(chain.get(i).id());
                float r_i = Vec.distance(pos_i, target);
                float dist_i = chain.get(i+1).translation().magnitude()/chain.get(i+1).magnitude();
                float lambda_i =  dist_i/r_i;
                Vec new_pos = Vec.multiply(pos_i, 1.f - lambda_i);
                new_pos.add(Vec.multiply(target, lambda_i));
                positions.put(chain.get(i+1).id(), new_pos);
            }
        }


        /*
         * Performs a FABRIK ITERATION
         *
         * */
        public boolean execute(){
            //Get the distance between the Root and the End Effector
            float length = getLength();
            GenericFrame root = chainCopied.get(0);
            GenericFrame end	 = chainCopied.get(chainCopied.size()-1);
            Vec target = this.target.position().get();
            //Get the distance between the Root and the Target
            float dist = Vec.distance(root.position(), target);
            //When Target is unreachable        //Debug methods
                /*if(dist > length){
                stretchChain(chain, target);
                return true;
            }else{*/
            //Initial root position
            Vec initial = positions.get(root.id()).get();
            //Execute Until the distance between the end effector and the target is below a threshold
            if(Vec.distance(end.position(), target) <= ERROR) return true;
            //Stage 1: Forward Reaching
            positions.put(end.id(), target.get());
            executeForwardReaching(chainCopied);
            //Stage 2: Backward Reaching
            positions.put(root.id(), initial);
            executeBackwardReaching(chainCopied);
            //Save best solution
            //if(Vec.distance(target, end.position()) <  Vec.distance(target, bestSolution.get(chainCopied.size()-1).position())) {
            //bestSolution = copyChain(chainCopied);
            //}
            //Check total position change
            if(change() <= MINCHANGE) return true;
            return false;
        }

        public float change(){
            return change(chainCopied);
        }

        public void executeForwardReaching(ArrayList<GenericFrame> chain){
            executeForwardReaching(chain, null,0);
        }

        public void update(){
            for(int i = 0; i < chain.size(); i++){
                chain.get(i).setRotation(bestSolution.get(i).rotation().get());
            }
        }

        public boolean stateChanged(){
            if(prevTarget == null) prevTarget = target.get();
            return !(prevTarget.position().equals(target.position()) && prevTarget.orientation().equals(target.orientation()));
        }

        public void reset(){
            if(prevTarget instanceof GenericFrame){
                ((GenericFrame) prevTarget).scene().pruneBranch((GenericFrame) prevTarget);
            }
            prevTarget = target.get();
            iterations = 0;
        }

    }

    /*
    * TODO: FIX TreeSolver, At this very moment is not working
    * */
    public static class TreeSolver extends Solver{
        /*Keeps track of a tree structure*/
        private GenericFrame root;
        private boolean setup; //flag used to setup the structure
        /*Set of Joints that are end effectors or are parents of at Least one end effector*/
        private HashSet<Integer> endEffector = new HashSet<Integer>();
        private HashSet<Integer> leaves = new HashSet<Integer>();//Make prune
        //Contains Sub-bases
        private HashMap<Integer, Vec> initialPositions = new HashMap<Integer, Vec>();
        private HashMap<Integer, Integer> subBase = new HashMap<Integer, Integer>();
        private HashMap<Integer, Boolean> solved = new HashMap<Integer, Boolean>();
        private HashMap<Integer, Frame> targets = new HashMap<Integer, Frame>();


        private boolean finished = false;
        private float change;
        public TreeSolver(GenericFrame root){
            //Find sub-base joints
            this.root = root;
            getSubBases(root);
        }

        public void getSubBases(){
            initialPositions = new HashMap<Integer, Vec>();
            subBase = new HashMap<Integer, Integer>();
            endEffector = new HashSet<Integer>();
            targets = new HashMap<Integer, Frame>();
            leaves = new HashSet<Integer>();
            getSubBases(root);
        }

        //Execute FABRIK For a Tree Structure (Multiple End Effectors)
        public void getSubBases(GenericFrame root){
            int endEffectors = 0;
            Vec target = targets.get(root.id()) != null ? targets.get(root.id()).position() : root.position();

            if(Vec.distance(target, root.position()) > Float.MIN_VALUE){
                endEffector.add(root.id());
                endEffectors++;
            }
            //First find all single Chains that must be defined
            //A Breadth First Traversing is performed to find sub-base joints
            for(GenericFrame child : root.children()){
                if(!(child instanceof GenericFrame)) continue;
                getSubBases((GenericFrame)child);
                if(endEffector.contains(((GenericFrame)child).id()) || subBase.containsKey(((GenericFrame)child).id())){
                    endEffectors++;
                }
            }
            if(endEffectors > 0){
                endEffector.add(root.id());
                //set initial position
                positions.put(root.id(), root.position());
            }
            if(endEffectors > 1)subBase.put(root.id(), endEffectors);
        }

        public void executeForwardReaching(GenericFrame joint){
            if(!endEffector.contains(joint.id())) return;
            if(isLeaf(joint) || leaves.contains(joint.id())){
                //Get a chain from joint to nearest sub-base
                ArrayList<GenericFrame> chain = getShortestChain(joint);
                printChain(chain);
                int endEffectors = 0;
                if(subBase.containsKey(chain.get(0).id())){
                    endEffectors = subBase.get(joint.id());
                    chain.add(0,(GenericFrame)chain.get(0).referenceFrame());
                }
                printChain(chain);
                //Perform a Forward reaching
                //Stage 1: Forward Reaching
                if(Vec.distance(joint.position(), targets.get(joint.id()).position()) > ERROR){
                    initialPositions.put(chain.get(0).id(), chain.get(0).position().get());
                    positions.put(joint.id(), targets.get(joint.id()).position().get());
                    printPositions(root);
                    executeForwardReaching(chain, targets.get(chain.get(0).id()) ,endEffectors);
                    finished = false;
                }
            }
            if(subBase.containsKey(joint.id()))
                targets.get(joint.id()).setPosition(
                        Vec.multiply(targets.get(joint.id()).position(), 1.f/subBase.get(joint.id())));
            for(GenericFrame child : joint.children()){
                if(!(child instanceof GenericFrame)) continue;
                executeForwardReaching((GenericFrame) child);
            }
        }

        public void executeBackwardReaching(GenericFrame root, GenericFrame end){
            if(!endEffector.contains(end.id())) return;
            if(subBase.containsKey(end.id())
                    || isLeaf(end)
                    || leaves.contains(end.id())){
                //Perform a Backward reaching
                //Stage 2: Backward Reaching
                ArrayList<GenericFrame> chain = new ArrayList<GenericFrame>();
                GenericFrame cur = end;
                while(cur != root){
                    chain.add(0,cur);
                    cur = (GenericFrame) cur.referenceFrame();
                }if(cur == root) chain.add(0,cur);


                if(Vec.distance(end.position(), targets.get(end.id()).position()) > ERROR){
                    positions.put(root.id(), initialPositions.get(root.id()).get());
                    printChain(chain);
                    executeBackwardReaching(chain);
                    change += change(chain);
                    finished = false;
                }
            }

            for(GenericFrame child : end.children()){
                if(!(child instanceof GenericFrame)) continue;
                executeBackwardReaching(root, (GenericFrame) child);
            }
        }

        public boolean isSetup() {
            return setup;
        }

        public void setSetup(boolean setup) {
            this.setup = setup;
        }

        public boolean execute(){
            if(setup){
                setup = false;
                getSubBases();
                printSubBases();
                printEndEffectors();
            }
            finished =true;
            change = 0;
            //Stage 1: Forward Reaching
            printPositions(root);
            executeForwardReaching(root);
            printPositions(root);
            //Stage 2: Backward Reaching
            printPositions(root);
            executeBackwardReaching(root, root);
            printPositions(root);

            if(change <= MINCHANGE) return true;
            return finished;
        }

        public void update(){
            update(root);
        }

        @Override
        public boolean stateChanged() {
            return false;
        }

        @Override
        public void reset() {

        }

        public void update(GenericFrame joint){
            if(!endEffector.contains(joint.id())) return;
            if(joint.children().size() > 1){
                joint.setPosition(positions.get(joint.id()));
            }else{
                //posToQuat(joint, joint.referenceFrame(), positions.get(joint.id()));
            }

            for(GenericFrame child : joint.children()){
                if(!(child instanceof GenericFrame)) continue;
                update((GenericFrame)child);
            }
        }

        public ArrayList<GenericFrame> getShortestChain(GenericFrame j){
            ArrayList<GenericFrame> chain = new ArrayList<GenericFrame>();
            chain.add(0, j);
            GenericFrame cur = (GenericFrame)j.referenceFrame();
            while(cur != root && !subBase.containsKey(cur.id()) ){
                chain.add(0, cur);
                cur = (GenericFrame)cur.referenceFrame();
            }
            if(cur == root) chain.add(0, cur);
            return chain;
        }

        public boolean isLeaf(GenericFrame joint){
            //A joint is a leaf when there is no other end effector next to it
            for(GenericFrame child : joint.children()){
                if(!(child instanceof GenericFrame)) continue;
                if(endEffector.contains(((GenericFrame)child).id())) return false;
            }
            return true;
        }

        //Debug Methods
        public void printSubBases(){
            printHead("Sub bases");
            for(Map.Entry<Integer, Integer> entry : subBase.entrySet()){
                System.out.println("Sub-base : " + entry.getKey());
                System.out.println("Children : " + entry.getValue());
            }
            printHead("");
        }

        public void printEndEffectors(){
            printHead("END EFFECTORS");
            for(Integer i : endEffector){
                System.out.println("End effectors : " + i);
            }
            printHead("");
        }

        public void printChain(ArrayList<GenericFrame> chain){
            printHead("CHAIN");
            System.out.print("[");
            for(GenericFrame i : chain){
                System.out.print(" " + i.id() + ", ");
            }
            System.out.println("]");
            printHead("");
        }

        public void printHead(String s){
            System.out.println("-----------------------");
            System.out.println("-----------------------");
            System.out.println(s);
        }

        public void printPositions(GenericFrame joint){
            System.out.println("JOINT INFORMATION");
            System.out.println("Joint " +joint.id() + " Initial Position : " + joint.position());
            if(!endEffector.contains(joint.id())) return;
            System.out.println("Joint " + joint.id()  + " Final Position : " + positions.get(joint.id()));
            for(GenericFrame child : joint.children()){
                if(child instanceof GenericFrame)
                    printPositions(child);
            }
        }

    }
}
