#include <Wire.h>
/**
 * Podem ser ligados relés a partir do pino 1 até ao pino 53
 */
char states[30];

void setup() {
  Wire.begin(21);              
  Wire.onReceive(receiveEvent);
  Wire.onRequest(requestEvent);
  Serial.begin(9600); 
  for (int i = 23 ; i < 54; i++){
    pinMode(i, OUTPUT);
    digitalWrite(i,LOW);
    Serial.println(i);
    states[i-23] = '0';
  }  
}

void loop() {
  
}


void receiveEvent(int howMany) {
  int x = Wire.read();
  if(x >22  && x < 54){
    bool state = !digitalRead(x);
    digitalWrite(x,state);
    states[x-1] = state ? '1' : '0';
    Serial.println(x);
  }else{
    Serial.println("Invalid Command");
    Serial.println(x);
  }
}

void requestEvent() {
  Wire.write(states);
}
