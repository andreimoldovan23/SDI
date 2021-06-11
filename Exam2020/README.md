In a factory, several sensors are attached to different pieces of equipment\
They take various measurements regarding the way they work.\
All sensors are continuously sending data (sensor
info, measurement info etc) to a server using tcp sockets.\
The tcp-socket-server processes the received data and saves the processed data to a
database.\
A factory employee may see the data from each sensor in a web interface and may take
appropriate actions.\
The sensor-app reads the following data from the console: sensor-name, sensor-id, lower-bound, upper-bound.\
Then the sensor-app will continuously generate numbers in the interval
(lower-bound, upper-bound) representing measurements of a certain piece of equipment and,
after each measurement, the following information is sent to the server-app using tcp-sockets:
sensor-name, sensor-id, measurement.\
The server-app concurrently processes information from sensors.\
For each received measurement, the server-app saves only the following information in a
database table called sensor: sensor-name, measurement, time.\
The sensor table has an id which is automatically incremented.\
The time represents the system current time in milliseconds.\
When visiting the address http://localhost:4200 the following elements are shown
- A list of sensor names (one sensor-name per line); each name appears only once
- A “Show” button, below the list
  When pressing the “Show” button, at the same address, the following elements are shown for
  each sensor:
- A label “Sensor name: ”, followed by the <sensor-name>, followed by a “Stop” button
  (same line)
- A list with data corresponding to <sensor-name> (id, sensor-name, measurement, time);\
  the list is sorted in descending order by time; only 4 elements are shown\
  The elements from above are dynamically created according to the number of sensors\
  The sensor data (from each list) is updated automatically at regular intervals as new data is
  produced by the sensor-app (page reload not allowed)\
  When pressing the “Stop” button from any sensor, the server-app is notified (a message is
  printed in the server-app console) and the corresponding sensor-app finishes execution (being
  notified by the server-app)