# heycar - Backend Challenge

## Installation

To install this app in a production-like environment run the provided build.sh. This will build and run two docker images on local network stack:

1. MongoDB with migrations.
2. App itself, built and tested.

Alternatively you can use maven with jdk11 to run tests or create your custom build.

Prerequisites: docker and eventually maven.

## Specification

Here we are looking to implement an api for a second hand car selling website. A few basic scenarios to be supported are:

1. A dealer uploads or updates it's car listings (JSON or CSV).
2. A user retrieves car listings.
3. A user searches through car listings.

## Design

