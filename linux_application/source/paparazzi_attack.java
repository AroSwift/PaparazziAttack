import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 
import java.util.*; 
import java.util.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class paparazzi_attack extends PApplet {

//
// paparazzi_attack
// by: Aaron Barlow
// game name: Paparazzi Attack
//
// The paparazzi are after you, a celebrity, and you must not
// not only defend yourself, but kill all the paparazzi
//

// Sound Libraries for music and sound effects







// Library for randomness


// Screen background image
PImage bg_iamge;

// Characters and their animation path
Celeb main_character;
Paparazzi paparazzi;
String celeb_animation_path = "MainCharacter/";
String paparazzi_animation_path = "Paparazzi/Character_1/";

// Blood Effects
Blood drawing_blood = null;

// Default screen
// 0: introduction screen
// 1: Play the game screen
// 2: Lose game screen
int game_screen = 0;

// Set the defualt offset from window in which objects should not go
int heightBuffer = 130;
int widthBuffer = 40;

// Sounds and support objects
Random generator;
AudioPlayer music;
AudioPlayer punch;
AudioPlayer kick;
AudioPlayer death;
Minim minim;

//
// setup
// Load the necessary objects, but do not display them.
// Play Bacon & Eggs music forever.
//
public void setup() {
   // Set the size of the window
   
  
  // Load music and sound effects
   minim = new Minim(this);
   // Bacon & Eggs is produced by Ethan Barlow and processed by Aaron Barlow
   music = minim.loadFile("Bacon & Eggs.mp3", 2048);
   music.loop();
   
   // Sound effects provided by SoundBible
   punch = minim.loadFile("Punch.mp3", 2048);
   kick = minim.loadFile("Kick.mp3", 2048);
   death = minim.loadFile("Death.mp3", 2048);
  
  // Create new instance of paparrazzi and celeb
   main_character = new Celeb("idle");
   paparazzi = new Paparazzi("idle");
  
   // Load the background image only once
   bg_iamge = loadImage("background_screen.jpg");
   bg_iamge.resize(width, height);
}

//
// draw
// Determines which screen the game is currently at
// By default set to 0 which is the instruction screen
//
public void draw() {
 // Detemine which state the game is in
  if(game_screen == 0) {
    // Show intro screen (instructions)
   initial_screen(); 
  } else if(game_screen == 1) {
    // Play the game!
    play_game_screen();
  } else if(game_screen == 2) {
    // Show end of game screen
    end_game_screen();
  } else {
    // Set a defualt screen just incase
    initial_screen();
  }
}

//
// keyPressed
// When a key is pressed on the the intro or end screen, play game
// When playing game, handles the actions and animations of the celeb
//
public void keyPressed() {
  // Play the game when the user is ready to play the game
  if(game_screen == 0) {
    // Starting game for the first time when any key is pressed
    game_screen = 1;
    // Set the first animation frame
    main_character.set_state("idle");
    paparazzi.set_state("idle");
  } else if(game_screen == 2) {
    // Play the game again
    reset_game();
  } else if(game_screen == 1) { // Playing game
    if( key == 'd' || key == 'D' ) {
      // Do not reset run animation if already running!
      if(main_character.state != "run") main_character.set_state("run");
    } else if( key == 'a' || key == 'A' ) {
      // Do not reset run animation if already running!
      if(main_character.state != "run") main_character.set_state("run");
    } else if( key == 'w' || key == 'W' ) {
      //main_character.set_state("jump");
    } else if( key == 'k' || key == 'K' ) {
      if(main_character.state != "kick") main_character.set_state("kick"); 
    } else if( key == 'j' || key == 'J' ) {
      if(main_character.state != "punch") main_character.set_state("punch");
    } else if(key == 'p' || key == 'P') {
      // Pause the game
       if(looping) {
         fill(0); // Set text to black
         textSize(40); // Set text size to 40
         // Inform user that game is paused
         textAlign(CENTER);
         // Put text near the top of the screen
         text("Game Paused...",width/2,(height/2)-(height*0.3f));
         text("P to continue",width/2,(height/2)-(height*0.3f)+50);
         
         // Stop the game
         noLoop();
       } else {
         // Start the game again
         loop();
       }  
    }
  }
}

//
// keyReleased
// When playing game set celeb animation to idle if user does not want to run
//
public void keyReleased() {
  // Dont do game actions if user is not ready to play game
  if(game_screen != 0 && game_screen != 2) {
    if( key == 'd' || key == 'D') {
      // Do not reset idle animation if already idling
      if(main_character.state != "idle") main_character.set_state("idle");
    } else if( key == 'a' || key == 'A') {
      // Do not reset idle animation if already idling
      if(main_character.state != "idle")  main_character.set_state("idle");
    }
  }
}

//
// initial_screen
// Introduction screen. Give user instructions for game.
//
public void initial_screen() {
   // Show user how to play the game
   // When user presses key, start the game!
   background(bg_iamge);
}

//
// play_game_screen
// Continuely set the background to white, create a floor,
// and update the celeb and paparazzi state. Show the updated
// kill count and when the celeb is dead, stop playing game.
//
public void play_game_screen() {
  background(255);
 
  // Set the floor
  fill(175);
  noStroke();
  rect(0, height-100, width, 100);
 
  // Update the totality of celeb
  main_character.update();
  main_character.display();
  
  // Update the totality of paparazzi
  paparazzi.update();
  paparazzi.display();
 
   fill(0); // Set text to black
   textSize(20); // Set text size to 20
   // Display main character's kills and account for text size
   text("Kills: " + main_character.num_kills,(width-widthBuffer-main_character.num_kills-60),BOTTOM-heightBuffer+60);
  
  // When the celeb dies, go to losing screen
  if(!main_character.alive) game_screen = 2;
}

//
// end_game_screen
// Show the end game screen.
//
public void end_game_screen() {
  // Show the losing screen
  // Play again when user presses any key
  background(bg_iamge);
}

//
// reset_game
// Reset the celeb and paparazzi and allow user to play game again.
//
public void reset_game() {
  // Reset the entire game
  game_screen = 1;
  main_character = new Celeb("idle");
  paparazzi = new Paparazzi("idle");
 
  main_character.set_state("idle");
  paparazzi.set_state("idle");
}
//
// animation
// by: Aaron Barlow
//
// Simulate animation by getting images and going to the next frame.
// Allows frames to either repeat or not repeat the sequence.
//
class Animation extends Object {
  PImage[] images;
  PVector  animation_size;
  int image_count;
  int frame;
  boolean repeat;
  String speech_text[];
  TextEmitter  text_emitter;
  
  //
  // Animation
  // Constructor for animation.
  //
  Animation(String path, int count, boolean repeat, int x, int y, int w, int h) {
    // Intialize the varables
    super(x, y, w, h);
    animation_size = new PVector(w, h);
    images = new PImage[count];
    image_count = count;
    frame = 0;
    this.repeat = repeat;
    // Load the speech file
    speech_text = loadStrings("Speech.txt");
    text_emitter = new TextEmitter(location.x,location.y-(size.y/2),300);
    
    // Load each image under the given file path into an array
    for (int i = 0; i < image_count; i++) {
     String image_name = path + "__" + nf(i, 3) + ".png";
     images[i] = loadImage(image_name);
     // Set the image size to the size of the object
     images[i].resize(PApplet.parseInt(animation_size.x), PApplet.parseInt(animation_size.y));
    }
  }
  
  public void show_speech() {
    if(speech_text == null) return;
    float random_text = random(0, speech_text.length);
    text_emitter.load(speech_text[PApplet.parseInt(random_text)]);
  }
  
  //
  // change_animation
  // Set the animation of the character to the new specified animation.
  //
  public void change_animation(String path, int count, boolean repeat) {
    // Reset the variables
    images = new PImage[count];
    image_count = count;
    frame = 0;
    this.repeat = repeat;
    
    // Load each image under the given file path into an array
    for (int i = 0; i < image_count; i++) {
     String image_name = path + "__" + nf(i, 3) + ".png";
     images[i] = loadImage(image_name);
     // Set the image size to the size of the object
     images[i].resize(PApplet.parseInt(animation_size.x), PApplet.parseInt(size.y));
    }
  }

  //
  // display_animation
  // Show the image sequence frame by frame.
  // Returns whether or not the animation is at the end.
  //
  public boolean display_animation() {
    boolean done_with_animation = false;
    
    // Set the current image to be displayed
    frame = (frame+1) % image_count;
    super.myImage = images[frame];
    
    // When at the end of animation
    if(frame+1 == image_count) {
      // Only run the animation once unless otherwise specified
       if( repeat ) {
         // Repeat the animation
         frame = 0;
       } else { // The animation is done and we do not want to repeat it
         done_with_animation = true;
       }
    }
    
    // Update text emitter location
    text_emitter.location = new PVector(location.x,location.y-(size.y/2),150);
    // Then display the text
    text_emitter.run();
    
    super.display(); // Run the super class's dispaly method
    return done_with_animation;
  }
  
}
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
      gravity = new PVector(0,0.2f);
    }
    
    //
    // run
    // Apply gravity to the blood drops.
    //
    public void run() {
      applyForce(gravity);
      super.run();
    }

}
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
      deathRate = 2.5f;
    }
    
    //
    // render
    // Draw a single drop of blood with a shape that varies a bit
    // randomly with each render.
    //
    public void render() {
      stroke(0,lifespan);
      strokeWeight(0);
      fill(150,30,30,lifespan);
      ellipse(location.x, location.y, size.x+random(-1,1),size.y+random(-1,1));
  }
}
//
// celeb
// by: Aaron Barlow
//
// Encapsilate the characteristics of the celebrity.
// The celeb can run left, right, punch, and kick paparazzi.
//
class Celeb extends Animation {
  // Default max life of character(s)
  int max_health = 100;
  HealthBar health_bar;
  int health;
  boolean alive;
  String state;
  int num_kills;
  Blood drawing_blood;
  
  //
  // Celeb
  // Constructor for celebrity character.
  //
  Celeb(String state) {
    // Character should be set in the middle of the screen
    super(paparazzi_animation_path + state, 61, true, width/2, 425, 125, 275);
    health = max_health;
    // Should start game alive
    alive = true;
    // We are not being punched or kicked
    drawing_blood = null;
    // Have not kicked anyone
    num_kills = 0;
    
    // Set the current animation state
    this.state = state;
    
    // Put a health bar in the top left corner
    PVector new_location = new PVector(0, 10);
    health_bar = new HealthBar(health, 50, new_location);
  }
  
  //
  // go
  // Move the character in the specified direction.
  //
  public void go(int x, int y) {
    // Move in the specified direction
    super.velocity = new PVector(x, y);
  }
  
  //
  // stop
  // Stop all character movement.
  //
  public void stop() {
    // Stop moving in any direction
    super.velocity = new PVector(0, 0);
    super.acceleration = new PVector(0, 0);
    super.rotation = 0;
    super.rotationVelocity = 0;
    super.rotationAcceleration = 0;
  }
  
  //
  // set_state
  // Set the animation state to idle, run, jump, punch, or kick
  //
  public void set_state(String state) {
    boolean can_repeat_animation;
    
    if(state == "idle") stop();
    if(state == "jump") go(0, -10);
    if(state == "kick" || state == "punch") {
      stop();
      
      // When celeb is within punching or kicking distance
      if( dist(main_character.location.x, main_character.location.y, paparazzi.location.x, paparazzi.location.y) <= main_character.size.x ) {
        float randomize = random(5,20);
        // Decrease the health of paparazzi because celeb is punching or kicking them
        paparazzi.hit( PApplet.parseInt(randomize) );
        paparazzi.drawing_blood = new Blood(100, paparazzi.location, 5, 8);
        
        // Play kick sound effect when kicking
        if(state == "kick") {
          kick.play();
          kick.rewind();
        }
        
        // Play punch sound effect when punching
        if(state == "punch") {
          punch.play();
          punch.rewind();
        }
        
      }
    }
    
    // When running
    if(state == "run") {
      // Move the character in the direction the celeb is running
      if(key == 'a' || key == 'A') {
         stop();
         go(-5, 0);
       } else if(key == 'd' || key == 'D') {
         stop();
        go(5, 0);
      }
    }
    
    // Determine if this current animation can be repeated
    can_repeat_animation = state == "idle" || state == "run" ? true : false;
    
    this.state = state;
    
    // Set the new animation state
    super.change_animation(celeb_animation_path + state, 61, can_repeat_animation);
  }
  
  //
  // hit
  // Decrease the health of celeb if still alive.
  // If health is below or at zero, set state to dead.
  //
  public void hit(int loss) {
    health -= loss;
    if( health <= 0 ) {
      die();
    }
  }
  
  //
  // die
  // Encapsulate logic that sets chracter's state to dead.
  // Make celeb fly off screen and play dead sound.
  //
  public void die() {
    alive = false;
    super.applyRotation(40);
    go(-100, width/2);
    
    // Play the death sound effect
    death.play();
    death.rewind();
    
    text_emitter.clear();
  }

  //
  // display
  // Show updated state of celeb:
  // Set health bar of celeb to health and update animation of celeb and 
  // Show blood if punched or kicked.
  //
  public void display() {
    boolean done_with_animation;
    done_with_animation = super.display_animation(); 
      
    if(done_with_animation) set_state("idle");

    health_bar.update(health);
    health_bar.display();
    
    // Show text above character's head
    show_speech();    
    
    if(drawing_blood != null) drawing_blood.display();
  }
 
}

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
  public void addParticle(PVector loc) {
    particles.add(new Particle(loc,size.x,size.y));
  }

  // addParticle
  // Add a Particle object at a given location. Used to 
  // add subclassed Particles.
  //
  public void addParticle(PVector loc, Particle p) {
    particles.add(p);
  }

  //
  // run
  // Emits particles at regular intervals and also
  // runs particles until they die.
  //
  public void run() {
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
  public void emit() {
     addParticle(location);
  }

  //
  // isDead
  // Determines whether the Emitter has finished emitting all of
  // its Particles.
  //
  public boolean isDead() {
    return(finishedEmission && (particles.size() == 0));
  }
 }
//
// health_bar
// by: Aaron Barlow
//
// A health bar for characters which sets the width of the bar to the health of the character.
// Health bar color changes in increments of thirds from green to orange to red.
//
class HealthBar extends Object {
  int orig_health;
  int current_health;
 
 // Constructor for when the location of the health bar needs to be specified
 HealthBar(int orig_health, int h, PVector set_location) {
   // Create a rectangle that resembles a health bar and set the width to health
   super(set_location.x, set_location.y, orig_health, h, 0, 255, 0);
   
   this.orig_health = orig_health;
   current_health = orig_health;
 }
 
 public void update(int new_health) {
   current_health = new_health;
   // Change the size of the rectangle to match the new state of health
   size.x = current_health;
   
   set_health_bar();
 }
 
 public void update(int new_health, PVector new_location) {
   current_health = new_health;
   // Change the size of the rectangle to match the new state of health
   size.x = current_health;
   location.x = new_location.x;
   location.y = new_location.y;
   
   set_health_bar();
 }
 
 public void set_health_bar() {
   // Does the given character still have more than 2/3 of health?
   if(current_health >= orig_health*0.67f) {
    // Set the health bar color to green
    super.myColor = new PVector(0,255,0);
   // Does the given character still have more than 1/3 of health?
   } else if(current_health >= orig_health*.34f) {
     // Set the health bar color to yellow/orange
     super.myColor = new PVector(255,200,0);
   } else { // Character has less than 1/3 of life
     // Set the health bar color to red
     super.myColor = new PVector(255,0,0);
   }
 }
   
 
 
  
}
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
  final   float G = 0.4f;        // force of gravity
  
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
  public void applyRotation(float rot) {
    rotationAcceleration += rot;
  }

  //
  // applyForce
  // Accumulate movement forces.
  //
  public void applyForce(PVector force) {
    //PVector f = PVector.div(force, mass);  //Take gravity into account (NOT)
    acceleration.add(force);
  }

  //
  // update
  // Implement the physics of movement.
  //
  public void update() {
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
  public void updateRotation() {
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
  public void display() {
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
  public void show() {
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
  public PVector attract(Object m) {
    PVector force = PVector.sub(location, m.location);   // Calculate direction of force
    float distance = force.mag();                        // Distance between objects
    distance = constrain(distance, 5.0f, 25.0f);           // Limiting the distance 
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
  public void enforceBoundaries() {    
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
  public boolean isWithin(float x, float y) {
    float w = size.x/2;
    float h = size.y/2;
    return ((x >= (location.x - w)) && (x <= (location.x + w)) &&
        (y >= (location.y - h)) && (y <= (location.y + h)));
  }
}
//
// paparazzi
// by: Aaron Barlow
//
// Encapsilate the characteristics of the paparazzi.
// The paparazzi can run left, right, punch, and kick paparazzi.
// The paparazzi follows the celebrity and tries to take the celeb down.
//
class Paparazzi extends Animation {
  // Default max life of character(s)
  int max_health = 75;
  HealthBar health_bar;
  int health;
  boolean alive;
  String state;
  //long time_before_regenerate = 10000;
  long time_before_regenerate = 6000;
  long respawn_time;
  Blood drawing_blood;
  
  //
  // Paparazzi
  // Constructor for paparazzi character.
  //
  Paparazzi(String state) {
    // Default pose and position of character
    super(paparazzi_animation_path + state, 61, true, 7, 425, 125, 275);
    // Health by defualt shall be 100
    health = max_health;
    // Should start boolean alive
    alive = true;
    drawing_blood = null;
    this.state = state;
    
    // Put the paparazzi as far from the celeb as possible
    generate();
    
    // Put a health bar above the paparazzi's head
    //health_bar = new HealthBar(max_health, 10, location);
    PVector new_location = new PVector(location.x, location.y+size.y);
    health_bar = new HealthBar(health, 10, new_location);
  }
  
   //
  // go
  // Move the character in the specified direction.
  //
  public void go(int x, int y) {
    velocity = new PVector(x, y);
    //applyForce(new PVector(x,y));
  }
  
  //
  // stop
  // Stop all character movement.
  //
  public void stop() {
    velocity = new PVector(0, 0);
    acceleration = new PVector(0, 0);
    super.rotation = 0;
    super.rotationVelocity = 0;
    super.rotationAcceleration = 0;
  }
  
  //
  // set_state
  // Set the animation state to idle, run, jump, punch, or kick
  //
  public void set_state(String state) {
    boolean can_repeat_animation;
    this.state = state;
    
    // Set the current animation state to the given animation state
    if(state == "idle") stop();
    if(state == "jump") go(0, -10);
    if(state == "kick" || state == "punch") {
      main_character.hit( PApplet.parseInt(random(3, 10)) );
      main_character.drawing_blood = new Blood(100, main_character.location, 5, 8);
      stop();
            
      // Play kicking sound when kicking
      if(state == "kick") {
        kick.play();
        kick.rewind();
      }
      
      // Play punching sound when punching
      if(state == "punch") {
        punch.play();
        punch.rewind();
      }
    }
    
    // Determine if this current animation can be repeated
    can_repeat_animation = state == "idle" || state == "run" ? true : false;
    
    // Set the new animation state
    super.change_animation(paparazzi_animation_path + state, 61, can_repeat_animation);
  }
  
  //
  // hit
  // Decrease the health of paparazzi if still alive.
  // If health is below or at zero, set state to dead.
  //
  public void hit(int loss) {
    health -= loss;
    if( health <= 0 && alive ) {
      die();
    }
  }
  
  //
  // die
  // Encapsulate logic that sets chracter's state to dead.
  // Make celeb fly off screen and play dead sound.
  //
  public void die() {
    alive = false;
    super.applyRotation(20);
    go(-50, width/2);
    
    // Celeb got another kill
    main_character.num_kills++;
    
    // Play the death sound effect
    death.play();
    death.rewind();
    
    // Set the time when this paparazzi should regenerate
    respawn_time = millis() + time_before_regenerate;
    
    text_emitter.clear();
  }
  
  
  //
  // generate
  // Place the paparazzi farthest from the celeb. Set the celeb
  // alive, stop all motion, set health to max, and set animation to idle.
  //
  public void generate() {
    alive = true;
    stop();
    drawing_blood = null;
    health = max_health;
    set_state("idle");
    
    float celeb_distance_from_left = dist(main_character.location.x, main_character.location.y, 0, height);
    float celeb_distance_from_right = dist(main_character.location.x, main_character.location.y, width, height);
    
    if( celeb_distance_from_left > celeb_distance_from_right) {
      location = new PVector(widthBuffer, 425);
    } else {
      location = new PVector(width-widthBuffer, 425);
    }
  }
  
  //
  // enforceBoundaries
  // Overide super class's enforcement of boundries when dead 
  // otherwise use super class's boundary enforcement
  //
  public void enforceBoundaries() {
    if(alive) {
      super.enforceBoundaries();
    } else {
      if (location.x <= -size.x/2) {
         location.y = width;
      }
        
      if (location.y <= -size.y/2) {
         location.y = height;
      }
    }
  }
  
  //
  // display
  // Show updated state of paparazzi:
  // Set health bar of paparazzi to health and update animation of paparazzi.
  // When celeb is away from paparazzi, paparazzi should follow the celeb.
  // Once paparazzi is within punching and kicking distance, commence randomly
  // Punching and kicking. While paparazzi is not within punching and kicking
  // Distance, paparazzi should run towards celeb. Show blood if punched or kicked.
  //
  public void display() {
     if(!alive) {
        // Show drawing blood
        if(drawing_blood != null) drawing_blood.display();
        super.text_emitter.clear();
       
        if(millis() >= respawn_time) {
          generate();
        } else {
          super.display();
          return;
        }
     }
     boolean done_with_animation;
     done_with_animation = super.display_animation(); 
      
     if(done_with_animation) set_state("idle");
     
     // Define the actions that the simulate the paparazzi following the celebrity
     if( dist(main_character.location.x, main_character.location.y, paparazzi.location.x, paparazzi.location.y) <= main_character.size.x ) {
       float randomize = random(20);
      
       paparazzi.stop();
      
       if( (randomize > 10 && done_with_animation) || state == "run" ) {
         paparazzi.set_state("punch");
       } else if( (randomize <= 10 && done_with_animation) || state == "run" ) {
         paparazzi.set_state("kick");
       }
    } else {
       PVector f = main_character.attract(paparazzi);
       f.mult(0.00000007f);
       paparazzi.applyForce(f); 
       if(state != "run") paparazzi.set_state("run");
    }
    
    PVector new_location = new PVector(location.x, location.y+heightBuffer+(-size.y));    
    health_bar.update(health, new_location);
    health_bar.display();
    
    // Show text above character's head
    show_speech();    
    
    if(drawing_blood != null) drawing_blood.display();
  }
 
}
//
// particle
// by: Dr. Brown
// Partilce
// (adapted from example in "The Nature of Code")
//
class Particle extends Object {
  float lifespan = 255.0f;   // life and transparency of particle
  float deathRate = 3.0f;    // how much is subtracted each draw

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
    float vx = (float) generator.nextGaussian()*0.3f;
    float vy = (float) generator.nextGaussian()*0.3f - 1.0f;
    velocity = new PVector(vx,vy);
    myImage = img;
  }
  
  //
  // run
  // Update physics and lifespan, then render particle.
  //
  public void run() {
    update();
    render();
  }
 
 //
 // update
 // Draw and update life.
 //
  public void update() {
    super.update();
    lifespan -= deathRate;
  }
 
 //
 // render
 // Draw the particle.
 //
  public void render() {
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
  public boolean isDead() {
    if (lifespan < 0.0f) {
      return true;
    } else {
      return false;
    }
  }
}
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
  public void display() {
    update();
    updateRotation();
    run();
  }

  //
  // run
  // Run the ParticleSystem. Tell each Particle to run.
  //
  public void run() {
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
  public void applyForce(PVector dir) {
    // Enhanced loop!!!
    for (Particle p: particles) {
      p.applyForce(dir);
    }
  
  }  

  //
  // addParticle
  // Add a Particle to the ParticleSytem.
  //
  public void addParticle() {
    particles.add(new Particle(location, size.x, size.y));
  }

  //
  // addParticle
  // Add a Particle object to the ParticleSytem. Used to
  // add subclassed Particles.
  //
  public void addParticle(Particle p) {
    particles.add(p);
  }
 
  //
  // isDead
  // Test if the particle system still has particles.
  //
  public boolean isDead() {
    if (particles.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

}

//
// TextEmitter
// by: Dr. David Brown
//
// Emits text from a loaded string at regular intervals.
//
class TextEmitter extends Emitter {
  String[]             words;
  int                  nextWord = 0;
  
  TextEmitter(float x, float y, float rate) {
    super(x, y, 0, 0, rate);
    words = new String[0];
  }
  
  public void load(String s) {
    words = s.split(" ");
    nextWord = 0;
    finishedEmission = false;
  }
  
  public void clear() {
    words = new String[0];
    nextWord = 0;
    finishedEmission = true;
  }
  
  public void addParticle(PVector loc, String s) {
    particles.add(new TextParticle(loc, s));
  }
  
  public void run() {
     if (!finishedEmission && (millis() > nextEmission)) {
       if (nextWord >= words.length)
          finishedEmission = true;
       else {
          addParticle(location, words[nextWord++]);
          nextEmission = millis() + emitRate;
       }
     }
     super.run();
  }

  public boolean notEmitting() {
    return(nextWord >= words.length);
  }
  
}
//
// TextParticle
// by: Dr. David Brown
//
// A "text bubble" Particle.
//
class TextParticle extends Particle {
  String myText;  // text to display in bubble
  
  //
  // TextParticle
  // Constructor. Create a TextParticle at loc with 
  // string s. Cause it to float upwards.
  //
  TextParticle(PVector loc, String s) {
    super(loc,0,0);
    acceleration = new PVector(0,-random(0.01f,0.1f));
    velocity = new PVector(random(-1,1),random(-1,0));
    myText = s;
    size.x = textWidth(myText) + 20;
    size.y = 40;
  }
  
  //
  // render
  // Draw TextParticle as text inside of an ellipse.
  //
  public void render() {
    stroke(0,lifespan);
    strokeWeight(1);
    fill(255,lifespan);
    float w = textWidth(myText);
    ellipse(location.x+(size.x-20)/2,location.y-5, size.x, size.y);
    fill(0,lifespan);
    text(myText, location.x,location.y);
  }
}
  public void settings() {  size(1200,600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "paparazzi_attack" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
