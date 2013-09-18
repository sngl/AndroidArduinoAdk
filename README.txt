Description:

The class ArduinoAdk manages all the stuff to create the connection with the usb accessory. 
You only have to use ArduinoAdk.Connect() to open the accessory.
ArduinoAdk.write(int) to send a byte to the accessory. 

!!!ArduinoAdk.read() is implemented but not tested!!!

Other Function:

int available();
String getStatus();
boolean isConnected();


Usage:

Declare a private variable in your main Activity class and initialize the class in the onCreate function with:

mArduinoAdk = new ArduinoAdk(this);



Supported Android API: minimum version 12