//
// Blood
// by: Dr. David Brown
//
// Implements a nasy "blood bag" splatter particle system.
// It loads the particles on creation only and then explodes
// outward with a one-time velocity and then the particles
// have gravity applied throughout their life.
//
class Blood extends ParticleSystem {
    PVector gravity;
    
    //
    // Blood
    // Constructor. Create a blood splatter bag containing num
    // particles of size w,h at loc.
    //
    Blood(int num, PVector loc, int w, int h) {
      super(num,loc,w,h);
      for(int i = 0; i < particlesToAdd; i++)
        addParticle(new BloodParticle(location, size.x,size.y));
      gravity = new PVector(0,0.2);
    }
    
    //
    // run
    // Apply gravity to the blood drops.
    //
    void run() {
      applyForce(gravity);
      super.run();
    }

}