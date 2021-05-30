#include <Adafruit_TFTLCD.h>
#include <registers.h>
#include <pin_magic.h>

#include <SoftwareSerial.h>

#include <Adafruit_SSD1306.h>
#include <splash.h>
#include <gfxfont.h>
#include <Adafruit_GFX.h>
#include <Stepper.h>

#define OLED_ADDR 0x3C
#define MAXWIDTH 127
#define MAXHEIGHT 31

#define STEPS 200

enum ETAT_PROJET{
  MANIPULATION = 0,
  PET = 1,
};

enum ETAT_PROJET_SUIVRE{
  ATTAPPAIRANGE = 0,
  SCAN = 1,
  MOUVEMENT = 2,
  OBSTACLE = 3,
  ARRIVE = 4,
};

Adafruit_SSD1306 display(-1);

const int trigPin = 6;
const int echoPin = 7;
const int Led1 = 3;
const int buzzer = 2;

const int moteurA1 = 13;
const int moteurA2 = 12;
const int moteurB1 = 10;
const int moteurB2 = 11;

bool changeDirection = false; 
bool obstacle = false;
bool doneInstruction = false; 

long duration;
int distance;

int etatRobot;

SoftwareSerial mySerial(2, 4); // RX, TX

void setup() {
  etatRobot = MANIPULATION;
  //Setup du Serial
  Serial.begin(9600);
  mySerial.begin(9600);
  //Setup de l'écran
  display.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  display.clearDisplay();
  display.display();
  //Setup du capteur
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT);
  //Setup de la Led
  pinMode(Led1, OUTPUT);
  //Setup du capteur bluetooth
//  mySerial.begin(9600);
  //Setup moteur A
  pinMode( moteurA1, OUTPUT );
  pinMode( moteurA2, OUTPUT );
  digitalWrite( moteurA1, LOW );
  digitalWrite( moteurA2, LOW ); 
  //Setup moteur B
  pinMode( moteurB1, OUTPUT );
  pinMode( moteurB2, OUTPUT );
  digitalWrite( moteurB1, LOW );
  digitalWrite( moteurB2, LOW );
  displayStandBy();
  //
}

byte bigVal[] = {0};

String instruction = "";

int position = 0;

void resetMotor(){
   digitalWrite( moteurA1, LOW );
   digitalWrite( moteurA2, LOW  );
   digitalWrite( moteurB1, LOW );
   digitalWrite( moteurB2, LOW  );
   delay(300);
}
void avancer(){
  digitalWrite( moteurA1, HIGH );
  digitalWrite( moteurA2, LOW  );
  digitalWrite( moteurB1, LOW );
  digitalWrite( moteurB2, HIGH  );
}
void reculer(){
  digitalWrite( moteurA1, LOW );
  digitalWrite( moteurA2, HIGH  );
  digitalWrite( moteurB1, HIGH );
  digitalWrite( moteurB2, LOW  ); 
}
void droite(){
  digitalWrite( moteurA1, HIGH );
  digitalWrite( moteurA2, LOW  );
  digitalWrite( moteurB1, LOW );
  digitalWrite( moteurB2, LOW  );
}
void gauche(){
  digitalWrite( moteurA1, LOW );
  digitalWrite( moteurA2, LOW  );
  digitalWrite( moteurB1, LOW );
  digitalWrite( moteurB2, HIGH  );
}

void displaySmile(){
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(MAXWIDTH/3, MAXHEIGHT/2);
  display.print("^w^");
  display.display();
}
void displayStandBy(){
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(MAXWIDTH/3, MAXHEIGHT/2);
  display.print("-w-");
  display.display();
}

void loop() {

  while (etatRobot == MANIPULATION){

    digitalWrite(trigPin, HIGH);
    digitalWrite(trigPin, LOW);
    duration = pulseIn(echoPin, HIGH);
    distance= duration*0.034/2;
    if(distance<10){
      obstacle = true;
      resetMotor(); 
      reculer();
      delay(200);
      resetMotor();
      displayStandBy();
      mySerial.println("ST\n");
      }
    
    if(mySerial.available() > 0){
      char dataStock = mySerial.read();
      //Serial.println(String(dataStock));
      instruction = String (dataStock);
      doneInstruction = true;
    }

    if(instruction != "" && doneInstruction == true){ // Si recoit data // Mode
            if(instruction[0] == 'F') { // Avancer
                resetMotor();
                avancer();
                displaySmile();
                mySerial.println("SM\n");
                //delay(300);
            }
            if(instruction[0] == 'R') { // Droite
                resetMotor();
                droite();
                displaySmile();
                mySerial.println("SM\n");
                //delay(300);
            }
            if(instruction[0] == 'L') { // Gauche
                resetMotor();
                gauche();
                displaySmile();
                mySerial.println("SM\n");
                //delay(300);
            }
            if(instruction[0] == 'B') { // Arriere
                resetMotor();
                reculer();
                displaySmile();
                mySerial.println("SM\n");
                //delay(300);
            }
            if(instruction[0] == 'S') { // Stop
                resetMotor();
                displayStandBy();
                mySerial.println("ST\n");
            }
         if (instruction[0] == '2')
         {
            //etatRobot = PET;
         }
         instruction = "";
         doneInstruction = false;
    }
  }
}
