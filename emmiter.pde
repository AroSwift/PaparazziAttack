import java.util.*;
//
// Emitter
// by: Dr. David Brown
//
// Emit Particles at a given rate. 
//
class Emitter extends Object{
  ArrayList<Particle>  particles;                // list of particles
  float                nextEmission = 0;         // time in ms of next emission
  float                emitRate;                 // how fast to emit particles
  boolean              finishedEmission = false; // whether all particles have been emitted
  
  //
  // Emitter
  // Constructor. Create an Emitter at x,y of size w,h to emit
  // particles at rate (in ms).
  //
  Emitter(float x, float y, float w, float h, float rate) {
    super(x, y, w, h);
    emitRate = rate;
    particles = new ArrayList();
  }
  
  //
  // addParticle
  // Add a Particle at a given location.
  //
  void addParticle(PVector loc) {
    particles.add(new Particle(loc,size.x,size.y));
  }

  // addParticle
  // Add a Particle object at a given location. Used to 
  // add subclassed Particles.
  //
  void addParticle(PVector loc, Particle p) {
    particles.add(p);
  }

  //
  // run
  // Emits particles at regular intervals and also
  // runs particles until they die.
  //
  void run() {
   // time to emit?
   if (millis() > nextEmission) {
        emit();
        nextEmission = millis() + emitRate;
    }
    // run particles.
    Iterator<Particle> it =
        particles.iterator();
    while (it.hasNext()) {
      Particle p = it.next();
      p.run();
      if (p.isDead()) {
        it.remove();
      }
    }
  }
  
  //
  // emit
  // Emit a particle.
  //
  void emit() {
     addParticle(location);
  }

  //
  // isDead
  // Determines whether the Emitter has finished emitting all of
  // its Particles.
  //
  boolean isDead() {
    return(finishedEmission && (particles.size() == 0));
  }
 }