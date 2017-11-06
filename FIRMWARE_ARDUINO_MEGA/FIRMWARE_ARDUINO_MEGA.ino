#include <Wire.h>
#define START_PIN 23
#define END_PIN 53
#define i2C_ADDRESS 21


char states[30];

void setup() {
  Wire.begin(i2C_ADDRESS);              
  Wire.onReceive(receiveEvent);
  Wire.onRequest(requestEvent);
  for (int i = START_PIN ; i < END_PIN; i++){
    pinMode(i, OUTPUT);
    digitalWrite(i,LOW);
    states[i-START_PIN] = '0';
  }  
}

void loop() {
  
}


void receiveEvent(int howMany) {
  int x = Wire.read();
  if(x >START_PIN  && x < END_PIN){
    bool state = !digitalRead(x);
    digitalWrite(x,state);
    states[x-START_PIN] = state ? '1' : '0';
  }
}

void requestEvent() {
  Wire.write(states);
}
