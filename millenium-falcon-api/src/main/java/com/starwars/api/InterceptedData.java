package com.starwars.api;

import com.google.common.collect.ImmutableSet;

public record InterceptedData(
    int countdown, ImmutableSet<BountyHunterLocation> bountyHunterLocations) {}
