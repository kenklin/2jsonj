2jsonj
======

CSV to JSON REST Gateway

The *2json* web service transforms various data formats (currently CSV) to JSON so that web clients
can more easily make use of the data.


Philadelphia Red Light Camera Locations
---------------------------------------
To demonstrate the *2json* web service, I created a Philadelphia Red Light Camera Locations web page at
http://kenlin.com/x/2json/philadelphia-red-light-camera-locations.html.  The page performs the following steps:
- Passes the URL, 
[https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv]
(https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv)
to the 2json gateway, currently at
[http://p1software-eb1.elasticbeanstalk.com/csv2json/api]
(http://p1software-eb1.elasticbeanstalk.com/csv2json/api).
- With the CSV data transformed to JSON, it then passes each the "Intersection" strings like "Roosevelt Blvd @ Grant Ave"
to the Google Maps API and creating Markers for their latitude and longitude.
- The Google Maps API is again used to place the Markers onto a map of Philadelphia.
