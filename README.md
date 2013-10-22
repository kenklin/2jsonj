2jsonj
======

CSV to JSON Web Service

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
- The returned JSON object follows the
[Google JSON Style Guide](http://google-styleguide.googlecode.com/svn/trunk/jsoncstyleguide.xml),
returning the CSV values in the
[**data.items**](http://google-styleguide.googlecode.com/svn/trunk/jsoncstyleguide.xml?showone=error#data.items)
array property, like so ...
<pre>
{
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
- The **2json.com** web service caches the *csvurl* and its resultant JSON.  If you believe the information is stale,
add **no-cache** to the query string, like so ...

  [http://www.2json.com/csv2json/api?url=*csvurl*&**no-cache**](http://www.2json.com/csv2json/api?url=csvurl&no-cache).

The table following summaries the **2json.com** web services technologies used ...
<table>
<tr><th>Type</th>
  <th>Technology</th></tr>
<tr><td>Language</td>
  <td>Java</td></tr>
<tr><td>Web Server</td>
  <td><a href="http://tomcat.apache.org/" target="_blank">Tomcat</a></td></tr>
<tr><td>Backend Web Framework</td>
  <td><a href="http://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/mvc.html" target="_blank">Spring MVC</a></td></tr>
<tr><td>JSON Library</td>
  <td><a href="http://jackson.codehaus.org/" target="_blank">Jackson</a></td></tr>
<tr><td>CSV Library</td>
  <td><a href="http://commons.apache.org/proper/commons-csv/" target="_blank">Apache Commons CSV</a></td></tr>
<tr><td><a href="http://en.wikipedia.org/wiki/Infrastructure_as_a_service#Infrastructure_as_a_service_.28IaaS.29" target="_blank">IAAS</a>
  <td><a href="http://aws.amazon.com/elasticbeanstalk/" target="_blank">Amazon Elastic Beanstalk</a></td></tr>
</table>

Example 1 - Philadelphia Red Light Camera Locations
---------------------------------------------------
To demonstrate the **2json.com** web service, I created a Philadelphia Red Light Camera Locations
[web page](http://kenlin.com/x/2json/philadelphia-red-light-camera-locations.html).
My web page marks the camera locations obtained from the Open Data Philadelphia Parking Authority 
[CSV file](https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv)
onto Google maps.

Its JavaScript 
[code](https://github.com/kenklin/2jsonj/blob/master/WebContent/WEB-INF/philadelphia-red-light-camera-locations.html)
performs the following steps:
- Calls the **2json.com** web service's REST API asking it to transform the CSV at the given URL to JSON, using
<a href="https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv" target="_blank">
**?url**=https://raw.github.com/CityOfPhiladelphia/ppa-data/master/red-light-cameras/red-light-camera-locations.csv</a>.
- Each of the JSON data's "Intersection" strings, like "Roosevelt Blvd @ Grant Ave", are then passed
to the Google Maps [Geocoding](https://developers.google.com/maps/documentation/geocoding/) API
to obtain their latitude and longitude.  These are then made into Google Maps Markers.
- The Google [Maps](https://developers.google.com/maps/documentation/javascript/reference#Map) API is called one more time
to drop the Markers onto a map of Philadelphia with some drop animation.

[**Try me**](http://kenlin.com/x/2json/philadelphia-red-light-camera-locations.html)

Example 2 - Craig LaBan's 76 Favorite Restaurants
-------------------------------------------------
I love good food.  [Craig LaBan](http://www.philly.com/philly/columnists/craig_laban/), Philadelphia Inquirer's
restaurant critic wrote a book, "Craig LaBan's 76 Favorite Restaurants" which I got as a gift in 2007.
I found it difficult to visualize where all these wonderful restaurants were, so I transcribed their locations
into my own static
Google [map](https://maps.google.com/maps/ms?hl=en&gl=us&ie=UTF8&oe=UTF8&msa=0&msid=210519233940291334860.0000011278882e18ade7f).

To demonstrate the **2json.com** web service, I copied the information into a CSV
[file](https://raw.github.com/kenklin/2jsonj/master/WebContent/WEB-INF/craig-labans-76-favorite-restaurants.csv),
and then extended the red light camera web page to create a restaurant locations map.  Its JavaScript
[code](https://github.com/kenklin/2jsonj/blob/master/WebContent/WEB-INF/craig-labans-76-favorite-restaurants.html)
performs the following interesting steps:
- The restaurants are rated by "Bells", with 4 being the highest rating.  These numbers are placed in custom markers,
![Alt text](http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=4|c6dbef) following [this](https://developers.google.com/chart/infographics/docs/dynamic_icons?csw=1#plain_pin).
- The marker background colors were taken from the palest color from D3's
[category20c](https://github.com/mbostock/d3/wiki/Ordinal-Scales#categorical-colors).

[**Try me**](http://kenlin.com/x/2json/craig-labans-76-favorite-restaurants.html)
