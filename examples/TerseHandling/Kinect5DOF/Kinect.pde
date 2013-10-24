public class Kinect {
  SimpleOpenNI  context;    //Context for the Kinect handler
  Hand left, right;          // Hands position
  PVector posit;            // The vector with position values calculated with hands positions
  PVector rotat;            // The vector with rotation values calculated with hands positions
  PVector initial;          // Initial position, set when two hands are detected
  boolean initialDefined;   // True if the initial vector was defined

  /**
   * Kinect object constructor
   * @param PApplet p PApplet object
   */
  public Kinect(PApplet p) {
    //Kinect init
    context = new SimpleOpenNI(p);
    if (context.isInit() == false) {
      println("ERROR PARSING KINECT: Check if the camera is connected.");
    }
    else {
      // disable mirror
      context.setMirror(true);

      // enable depthMap generation 
      context.enableDepth();

      // enable skeleton generation for all joints
      context.enableUser();

      // Initialize the hands vectors
      left = new Hand(color(0, 0, 255));
      right = new Hand(color(0, 0, 255));
      //Initialize the movement vectors
      posit=rotat=new PVector(0, 0, 0);

      // Initialize the initial vector
      initialDefined=false;
      initial=new PVector(0, 0, 0);

      //Update for the first time the hands to define the starting vector
      update();
    }
  }

  /**
   * Update the Kinect context and the hand positions
   */
  public void update() {
    //update the camera context
    context.update();
    //Update the hands position
    updateHands();
    //If the hands are detected by the first time, set the initial vector positions
    if (isDetectingHands()&&!initialDefined) {
      initial=positionVector();
      initialDefined=true;
    }
  }
  
  /**
   * Draw the hands
   */
  public void draw() {
    drawScreenElements();
  }

  void drawScreenElements() {
    scene.beginScreenDrawing();
    pushStyle();
    //Draw the depth image
    image(context.userImage(), 0, 0, width/3, height/3);
    left.draw();
    right.draw();
    // Draw the position vector
    fill(color(255, 140, 0));
    ellipse(posit.x, posit.y, 10, 10);
    popStyle();
    scene.endScreenDrawing();
  }

  /**
   * Return the position computed from the position of the hands
   * @param PVector Position vector
   */
  public PVector positionVector() {
    posit=new PVector(0, 0, 0);
    if (isActiveUser()) {
      posit.x=((left.getPoint().x+right.getPoint().x)/2);
      posit.y=((left.getPoint().y+right.getPoint().y)/2);
      posit.z=((left.getPoint().z+right.getPoint().z)/2);
    }
    return posit;
  }
  
  /**
   * Return the difference between the initial position and the current position
   * @param PVector Position vector
   */
  public PVector deltaPositionVector() {
    PVector delta=new PVector(0, 0, 0);
    PVector posit=positionVector();
    if (isActiveUser()) {
      delta.x=initial.x-posit.x;
      delta.y=initial.y-posit.y;
      delta.z=initial.z-posit.z;
    }
    return delta;
  }
  
  /**
   * Return the rotations computed from the position of the hands
   * @param PVector Rotation vector
   */
  public PVector rotationVector() {
    rotat=new PVector(0, 0, 0);
    if (isActiveUser()) {
      //TODO: Define a gesture to x-rotation rotation.x=(left.getPoint().x-right.getPoint().x);
      rotat.x=0;
      rotat.y=-(left.getPoint().z-right.getPoint().z);
      rotat.z=-(left.getPoint().y-right.getPoint().y);
    }
    return rotat;
  }

  /**
   * Update the hands position with the screen values (projective coordinates)
   */
  private void updateHands() {
    if (isActiveUser()) {
      int[] userList = context.getUsers();
      if (context.isTrackingSkeleton(userList[0])) {
        PVector leftPoint=new PVector();
        PVector rightPoint=new PVector();
        context.getJointPositionSkeleton(userList[0], SimpleOpenNI.SKEL_LEFT_HAND, leftPoint);
        context.getJointPositionSkeleton(userList[0], SimpleOpenNI.SKEL_RIGHT_HAND, rightPoint);
        left.setPoint(getScreen(leftPoint));
        right.setPoint(getScreen(rightPoint));
      }
    }
  }
  
  private PVector getScreen(PVector point) {
    PVector screenPos = new PVector();
    context.convertRealWorldToProjective(point, screenPos);
    return screenPos;
  }
  
  /**
   * Check if there is an active user in the openni context.
   * This example only works with the first registered user.
   * @return boolean True if is an active user, false otherwise
   */
  private boolean isActiveUser() {
    boolean active=false;
    if (context.isInit() == true) {
      int[] userList = context.getUsers();
      if (userList.length>=1) {
        active=true;
      }
    }
    return active;
  }
  
  /**
   * Check if the device is detecting both hands.
   */
  private boolean isDetectingHands() {
    if (left.getPoint().x==0&&left.getPoint().y==0&&left.getPoint().z==0&&
      right.getPoint().x==0&&right.getPoint().y==0&&right.getPoint().z==0)
      return false;
    else
      return true;
  }
  
  // SimpleOpenNI events
  public void onNewUser(SimpleOpenNI curContext, int userId) {
    println("New user detected: " + userId);
    curContext.startTrackingSkeleton(userId);
  }
}

