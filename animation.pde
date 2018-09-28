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
     images[i].resize(int(animation_size.x), int(animation_size.y));
    }
  }
  
  void show_speech() {
    if(speech_text == null) return;
    float random_text = random(0, speech_text.length);
    text_emitter.load(speech_text[int(random_text)]);
  }
  
  //
  // change_animation
  // Set the animation of the character to the new specified animation.
  //
  void change_animation(String path, int count, boolean repeat) {
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
     images[i].resize(int(animation_size.x), int(size.y));
    }
  }

  //
  // display_animation
  // Show the image sequence frame by frame.
  // Returns whether or not the animation is at the end.
  //
  boolean display_animation() {
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
