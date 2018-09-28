//
// particle
// by: Dr. Brown
// Partilce
// (adapted from example in "The Nature of Code")
//
class Particle extends Object {
  float lifespan = 255.0;   // life and transparency of particle
  float deathRate = 3.0;    // how much is subtracted each draw

  //
  // Particle
  // Constructor for ellipse particles.
  //
  Particle(PVector loc, float w, float h) {
    super(loc.x, loc.y, w, h);
    velocity = new PVector(random(-1,1),random(-1,0));
  }
  
  //
  // Particle
  // Constructor for image particles.
  Particle(PVector loc,PImage img) {
    super(loc.x, loc.y, img.width, img.height);
    float vx = (float) generator.nextGaussian()*0.3;
    float vy = (float) generator.nextGaussian()*0.3 - 1.0;
    velocity = new PVector(vx,vy);
    myImage = img;
  }
  
  //
  // run
  // Update physics and lifespan, then render particle.
  //
  void run() {
    update();
    render();
  }
 
 //
 // update
 // Draw and update life.
 //
  void update() {
    super.update();
    lifespan -= deathRate;
  }
 
 //
 // render
 // Draw the particle.
 //
  void render() {
    if (myImage != null) {
      imageMode(CENTER);
      tint(255,lifespan);
      image(myImage,location.x,location.y);
    }
    else {
      stroke(0,lifespan);
      strokeWeight(1);
      fill(255,lifespan);
      ellipse(location.x, location.y, size.x, size.y);
    }
  }

  //
  // isDead
  // The particle isDead if it is invisible.
  //
  boolean isDead() {
    if (lifespan < 0.0) {
      return true;
    } else {
      return false;
    }
  }
}