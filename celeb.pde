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
  void go(int x, int y) {
    // Move in the specified direction
    super.velocity = new PVector(x, y);
  }
  
  //
  // stop
  // Stop all character movement.
  //
  void stop() {
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
  void set_state(String state) {
    boolean can_repeat_animation;
    
    if(state == "idle") stop(); //<>//
    if(state == "jump") go(0, -10); //<>//
    if(state == "kick" || state == "punch") {
      stop();
      
      // When celeb is within punching or kicking distance
      if( dist(main_character.location.x, main_character.location.y, paparazzi.location.x, paparazzi.location.y) <= main_character.size.x ) {
        float randomize = random(5,20);
        // Decrease the health of paparazzi because celeb is punching or kicking them
        paparazzi.hit( int(randomize) );
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
  void hit(int loss) {
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
  void die() {
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
  void display() {
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