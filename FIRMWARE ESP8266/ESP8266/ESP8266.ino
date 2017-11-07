//Wi-Fi Manger library
#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include <WiFiManager.h>//https://github.com/tzapu/WiFiManager
#include "Wire.h"
#include <ESP8266WiFi.h>
#include <ESP8266mDNS.h>

#define WIRE_SDA 0
#define WIRE_SCL 2
const String HOSTNAME  = "relaycontroller";
// TCP server at port 80 will respond to HTTP requests
WiFiServer server(80);

void setup(void){ 
  Wire.begin(WIRE_SDA,WIRE_SCL);
  WiFiManager wifiManager;
  //reset saved settings
  //wifiManager.resetSettings();
  /*define o tempo limite até o portal de configuração ficar novamente inátivo,
   útil para quando alteramos a password do AP*/
  wifiManager.setTimeout(60);
  wifiManager.autoConnect(HOSTNAME.c_str());
  // Configuração do mDNS :
  // para aceder é ao ESP podemos utilizar o mDNS "HOSTNAME.local"
  //Exemplo http://relaycontroller.local
  if (!MDNS.begin(HOSTNAME.c_str())) {
    Serial.println("Error setting up MDNS responder!");
    while(1) { 
      delay(1000);
    }
  }
  // Iniciar o serviço HTTP
  server.begin();
  // Adicionar o serviço MDNS-SD
  MDNS.addService("http", "tcp", 80);
}

void loop(void)
{

  WiFiClient client = server.available();
  if (!client) {
    return;
  }

  // Wait for data from client to become available
  while(client.connected() && !client.available()){
    delay(1);
  }
  
  // Read the first line of HTTP request
  String req = client.readStringUntil('\r');
  
  // First line of HTTP request looks like "GET /path HTTP/1.1"
  // Retrieve the "/path" part by finding the spaces
  int addr_start = req.indexOf(' ');
  int addr_end = req.indexOf(' ', addr_start + 1);
  if (addr_start == -1 || addr_end == -1) {
    return;
  }
  req = req.substring(addr_start + 1, addr_end);
  client.flush();
  String s = "";
  if (req != "/"){
    req.replace("/","");
    int a = req.toInt();
    Wire.beginTransmission(21);
    Wire.write(a); // one must mean something to the mega, 
    Wire.endTransmission();
    s = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
    Wire.requestFrom(21,30);
    while (Wire.available()){
      //lê os bytes como caracter
      char a =  Wire.read();
      s+=a;
    }
  }
  client.print(s);
}

