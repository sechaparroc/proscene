package remixlab.dandelion.constraint;

import remixlab.dandelion.geom.Frame;
import remixlab.dandelion.geom.Quat;
import remixlab.dandelion.geom.Rotation;
import remixlab.dandelion.geom.Vec;

import java.util.ArrayList;

import static java.lang.Math.PI;

/**
 * Created by sebchaparr on 19/08/17.
 */
public class PlanarPolygon extends Constraint{
    /*
    TODO: Enable Setting different Axis Direction
    * With this Kind of Constraint no Translation is allowed
    * and the rotation depends on a Cone which base is a Polygon. This kind of constraint always
    * look for the reference frame (local constraint), if no initial position is
    * set a Quat() is assumed as rest position
    * */

    private ArrayList<Vec> vertices = new ArrayList<Vec>();
    private float radius = 10.f;//Vertices must be inside a circle of radius r
    private Quat restRotation = new Quat();

    public Quat getRestRotation() {
        return restRotation;
    }
    public void setRestRotation(Quat restRotation) {
        this.restRotation = restRotation.get();
    }

    public ArrayList<Vec> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vec> vertices) {
        this.vertices = vertices;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public PlanarPolygon(){
        vertices = new ArrayList<Vec>();
        restRotation = new Quat();
    }

    public PlanarPolygon(ArrayList<Vec> vertices, Quat restRotation) {
        this.vertices = vertices;
        this.restRotation = restRotation.get();
    }

    public PlanarPolygon(ArrayList<Vec> Vertices) {
        this.vertices = vertices;
    }

    @Override
    public Rotation constrainRotation(Rotation rotation, Frame frame) {
        if(frame.is2D())
            throw new RuntimeException("This constrained not supports 2D Frames");
        Quat desired = (Quat) Quat.compose(frame.rotation(),rotation);
        Vec new_pos = Quat.multiply(desired, new Vec(0,0,1));
        Vec constrained = getConstraint(new_pos, restRotation);
        //Get Quaternion
        return new Quat(new Vec(0,0,1), Quat.multiply((Quat)frame.rotation().inverse(),constrained));
    }


    @Override
    public Vec constrainTranslation(Vec translation, Frame frame) {
        return new Vec(0,0,0);
    }


    public Vec getConstraint(Vec target){
        return getConstraint(target, restRotation);
    }

    public Vec getConstraint(Vec target, Quat restRotation){
        Vec uvec    = Quat.multiply(restRotation,new Vec(0,1,0));
        Vec rvec = Quat.multiply(restRotation,new Vec(1,0,0));
        Vec line = Quat.multiply(restRotation, new Vec(0,0,1));

        return null;
    }


}
