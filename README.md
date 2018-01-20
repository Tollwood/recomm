#Recommendation Service
This service offers product/game recommendations for a given user.

# Api Description

    GET customers/{userid}/games/recommendations
   
## Description
    returns an array of game/product recommendations

## Parameters
- **count** _(optinal)_ — number of game recommendations. Default *3*.

#Developers Guide
To start the server local execute

`mvn package && java -jar target/tollwood-recomm-0.0.1.jar`