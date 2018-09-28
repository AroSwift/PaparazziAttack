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
 
 void update(int new_health) {
   current_health = new_health;
   // Change the size of the rectangle to match the new state of health
   size.x = current_health;
   
   set_health_bar();
 }
 
 void update(int new_health, PVector new_location) {
   current_health = new_health;
   // Change the size of the rectangle to match the new state of health
   size.x = current_health;
   location.x = new_location.x;
   location.y = new_location.y;
   
   set_health_bar();
 }
 
 void set_health_bar() {
   // Does the given character still have more than 2/3 of health?
   if(current_health >= orig_health*0.67) {
    // Set the health bar color to green
    super.myColor = new PVector(0,255,0);
   // Does the given character still have more than 1/3 of health?
   } else if(current_health >= orig_health*.34) {
     // Set the health bar color to yellow/orange
     super.myColor = new PVector(255,200,0);
   } else { // Character has less than 1/3 of life
     // Set the health bar color to red
     super.myColor = new PVector(255,0,0);
   }
 }
   
 
 
  
}