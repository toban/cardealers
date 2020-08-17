# Backend Challenge

## Installation

To install and run this app in a production environment run the provided build.sh. This will build and start two docker images on local network stack:

1. MongoDB with migrations.
2. App itself, built and tested.

Alternatively, you can use maven with jdk11 to run tests or create your custom build.

Prerequisites: docker, java 11 and eventually maven.

## Specification

Here we are looking to implement an api for a second hand car selling website. A few basic scenarios to be supported are:

1. A dealer uploads or updates it's car listings (JSON and CSV).
2. A user retrieves car listings.
3. A user searches through car listings.

## Design

As stated the app will consist of two parts - the DB and the API App. Both of which are separate Docker containers to allow for separate deployment and provisioning further instances as required.

To make the app potentially available across regions, I've used MongoDB as storage layer. Rest APIs will be served by Spring Boot Reactive. DBAL is MongoDB reactive as well. For CSV parsing I'm going to use OpenCSV. 

The reason why I've chosen MongoDB is because it seems easier to scale out than relational dbs. Adding a node in a new region would allow for low latency reads (potentially writes) there which are important for this case. While MySQL can make the read queries fast through replicas, sharding is a sophisticated thing the more so a multi master scenario. Secondary heycar already uses non relational dbs.

The promise of event-driven architecture f.e. Spring Reactive is more predictable scaling characteristics as well as lower memory consumption under load compared to thread per request. That's why I've chosen this framework. I also find functional-like API quite interesting.      

### Data

With the given requirement for data it's quite simple to maintain consistency in the app itself. Hence we can choose availability when possible. 

'Schema' can be found in `data/scripts`.  

### API

I found the API specification a tad too SOAP-like so I've tried to improve it in accordance with https://martinfowler.com/articles/richardsonMaturityModel.html. So I've reorganized the namings to look resource-centric and used http verbs in responses.

* GET /vehicle -- All vehicle listings\
* GET /vehicle/search?model={model}&make={make}&color={color}&year={year}
* GET /vehicle/dealer/{dealerID} -- Vehicle listings by dealer
* POST /vehicle/dealer/{dealerID} -- Upload listing as json.
* POST /vehicle/dealer/{dealerID}/csv -- Upload listing as csv.

### Testing

Essential test here is to see how the app performs as the whole aka integration test. I'm going to test the API while using an embed MongoDB (https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) to allow for testing across environments. 

## Improvement points

* OpenAPI format for API documentation/specification.
* Separate entities for requests/responses and db.
* Test csv reader
* Introduce app-side validation 
* Use content-type header instead of extra endpoint for csv ingesting.
* Think of what read/write concerns to use.
* There was a bug which appeared once through many builds in container. Couldn't reproduce that however.




