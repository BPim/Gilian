//아두이노 스케치 LED 켜기 간단한 테스트 


// 1초동안 깜빡 깜빡 blink
int led = 9; //LED 9번 핀을 사용 

void setup(){
  pinMode(led, OUTPUT);
}

void loop(){
  digitalWrite(led, HIGH);  // LED ON
  delay(1000);
  digitalWrite(led, LOW);  // LED OFF
  delay(1000);
}



/*
#include <stdio.h>
void main()
{
  printf("HIHIHIHIHI");
}
*/
