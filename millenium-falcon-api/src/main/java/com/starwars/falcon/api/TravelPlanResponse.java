package com.starwars.falcon.api;

import com.google.common.collect.ImmutableSet;

public record TravelPlanResponse(double successProbability, ImmutableSet<Step> path) {}
