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
* TODO: Add Multiple End Effector Solver in scene
* TODO: Modify FABRIK Basic Algorithm using Local Coords (More Efficient)
* */

public  abstract class Solver {
    /*Convenient String to register/unregister solvers in an Abstract Scene*/
    protected float ERROR = 0.01f;
    protected int MAXITER = 200;
    protected float MINCHANGE = 0.01f;
    protected float TIMESPERFRAME = 1.f;
    protected float FRAMECOUNTER = 0;
    protected int iterations = 0;
    private float weight = 1; //attribute used for Multiple End Effectors System

    /*Store Joint's desired position*/
    protected ArrayList<Vec> positions = new ArrayList<Vec>();

    public ArrayList<Vec> getPositions(){ return positions;}

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

    /*
    * Performs First Stage of FABRIK Algorithm, receives a chan of Frames, being the Frame at i
    * the reference frame of the Frame at i + 1
    * */

    public void executeForwardReaching(ArrayList<? extends Frame> chain){
        for(int i = chain.size()-2; i >= 0; i--){
            Vec pos_i = positions.get(i);
            Vec pos_i1 = positions.get(i+1);
            /*Check constraints (for Ball & Socket) it is not applied in First iteration
            * Look at paper FABRIK: A fast, iterative solver for the Inverse Kinematics problem For more information*/
            Vec pos_i2 = null;
            if(i != chain.size()-2){
                pos_i2 = positions.get(i+2);
            }
            Vec pos_i1_constrained = applyConstraintsForwardStage(chain.get(i + 1), chain.get(i + 1).referenceFrame(),  pos_i, pos_i1, pos_i2);
            /*Checking constraints without considering special cases in Forward Step, as the Paper suggests, is possible, solutions are not as good thought*/
            //Vec pos_i1_constrained = applyConstraintsBackwardStage(chain.get(i + 1), chain.get(i + 1).referenceFrame(), pos_i, pos_i1);
            Vec diff = Vec.subtract(pos_i1, pos_i1_constrained);
            pos_i.add(diff);
            positions.set(i, pos_i);
            float r_i = Vec.distance(pos_i, pos_i1);
            float dist_i = chain.get(i+1).translation().magnitude()/chain.get(i+1).magnitude();
            if(dist_i == 0){
                positions.set(i, pos_i1.get());
                continue;
            }
            float lambda_i =  dist_i/r_i;
            Vec new_pos = Vec.multiply(pos_i1, 1.f - lambda_i);
            new_pos.add(Vec.multiply(pos_i, lambda_i));
            positions.set(i, new_pos);
        }
    }

    /*Return the total distance between the configuration at beginning of the Iteration and the final configuration*/
    public float executeBackwardReaching(ArrayList<? extends Frame> chain){
        float change = 0;
        for(int i = 0; i < chain.size()-1; i++){
            Vec pos_i = positions.get(i);
            Vec pos_i1 = positions.get(i+1);

            /*Check constraints*/
            pos_i1 = applyConstraintsBackwardStage(chain.get(i+1), chain.get(i+1).referenceFrame(), pos_i, pos_i1);
            positions.set(i+1, pos_i1);
            //Get the distance between Joint i and the Target
            float r_i = Vec.distance(pos_i, pos_i1);
            float dist_i = chain.get(i+1).translation().magnitude()/chain.get(i+1).magnitude();
            if(dist_i == 0){
                positions.set(i+1, pos_i.get());
                continue;
            }
            float lambda_i =  dist_i/r_i;
            Vec new_pos = Vec.multiply(pos_i, 1.f - lambda_i);
            new_pos.add(Vec.multiply(pos_i1, lambda_i));
            positions.set(i+1, new_pos);

            Rotation delta = null;
            if(chain.get(i+1).is3D())
                delta = posToQuat(chain.get(i+1), chain.get(i+1).referenceFrame(), chain.get(i+1).referenceFrame().position(), positions.get(i+1));
            else
                delta = posToRot(chain.get(i+1), chain.get(i+1).referenceFrame(), chain.get(i+1).referenceFrame().position(), positions.get(i+1));

            chain.get(i+1).referenceFrame().rotate(delta);
            Vec constrained_pos = chain.get(i+1).position().get();
            change += Vec.distance(positions.get(i+1),constrained_pos);
            positions.set(i+1, constrained_pos);
        }
        return change;
    }
    /*Set Joint's Parent orientation assuming that
     * new Joint position is v*/
    public Rot posToRot(Frame j, Frame parent, Vec o, Vec p){
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
    * Frame J is the frame used to verify if the orientation of Parent is appropriate,
    * Vec o is a Vector where Parent is located, whereas p is express the position of J
    * Vec q is the position of Child of J.
    * */
    public Vec applyConstraintsForwardStage(Frame j, Frame parent, Vec o, Vec p, Vec q){
        if(parent.constraint() instanceof BallAndSocket){
            if(q == null) return p;
            Vec newTranslation = Vec.subtract(q,p);
            newTranslation.add(parent.position());
            newTranslation = parent.coordinatesOf(newTranslation);
            //Get The Quat between current Translation and new Translation to set new rotation axis
            Quat deltaRestRotation = new Quat(j.translation(), newTranslation);
            BallAndSocket constraint = (BallAndSocket) parent.constraint();
            Quat desired = (Quat) Quat.compose(parent.rotation(), posToQuat(j, parent, o, p));
            Vec target = Quat.multiply(desired, j.translation());
            Quat restRotation = (Quat) Quat.compose(parent.rotation(), constraint.getRestRotation().inverse());
            restRotation = (Quat) Quat.compose(constraint.getRestRotation(), restRotation);
            target = constraint.getConstraint(target, (Quat) Quat.compose(restRotation,deltaRestRotation));
            target.normalize();
            target.multiply(Vec.subtract(p,o).magnitude());
            return parent.inverseCoordinatesOf(Quat.multiply((Quat)parent.rotation().inverse(), target));
        } else if(parent.constraint() instanceof Hinge){
            if(parent.is2D()){
                /*Consider same steps as in Backward Step*/
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

    /*
    * Check the type of the constraint related to the Frame Parent,
    * Frame J is the frame used to verify if the orientation of Parent is appropriate,
    * Vec o is a Vector where Parent is located, whereas p is express the position of J
    * */
    public Vec applyConstraintsBackwardStage(Frame j, Frame parent, Vec o, Vec p){
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

        if(iterations == MAXITER){
            return true;
        }
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

    public float change(ArrayList<? extends Frame> chain){
        float change = 0.f;
        for(int i = 0; i < chain.size(); i++){
            change += Vec.distance(chain.get(i).position(), positions.get(i));
        }
        return change;
    }

    public static class ChainSolver extends Solver{

        //TODO: It will be useful that any Joint in the chain could have a Target ?
        //TODO: Enable Translation of Head (Skip Backward Step)

        protected ArrayList<? extends Frame> chain;
        private ArrayList<Frame> bestSolution;

        protected Frame target;
        private Frame prevTarget;

        public ArrayList<? extends Frame> getChain() {
            return chain;
        }

        private ArrayList<Frame> copyChain(ArrayList<? extends Frame> list){
            ArrayList<Frame> copy = new ArrayList<Frame>();
            Frame reference = list.get(0).referenceFrame();
            if(reference != null){
                reference = new Frame(reference.position().get(), reference.orientation().get());
            }
            for(Frame joint : list){
                Frame newJoint = new Frame(joint.is3D());
                newJoint.setReferenceFrame(reference);
                newJoint.setPosition(joint.position().get());
                newJoint.setOrientation(joint.orientation().get());
                newJoint.setConstraint(joint.constraint());
                copy.add(newJoint);
                reference = newJoint;
            }
            return copy;
        }

        public void setChain(ArrayList<? extends Frame> chain) {
            this.chain      = chain;
            bestSolution    = copyChain(chain);
        }

        public Frame getTarget() {
            return target;
        }

        public void setTarget(Frame target) {
            this.target = target;
        }

        public Frame getHead(){
            return chain.get(0);
        }

        public Frame getEndEffector(){
            return chain.get(chain.size()-1);
        }

        public ChainSolver(ArrayList<? extends Frame> chain){
            this(chain, null);
        }

        public ChainSolver(ArrayList<? extends Frame> chain, Frame target){
            setChain(chain);
            positions = new ArrayList<Vec>();
            for(Frame joint : chain){
                positions.add(joint.position().get());
            }
            this.target = target;
            this.prevTarget =
                    target == null ? null : new Frame(target.position().get(), target.orientation().get());
        }
        /*Get maximum length of a given chain*/
        public float getLength(){
            float dist = 0;
            for(int i = 1; i < chain.size(); i++){
                dist += chain.get(i).translation().magnitude()/chain.get(i).magnitude();
            }
            return dist;
        }

        public void stretchChain(ArrayList<? extends Frame> chain, Vec target){
            for(int i = 0; i < chain.size()-1; i++){
                //Get the distance between Joint i and the Target
                Vec pos_i = positions.get(i);
                float r_i = Vec.distance(pos_i, target);
                float dist_i = chain.get(i+1).translation().magnitude()/chain.get(i+1).magnitude();
                float lambda_i =  dist_i/r_i;
                Vec new_pos = Vec.multiply(pos_i, 1.f - lambda_i);
                new_pos.add(Vec.multiply(target, lambda_i));
                positions.set(i+1, new_pos);
            }
        }


        /*
         * Performs a FABRIK ITERATION
         *
         * */
        public boolean execute(){
            //As no target is specified there is no need to perform FABRIK
            if(target == null) return true;
            Frame root  = chain.get(0);
            Frame end   = chain.get(chain.size()-1);
            Vec target  = this.target.position().get();

            //Execute Until the distance between the end effector and the target is below a threshold
            if(Vec.distance(end.position(), target) <= ERROR){
                return true;
            }

            //Get the distance between the Root and the End Effector
            float length = getLength();
            //Get the distance between the Root and the Target
            float dist = Vec.distance(root.position(), target);
            //When Target is unreachable        //Debug methods
                /*if(dist > length){
                stretchChain(chain, target);
                return true;
            }else{*/
            //Initial root position
            Vec initial = positions.get(0).get();
            //Stage 1: Forward Reaching
            positions.set(chain.size()-1, target.get());
            executeForwardReaching();
            //Stage 2: Backward Reaching
            positions.set(0, initial);
            float change = executeBackwardReaching();
            //Save best solution
            if(Vec.distance(target, end.position()) <  Vec.distance(target, bestSolution.get(chain.size()-1).position())) {
                bestSolution = copyChain(chain);
            }
            //Check total position change
            if(change <= MINCHANGE) return true;
            return false;
        }

        public void executeForwardReaching(){
            executeForwardReaching(chain);
        }

        public float executeBackwardReaching(){
            return executeBackwardReaching(chain);
        }

        public void update(){
            for(int i = 0; i < chain.size(); i++){
                chain.get(i).setRotation(bestSolution.get(i).rotation().get());
            }
        }

        public boolean stateChanged(){
            if(target == null){
                prevTarget = null;
                return false;
            }else if(prevTarget == null) {
                return true;
            }
            return !(prevTarget.position().equals(target.position()) && prevTarget.orientation().equals(target.orientation()));
        }

        public void reset(){
            prevTarget = target == null ? null : new Frame(target.position().get(), target.orientation().get());
            iterations = 0;
        }

    }

    public static class TreeSolver extends Solver{

        /*Convenient Class to store ChainSolvers in a Tree Structure*/
        private static class Node{
            private Node parent;
            private ArrayList<Node> children;
            private ChainSolver solver;
            private boolean modified;
            private float weight = 1.f;

            public Node(){
                children = new ArrayList<Node>();
            }

            public Node(ChainSolver solver){
                this.solver = solver;
                children = new ArrayList<Node>();
            }
            private Node(Node parent, ChainSolver solver){
                this.parent = parent;
                this.solver = solver;
                if(parent != null){
                    parent.addChild(this);
                }
                children = new ArrayList<Node>();
            }
            private boolean addChild(Node n){
                return  children.add(n);
            }

            private ArrayList<Node> getChildren(){ return children; }
            private float getWeight(){ return  weight; }
            private ChainSolver getSolver(){ return solver; }

            public boolean isModified() {
                return modified;
            }

            public void setModified(boolean modified) {
                this.modified = modified;
            }
        }

        //TODO Relate weights with End Effectors not with chains
        /*Tree structure that contains a list of Solvers that must be accessed in a BFS way*/
        private Node root;

        public GenericFrame getHead(){
            return (GenericFrame) root.getSolver().getHead();
        }

        public void setup(Node parent, GenericFrame frame, ArrayList<GenericFrame> list){
            if(frame == null) return;
            if(frame.children().isEmpty()){
                list.add(frame);
                ChainSolver solver = new ChainSolver(list, null);
                new Node(parent, solver);
                return;
            }
            if(frame.children().size() > 1){
                list.add(frame);
                ChainSolver solver = new ChainSolver(list,null);
                Node node = new Node(parent, solver);
                for(GenericFrame child : frame.children()){
                    ArrayList<GenericFrame> newList = new ArrayList<GenericFrame>();
                    newList.add(frame);
                    setup(node, child, newList);
                }
            }else{
                list.add(frame);
                setup(parent, frame.children().get(0), list);
            }
        }

        private boolean addTarget(Node node, GenericFrame endEffector, Frame target){
            if(node == null) return false;
            if(((GenericFrame)node.getSolver().getEndEffector()).id() == endEffector.id()){
                node.getSolver().setTarget(target);
                return true;
            }
            for(Node child : node.getChildren()){
                addTarget(child, endEffector, target);
            }
            return false;
        }

        public boolean addTarget(GenericFrame endEffector, Frame target){
            return addTarget(root, endEffector, target);
        }

        public TreeSolver(GenericFrame genericFrame){
            Node dummy = new Node(); //Dummy Node to Keep Reference
            setup(dummy, genericFrame, new ArrayList<GenericFrame>());
            //dummy must have only a child,
            this.root = dummy.getChildren().get(0);
        }

        public int executeForward(Node node){
            float totalWeight = 0;
            boolean modified = false;
            int chains = 0;
            for(Node child : node.getChildren()) {
                chains += executeForward(child);
                if(child.getSolver().getTarget() != null) totalWeight += child.getWeight();
                modified = modified || child.isModified();
            }
            //Stage 1: Forward Reaching
            ChainSolver solver = node.getSolver();
            //TODO: add embedded target and enable to give it some weight - Weight/Target as an attribute of Chain or as Node attribute?
            //Update Target according to children Head new Position
            Vec newTarget = new Vec();
            for(Node child : node.getChildren()) {
                //If Child Chain Joints new positions doesn't matter
                if(child.getSolver().getTarget() == null) continue;
                newTarget.add(Vec.multiply(child.getSolver().getPositions().get(0),1.f/totalWeight));
            }
            if(newTarget.magnitude() > 0){
                solver.setTarget(new Frame(newTarget,solver.getEndEffector().orientation().get()));
            }

            //Execute Until the distance between the end effector and the target is below a threshold
            if(solver.getTarget() == null){
                node.setModified(false);
                return 0;
            }
            if(Vec.distance(solver.getEndEffector().position(), solver.getTarget().position()) <= ERROR){
                node.setModified(false);
                return 0;
            }
            solver.getPositions().set(solver.getChain().size()-1, solver.target.position().get());
            solver.executeForwardReaching();
            node.setModified(true);
            return chains + 1;
        }

        public float executeBackward(Node node){
            float change = MINCHANGE;
            if(node.isModified()){
                ChainSolver solver = node.getSolver();
                solver.getPositions().set(0,solver.getHead().position());
                change = solver.executeBackwardReaching();
                /*When executing Backward Step, if the Frame is a SubBase (Has more than 1 Child) and
                 * it is not a "dummy Frame" (Convenient Frame that constraints position but no orientation of
                 * its children) then an additional step must be done: A Weighted Average of Positions to establish
                 * new Frame orientation
                 * */
                //TODO : Perhaps add an option to not execute this step
                // (Last chain modified determines Sub Base orientation)
                if(node.getChildren().size() > 1){
                    Vec centroid = new Vec();
                    Vec newCentroid = new Vec();
                    float totalWeight = 0;
                    for(Node child: node.getChildren()){
                        //If target is null, then Joint must not be included
                        if(child.getSolver().getTarget() == null) continue;
                        if(child.getSolver().getChain().size() < 2) continue;
                        if(child.getSolver().getChain().get(1).translation().magnitude() == 0) continue;
                        Vec diff = Vec.subtract(child.getSolver().getChain().get(1).position(),solver.getHead().position());
                        centroid.add(Vec.multiply(diff, child.getWeight()));
                        if(child.isModified()){
                            diff = Vec.subtract(child.getSolver().getPositions().get(1),solver.getHead().position());
                            newCentroid.add(Vec.multiply(diff, child.getWeight()));
                        }else{
                            newCentroid.add(Vec.multiply(diff, child.getWeight()));
                        }
                        totalWeight += child.getWeight();
                    }
                    //Set only when Centroid and New Centroid varies
                    if(Vec.distance(centroid,newCentroid) > 0.001){
                        centroid.multiply(1.f/totalWeight);
                        newCentroid.multiply(1.f/totalWeight);
                        if(node.getSolver().getEndEffector().is3D()){
                            Quat deltaOrientation = new Quat(centroid, newCentroid);
                            node.getSolver().getEndEffector().setOrientationWithConstraint(Quat.compose(node.getSolver().getEndEffector().orientation(),deltaOrientation));
                        }else{
                            Rot deltaOrientation = new Rot(centroid, newCentroid);
                            node.getSolver().getEndEffector().setOrientationWithConstraint(Rot.compose(node.getSolver().getEndEffector().orientation(),deltaOrientation));
                        }
                        for(Node child : node.getChildren()){
                            if(child.getSolver().getChain().size() < 2) continue;
                            if(child.getSolver().getChain().get(1).translation().magnitude() == 0) continue;
                            if(child.isModified()){
                                child.getSolver().getPositions().set(1, child.getSolver().getChain().get(1).position());
                            }
                        }
                    }
                }
            }
            for(Node child : node.getChildren()){
                change += executeBackward(child);
            }
            return change;
        }

        @Override
        public boolean execute(){
            int modifiedChains =  executeForward(root);
            float change = executeBackward(root);
            //Check total position change
            if(change/(modifiedChains*1.) <= MINCHANGE) return true;
            return false;
        }

        @Override
        public void update() {
            //As BackwardStep modify chains, no update is required
        }

        //Update Subtree that have associated Frame as root
        public boolean updateTree(Node node, GenericFrame frame){
            if(((GenericFrame)node.getSolver().getEndEffector()).id() == frame.id()){
                setup(node, frame, new ArrayList<GenericFrame>());
                return true;
            }
            for(Node child : node.getChildren()){
                updateTree(child, frame);
            }
            return false;
        }

        private boolean stateChanged(Node node) {
            if(node == null) return false;
            if(node.getSolver().stateChanged()) return true;
            for(Node child : node.getChildren()){
                if(stateChanged(child)) return true;
            }
            return false;
        }

        @Override
        public boolean stateChanged() {
            return stateChanged(root);
        }

        private void reset(Node node){
            if(node == null) return;
            //Update Previous Target
            if(node.getSolver().stateChanged()) node.getSolver().reset();
            for(Node child : node.getChildren()){
                reset(child);
            }
        }

        @Override
        public void reset() {
            iterations = 0;
            reset(root);
        }
    }
}
