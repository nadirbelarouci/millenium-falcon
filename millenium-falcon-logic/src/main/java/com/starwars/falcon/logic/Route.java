package com.starwars.falcon.logic;

record Route(String origin, String destination, int travelTime) {
  Route reverse() {
    return new Route(destination, origin, travelTime);
  }
}
