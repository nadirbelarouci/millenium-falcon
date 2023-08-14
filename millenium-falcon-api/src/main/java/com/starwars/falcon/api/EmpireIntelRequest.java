package com.starwars.falcon.api;

import com.google.common.collect.ImmutableSet;

public record EmpireIntelRequest(
    int countdown, ImmutableSet<BountyHunterLocationRequest> bountyHunters) {}
