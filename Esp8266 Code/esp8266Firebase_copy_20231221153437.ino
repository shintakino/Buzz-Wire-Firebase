#include <Arduino.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>

#include "addons/TokenHelper.h"

#include "addons/RTDBHelper.h"

// Firebase and Wi-Fi credentials
#define WIFI_SSID "" Wifi Name
#define WIFI_PASSWORD "" // Wifi Password key
#define API_KEY "" // Data Base API KEY
#define DATABASE_URL "" // Database UrL

FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

const int wirePin = 2;    // Pin to which the buzz wire is connected

void setup() {
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;

  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("Firebase signup successful");
  } else {
    Serial.printf("Signup error: %s\n", config.signer.signupError.message.c_str());
  }

  config.token_status_callback = tokenStatusCallback; // From addons/TokenHelper.h
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  pinMode(wirePin, INPUT); // Set the wire pin as input
}

void loop() {
  int wireValue = digitalRead(wirePin); // Read the wire's value

  if (wireValue == LOW) {
    // Wire is touched
    Serial.println("Wire Touched!");
    // Send boolean value to Firebase
    if (Firebase.RTDB.setBool(&fbdo, "wireStatus", true)) {
      Serial.println("Wire status sent to Firebase: Touched");
    } else {
      Serial.println("Failed to send wire status to Firebase");
    }
  } 

  delay(100); // Add a delay to avoid rapid readings and debounce the wire
	// You can Remove the delay
}
