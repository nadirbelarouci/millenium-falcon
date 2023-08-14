export class Route {
  origin: string;
  destination: string;
  travel_time: number;

  constructor(origin: string, destination: string, travel_time: number) {
    this.origin = origin;
    this.destination = destination;
    this.travel_time = travel_time;
  }
}

export class EmpireIntel {
  countdown: number;
  bounty_hunters: BountyHunterLocation[];

  constructor(countdown: number, bounty_hunters: BountyHunterLocation[]) {
    this.countdown = countdown;
    this.bounty_hunters = bounty_hunters;
  }
}

export class BountyHunterLocation {
  planet: string;
  day: number;

  constructor(planet: string, day: number) {
    this.planet = planet;
    this.day = day;
  }
}

export class TravelPlan {
  success_probability: number;
  path: Step[];

  constructor(success_probability: number, path: Step[]) {
    this.success_probability = success_probability;
    this.path = path;
  }
}

export class Step {
  node: string;
  day: number;
  fuel: number;
  risk: number;

  constructor(node: string, day: number, fuel: number, risk: number) {
    this.node = node;
    this.day = day;
    this.fuel = fuel;
    this.risk = risk;
  }
}
