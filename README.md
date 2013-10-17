2jsonj
======

CSV to JSON REST Gateway

The **2json** web service transforms various data formats (currently CSV) to JSON so web clients
can more easily make use of the data.


Philadelphia Red Light Camera Locations
---------------------------------------
To demonstrate the **2json** web service, I created a Philadelphia Red Light Camera Locations web page at
http://kenlin.com/x/2json/philadelphia-red-light-camera-locations.html 
(source: [here](https://github.com/kenklin/2jsonj/blob/master/WebContent/WEB-INF/philadelphia-red-light-camera-locations.html)).
My web page marks the camera locations from the Open Data from the Philadelphia Parking Authority onto Google maps.
Its JavaScript performs the following steps:
- Calls the **2json** gateway's REST API asking it to transform the CSV at the given URL, 
<a href="https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv" target="_blank">
red-light-camera-locations.csv</a>.  The REST API is invoked via 
[http://p1software-eb1.elasticbeanstalk.com/csv2json/api?url=*csvurl*]
(http://p1software-eb1.elasticbeanstalk.com/csv2json/api).
- Each of the JSON data's "Intersection" strings, like "Roosevelt Blvd @ Grant Ave", are then passed
to the Google Maps API to obtain their latitude and longitude.  The latter are then made into Markers.
- The Google Maps API is called one more time to place the Markers onto a map of Philadelphia with some drop animation.
