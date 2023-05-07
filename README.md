# Simple HTTP Server (made in JAVA)
A simple HTTP server made in JAVA sole purpose of which is to parse the requests and send a selected version of response.

## Requirements

- **JDK version:** 20
- **Build Tool:** Apache Maven

```
# https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core

GroupID:     com.fasterxml.jackson.core
Artifact ID: jackson-core
Version:     2.14.2

GroupID:     com.fasterxml.jackson.core
Artifact ID: jackson-databind
Version:     2.14.2
```

## Routes

- Home - `localhost:8000/`
- Simple Text Response - `localhost:8000/parse/`
- JSON Response - `localhost:8000/parse/json/`

## Further Tasks

- send an HTML response
- send an XML response
