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
  void go(int x, int y) {
    velocity = new PVector(x, y);
    //applyForce(new PVector(x,y));
  }
  
  //
  // stop
  // Stop all character movement.
  //
  void stop() {
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
  void set_state(String state) {
    boolean can_repeat_animation;
    this.state = state;
    
    // Set the current animation state to the given animation state
    if(state == "idle") stop();
    if(state == "jump") go(0, -10);
    if(state == "kick" || state == "punch") {
      main_character.hit( int(random(3, 10)) );
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
  void hit(int loss) {
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
  void die() {
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
  void generate() {
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
  void enforceBoundaries() {
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
  void display() {
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
       f.mult(0.00000007);
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