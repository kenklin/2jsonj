2jsonj
======

CSV to JSON REST Gateway

The **2json** web service transforms various data formats (currently CSV) to JSON so that web clients
can more easily make use of the data.


Philadelphia Red Light Camera Locations
---------------------------------------
To demonstrate the **2json** web service, I created a Philadelphia Red Light Camera Locations web page at
http://kenlin.com/x/2json/philadelphia-red-light-camera-locations.html 
(source: [here](https://github.com/kenklin/2jsonj/blob/master/WebContent/WEB-INF/philadelphia-red-light-camera-locations.html)).
The page performs the following steps:
- Passes the CSV's URL, 
<a href="https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv" target="_blank">
red-light-camera-locations.csv</a> to the **2json** gateway.  The latter is currently at
[http://p1software-eb1.elasticbeanstalk.com/csv2json/api?url=*csvurl*]
(http://p1software-eb1.elasticbeanstalk.com/csv2json/api).
- With the JSON data that was transformed by the **2json** web service, each of the "Intersection" strings,
like "Roosevelt Blvd @ Grant Ave", to the Google Maps API to obtain their latitude and longitude.
The latter are then made into Markers.
- The Google Maps API called one more time to place the Markers onto a map of Philadelphia.
