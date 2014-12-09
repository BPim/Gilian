// Bluetooth & arduino LED connection Test !!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
 #include "Adafruit_NeoPixel.h" 
 #define PIN            6 
 #define NUMPIXELS      31
 

 Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800); 


int delayval = 500; // delay for half a second 


 void setup() { 
   pixels.begin(); // This initializes the NeoPixel library. 
   Serial.begin(9600); 
 } 
 

 void loop() { 
   // For a set of NeoPixels the first NeoPixel is 0, second is 1, all the way up  the count of pixels minus one. 
   byte cmd; 
   cmd = Serial.read(); 
    
   if(cmd == '1') // 스마트폰으로 1을 보냈을 때 0, 150, 0 초록을 보냄 
   { 
for(int i=0;i<NUMPIXELS;i++) 
     { 
       // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255 
       pixels.setPixelColor(i, pixels.Color(0,150,0)); // Moderately bright green lor. 
       pixels.show(); // This sends the updated pixel color to the hardware. 
       //delay(delayval); // Delay for a period of time (in milliseconds). 
     } 
   } 
    
   if(cmd == '0') // 스마트폰으로 0을 보냈을 때 150, 0, 0 빨강을 보냄  
   { 
     for(int i=0;i<NUMPIXELS;i++) 
    { 
       // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255 
       pixels.setPixelColor(i, pixels.Color(150,0,0)); // Moderately bright green color. 
       pixels.show(); // This sends the updated pixel color to the hardware. 
       //delay(delayval); // Delay for a period of time (in milliseconds). 
    } 
   }
  if(cmd == '2')
   {
      for(int i=0;i<NUMPIXELS;i++) 
    { 
       // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255 
       pixels.setPixelColor(i, pixels.Color(0,0,0)); // Moderately bright green color. 
       pixels.show(); // This sends the updated pixel color to the hardware. 
       //delay(delayval); // Delay for a period of time (in milliseconds). 
    } 
   } 
    if(cmd == '3')
   {
      for(int i=0;i<NUMPIXELS;i++) 
    { 
       // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255 
       pixels.setPixelColor(i, pixels.Color(0,0,150)); // Moderately bright green color. 
       pixels.show(); // This sends the updated pixel color to the hardware. 
       //delay(delayval); // Delay for a period of time (in milliseconds). 
    } 
   } 
    if(cmd == '4')
   {
      for(int i=0;i<NUMPIXELS;i++) 
    { 
       // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255 
       pixels.setPixelColor(i, pixels.Color(75,75,75)); // Moderately bright green color. 
       pixels.show(); // This sends the updated pixel color to the hardware. 
       //delay(delayval); // Delay for a period of time (in milliseconds). 
    } 
    delay(delayval*6);
    for(int i=0;i<NUMPIXELS;i++) 
    { 
       // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255 
       pixels.setPixelColor(i, pixels.Color(0,0,0)); // Moderately bright green color. 
       pixels.show(); // This sends the updated pixel color to the hardware. 
       //delay(delayval); // Delay for a period of time (in milliseconds). 
    } 
   } 
 } 

