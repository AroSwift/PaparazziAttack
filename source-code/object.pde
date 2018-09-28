//
// Object
// by Dr. David Brown (adapted from "The Nature of Code")
// 
// Top level virtual world construct. Handles mass, force,
// location, velocity, acceleration and rotation. Has default
// display capabilities of a filled rectangle or a tinted or
// untinted image.
//
class Object {
  PVector size;                 // size of object
  float   mass;                 // mass of object (mostly ignored)
  PVector myColor;              // color of object (RGB)
  PImage  myImage;              // image of object (if applicable)
  PVector location;             // location of object  
  PVector velocity;             // speed of object
  PVector acceleration;         // acceleration of object
  float   rotation;             // rotation of object
  float   rotationVelocity;     // speed of rotation
  float   rotationAcceleration; // acceleration of object rotation
  float widthBuffer;            // offset from window width boundaries
  float heightBuffer;           // offset from window height boundaries

  float maxVelocity = 5;        // maximum object velocity default
  final   float G = 0.4;        // force of gravity
  
  //
  // Object
  // Constructor. Allows designation of location and RGB color of the
  // object.
  //
  Object(float x, float y, float w, float h, float red, float green, float blue) {
    this(x,y,w,h);
    myColor = new PVector(red, green, blue);
  }
  
  //
  // Object
  // Constructor. Allows designation of location and an image for the
  // object.
  //
  Object(float x, float y, String img) {
    this(x,y,0,0);
    myImage = loadImage(img);
    size.x = myImage.width;
    size.y = myImage.height;
  }
  
  //
  // Object
  // Constructor. Allows only designation of object location. Called
  // from other more specialized constructors.
  //
  Object(float x, float y, float w, float h) {
    location = new PVector(x, y);
    size = new PVector(w, h);
    mass = x * y;
    // Defaults to no movement.
    velocity = new PVector(0, 0, 0);
    acceleration = new PVector(0, 0, 0);
    // Defaults to no rotation.
    rotation = 0;
    rotationVelocity = 0;
    rotationAcceleration = 0;
    // No color or image yet specified.
    myColor = null;
    myImage = null;
    widthBuffer = size.x/2;
    heightBuffer = size.y/2;     
  }
  
  //
  // applyRotation
  // Accumulate acceleration forces.
  //
  void applyRotation(float rot) {
    rotationAcceleration += rot;
  }

  //
  // applyForce
  // Accumulate movement forces.
  //
  void applyForce(PVector force) {
    //PVector f = PVector.div(force, mass);  //Take gravity into account (NOT)
    acceleration.add(force);
  }

  //
  // update
  // Implement the physics of movement.
  //
  void update() {
    velocity.add(acceleration);
    velocity.x = constrain(velocity.x, -maxVelocity, maxVelocity);
    velocity.y = constrain(velocity.y, -maxVelocity, maxVelocity);
    location.add(velocity);
    acceleration.mult(0);
  }
  
  //
  // updateRotation
  // Rotate the object if directed.
  //
  void updateRotation() {
    rotationVelocity += rotationAcceleration;
    rotation += rotationVelocity;
    rotationAcceleration = 0;
  }

  //
  // display
  // High-level routine called by owning objects. Calls low-level routines
  // to keep the object in bounds, update its movement and rotation, and
  // finally to show itself.
  //
  void display() {
    // Keep in bounds and move/rotate.
    enforceBoundaries();
    update();
    updateRotation();
    // Draw object.
    pushMatrix();
    translate(location.x, location.y);
    rotate( radians(rotation) );
    if (myColor != null) {
      fill(myColor.x, myColor.y, myColor.z);
    }
    show();    // Usually overridden in subclasses...
    popMatrix();
  }
  
  //
  // show
  // Draw the object. The default behavior here allows for a filled rectangle
  // or a tinted or untinted image. This method is usually either overridden
  // or called in conjuction with additional code in object subclasses.
  //
  void show() {
    // Either draw an image...
    if (myImage != null) {
       pushMatrix();
       translate(-size.x/2, -size.y/2);
       image(myImage, 0, 0, size.x, size.y);
       popMatrix();
    }
    // or a filled rectangle
    else 
      rect(-size.x/2, -size.y/2, size.x, size.y);
  }

  //
  // attract
  // Calculate a normalized vector from an object to an object that it
  // is attracted to. The calling function can then scale the returned
  // vector to simulate the strength of the desire force.
  //
  PVector attract(Object m) {
    PVector force = PVector.sub(location, m.location);   // Calculate direction of force
    float distance = force.mag();                        // Distance between objects
    distance = constrain(distance, 5.0, 25.0);           // Limiting the distance 
    force.normalize();                                   // Normalize vector (we just want direction)

    float strength = (G * mass * m.mass) / (distance * distance); // Calculate gravitional force magnitude
    force.mult(strength);                                         // Get force vector --> magnitude * direction
    return force;
  }

  //
  // enforceBoundaries
  // Try to keep the objects within the boundaries of the window.
  // Am going to expand this in the future to allow objects within
  // other objects.
  //
  void enforceBoundaries() {    
    if (location.x < widthBuffer) {
      velocity.x = velocity.x * -1;
      location.x = widthBuffer;
    }
    else if (location.x > (width - widthBuffer)) {
      velocity.x = velocity.x * -1;
      location.x = width - widthBuffer;
    }
    if (location.y < heightBuffer) {
      velocity.y = velocity.y * -1;
      location.y = heightBuffer;
    }
    if (location.y > (height - heightBuffer)) {
      velocity.y = velocity.y * -1;
      location.y = height - heightBuffer;
    }
  }
  
  //
  // isWithin
  // Check to see if an x,y location is within the bounds of an object.
  //
  boolean isWithin(float x, float y) {
    float w = size.x/2;
    float h = size.y/2;
    return ((x >= (location.x - w)) && (x <= (location.x + w)) &&
        (y >= (location.y - h)) && (y <= (location.y + h)));
  }
}