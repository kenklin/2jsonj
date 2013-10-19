2jsonj
======

CSV to JSON REST Gateway

The **2json.com** web service transforms various data formats (currently CSV) to JSON so web clients
can more easily make use of the data.

The web service's REST API is invoked via 
[**http://www.2json.com/csv2json/api?url**=*csvurl*](http://www.2json.com/csv2json/api?url=csvurl).
- The first row of the source CSV must contain the column titles, like so ...
  <pre>Site ID,Intersection,Site Location,Warnings,Citations,Number of Approaches,Sensor Type</pre>
- The remaining rows of the source CSV must contain the row values, like so ...
  <pre>PI001,Roosevelt Blvd @ Grant Ave,N/B Roosevelt Blvd Svc @ Grant Ave,2/23/2005,6/23/2005,6,PLP
PI002,Roosevelt Blvd @ Grant Ave,N/B Roosevelt Blvd @ Grant Ave,2/23/2005,6/23/2005,6,PLP
. . .
</pre>
- The returned JSON object's
[**data.items**](http://google-styleguide.googlecode.com/svn/trunk/jsoncstyleguide.xml?showone=error#data.items)
property (follows [Google JSON Style Guide](http://google-styleguide.googlecode.com/svn/trunk/jsoncstyleguide.xml))
will have an array of the corresponding CSV values, like so ...
<pre>{
    "data": {
        "items": [
            {
                "Site ID":"PI001",
                "Intersection":"Roosevelt Blvd @ Grant Ave",
                "Site Location":"N/B Roosevelt Blvd Svc @ Grant Ave",
                "Warnings":"2/23/2005",
                "Citations":"6/23/2005",
                "Number of Approaches":6,
                "Sensor Type":"PLP"
            },
            {
                "Site ID":"PI002",
                "Intersection":"Roosevelt Blvd @ Grant Ave",
                "Site Location":"N/B Roosevelt Blvd @ Grant Ave",
                "Warnings":"2/23/2005",
                "Citations":"6/23/2005",
                "Number of Approaches":6,
                "Sensor Type":"PLP"
            },
            . . .
        ]
    }
}</pre>

The table following summaries the technologies used ...
<table>
<tr><th>Type</th><th>Technology</th></tr>
<tr><td>Language</td><td>Java</td></tr>
<tr><td>Web Server</td><td><a href="http://tomcat.apache.org/">Tomcat</a></td></tr>
<tr><td>Backend Web Framework</td><td><a href="http://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/mvc.html">Spring MVC</a></td></tr>
<tr><td>JSON Library</td><td><a href="http://jackson.codehaus.org/">Jackson</a></td></tr>
<tr><td>CSV Library</td><td><a href="http://commons.apache.org/proper/commons-csv/">Apache Commons CSV</a></td></tr>
<tr><td><a href="http://en.wikipedia.org/wiki/Infrastructure_as_a_service#Infrastructure_as_a_service_.28IaaS.29">IAAS</a>
  <td><a ref="http://aws.amazon.com/elasticbeanstalk/">Amazon Elastic Beanstalk</a></td></tr>
</table>

Philadelphia Red Light Camera Locations
---------------------------------------
To demonstrate the **2json.com** web service, I created a Philadelphia Red Light Camera Locations
[web page](http://kenlin.com/x/2json/philadelphia-red-light-camera-locations.html).
My web page marks the camera locations obtained from the Open Data Philadelphia Parking Authority 
[CSV file]((https://github.com/kenklin/2jsonj/blob/master/WebContent/WEB-INF/philadelphia-red-light-camera-locations.html)
onto Google maps.

Its JavaScript 
[code](https://github.com/kenklin/2jsonj/blob/master/WebContent/WEB-INF/philadelphia-red-light-camera-locations.html)
performs the following steps:
- Calls the **2json.com** web service's REST API asking it to transform the CSV at the given URL to JSON, using
<a href="https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv" target="_blank">
**?url**=https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv</a>.
- Each of the JSON data's "Intersection" strings, like "Roosevelt Blvd @ Grant Ave", are then passed
to the Google Maps API to obtain their latitude and longitude.  These are then made into Google Maps Markers.
- The Google Maps API is called one more time to place the Markers onto a map of Philadelphia with some drop animation.

[**Try me**](http://kenlin.com/x/2json/philadelphia-red-light-camera-locations.html)
