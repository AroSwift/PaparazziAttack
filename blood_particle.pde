//
// BloodParticle
// by: Dr. David Brown
//
// Simulates an exploding blood drop.
//
class BloodParticle extends Particle {
  
    //
    // BloodParticle
    // Create a blood drop of size w,h at loc.
    //
    BloodParticle(PVector loc, float w, float h) {
      super(loc, w, h);
      velocity = new PVector(random(-3,3),random(-3,3));
      deathRate = 2.5;
    }
    
    //
    // render
    // Draw a single drop of blood with a shape that varies a bit
    // randomly with each render.
    //
    void render() {
      stroke(0,lifespan);
      strokeWeight(0);
      fill(150,30,30,lifespan);
      ellipse(location.x, location.y, size.x+random(-1,1),size.y+random(-1,1));
  }
}