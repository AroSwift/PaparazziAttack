//
// paparazzi_attack
// by: Aaron Barlow
// game name: Paparazzi Attack
//
// The paparazzi are after you, a celebrity, and you must not
// not only defend yourself, but kill all the paparazzi
//

// Sound Libraries for music and sound effects
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

// Library for randomness
import java.util.*;

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
void setup() {
   // Set the size of the window
   size(1200,600);
  
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
void draw() {
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
void keyPressed() {
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
         text("Game Paused...",width/2,(height/2)-(height*0.3));
         text("P to continue",width/2,(height/2)-(height*0.3)+50);
         
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
void keyReleased() {
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
void initial_screen() {
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
void play_game_screen() {
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
void end_game_screen() {
  // Show the losing screen
  // Play again when user presses any key
  background(bg_iamge);
}

//
// reset_game
// Reset the celeb and paparazzi and allow user to play game again.
//
void reset_game() {
  // Reset the entire game
  game_screen = 1;
  main_character = new Celeb("idle");
  paparazzi = new Paparazzi("idle");
 
  main_character.set_state("idle");
  paparazzi.set_state("idle");
}
