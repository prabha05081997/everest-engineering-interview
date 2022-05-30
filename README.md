# everest-engineering interview

* Clone the project by **git clone https://github.com/prabha05081997/everest-engineering-interview.git**
* In root folder run **mvn clean package** and ensure every tests are getting passed

### Coupon
* Coupons are read from **coupons.csv** file which is placed in root folder of the application.
* It's path is read from **app.properties** file. so it can be placed anywhere in the system, not
necessarily in the root folder of the application.
* New coupons can be added to coupons.csv file then restart the sytem. No code changes are required

## Cost Estimation Service

* Inside cost-estimation module, Go to **cost-estimation/target** folder
* Run the following command **java -jar cost-estimation-1.0-SNAPSHOT.jar**
to start the application and give inputs in below format

#### Sample input

100 3\
PKG1 5 5 OFR001\
PKG2 15 5 OFR002\
PKG3 10 100 OFR003

#### Expected output

PKG1 0 175\
PKG2 0 275\
PKG 35 665

## Delivery time estimation service

* Inside delivery-time-estimation module, Go to **delivery-time-estimation/target** folder
* Run the following command **java -jar delivery-time-estimation-1.0-SNAPSHOT.jar**
to start the application and give inputs in below format

#### Sample input

100 5\
PKG1 50 30 OFR001\
PKG2 75 125 OFR0008\
PKG3 175 100 OFR003\
PKG4 110 60 OFR002\
PKG5 155 95 NA\
2 70 200

#### Expected Ouput

PKG1 0 750 3.98\
PKG2 0 1475 1.78\
PKG3 0 2350 1.42\
PKG4 105 1395 0.85\
PKG5 0 2125 4.19
