//
// particle_system
// by: Dr. Brown
// ParticleSytem
// (adapted from example in "The Nature of Code")
//
class ParticleSystem extends Object {
  ArrayList<Particle> particles;    // An arraylist for all the particles
  int     particlesToAdd;           // number of particle to add each run
  
  //
  // ParticleSystem
  // Constructor for drawing-based ParticleSystems.
  //
  ParticleSystem(int num, PVector loc, int w, int h) {
    super(loc.x,loc.y,w,h);
    particlesToAdd = num;
    particles = new ArrayList<Particle>();              // Initialize the arraylist
  }

  //
  // ParticleSystem
  // Constructor for image-based ParticleSystems.
  //
  ParticleSystem(int num, PVector loc, PImage img) {
    super(loc.x,loc.y,img.width,img.height);
    particlesToAdd = num;
    particles = new ArrayList<Particle>();              // Initialize the arraylist
    myImage = img;
  }
  
  //
  // display
  // Override display to not enforce boundaries and to
  // call run each time.
  //
  void display() {
    update();
    updateRotation();
    run();
  }

  //
  // run
  // Run the ParticleSystem. Tell each Particle to run.
  //
  void run() {
    Iterator<Particle> it = particles.iterator();
    while (it.hasNext()) {
      Particle p = it.next();
      p.run();
      if (p.isDead()) {
        it.remove();
      }
    }
  }
  
  //
  // applyForce
  // Add a force vector to all particles currently in the system.
  //
  void applyForce(PVector dir) {
    // Enhanced loop!!!
    for (Particle p: particles) {
      p.applyForce(dir);
    }
  
  }  

  //
  // addParticle
  // Add a Particle to the ParticleSytem.
  //
  void addParticle() {
    particles.add(new Particle(location, size.x, size.y));
  }

  //
  // addParticle
  // Add a Particle object to the ParticleSytem. Used to
  // add subclassed Particles.
  //
  void addParticle(Particle p) {
    particles.add(p);
  }
 
  //
  // isDead
  // Test if the particle system still has particles.
  //
  boolean isDead() {
    if (particles.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

}