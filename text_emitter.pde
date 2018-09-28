import java.util.*;
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
  
  void load(String s) {
    words = s.split(" ");
    nextWord = 0;
    finishedEmission = false;
  }
  
  void clear() {
    words = new String[0];
    nextWord = 0;
    finishedEmission = true;
  }
  
  void addParticle(PVector loc, String s) {
    particles.add(new TextParticle(loc, s));
  }
  
  void run() {
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

  boolean notEmitting() {
    return(nextWord >= words.length);
  }
  
}