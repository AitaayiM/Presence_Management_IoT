#include <SPI.h>
#include <MFRC522.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>

#define SS_PIN D2
#define RST_PIN D1

MFRC522 mfrc522(SS_PIN, RST_PIN);
String uid;

const char* ssid = "   ";
const char* password = "Alfa@212";
const char* serverIP = "192.168.1.110";
const int serverPort = 1234; // Le port sur lequel votre application Java écoute les connexions

WiFiClient client;

void setup() {
  Serial.begin(115200);
  SPI.begin();
  mfrc522.PCD_Init();
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  if (mfrc522.PICC_IsNewCardPresent()) {
    if (mfrc522.PICC_ReadCardSerial()) {
      uid = "";
      for (byte i = 0; i < mfrc522.uid.size; i++) {
        uid.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? "0" : ""));
        uid.concat(String(mfrc522.uid.uidByte[i], HEX));
      }
      mfrc522.PICC_HaltA();
      sendDataToJavaApp(uid);
      delay(1000);
    }
  }
}

void sendDataToJavaApp(String data) {
  if (client.connect(serverIP, serverPort)) {
    Serial.println("Connected to Java application");
    
    // Envoie des données à l'application Java
    client.println(data);
    client.flush();
    
    // Attente de la réponse de l'application Java
    while (client.connected()) {
      if (client.available()) {
        String response = client.readStringUntil('\n');
        Serial.println("Response from Java application: " + response);
        break;
      }
    }
    
    // Fermeture de la connexion
    client.stop();
    Serial.println("Disconnected from Java application");
  }
}
