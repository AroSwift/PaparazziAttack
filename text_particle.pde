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
    acceleration = new PVector(0,-random(0.01,0.1));
    velocity = new PVector(random(-1,1),random(-1,0));
    myText = s;
    size.x = textWidth(myText) + 20;
    size.y = 40;
  }
  
  //
  // render
  // Draw TextParticle as text inside of an ellipse.
  //
  void render() {
    stroke(0,lifespan);
    strokeWeight(1);
    fill(255,lifespan);
    float w = textWidth(myText);
    ellipse(location.x+(size.x-20)/2,location.y-5, size.x, size.y);
    fill(0,lifespan);
    text(myText, location.x,location.y);
  }
}