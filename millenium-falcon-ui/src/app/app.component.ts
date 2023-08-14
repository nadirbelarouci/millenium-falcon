import { Component, OnInit } from '@angular/core';
import { Edge, Node } from '@swimlane/ngx-graph';
import { EmpireIntel, Route, TravelPlan } from './models/models';
import { RouteService } from './services/route.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'millenium-falcon-ui';
  currentStepIndex = 0;
  bountyHuntersByDay: Map<number, Set<string>> = new Map();
  nodeById: Map<string, Node> = new Map();
  edges: Edge[] = [];
  nodes: Node[] = [];
  travelPlan: TravelPlan = new TravelPlan(0, []);
  travelPlanDescription: string[] = [];
  constructor(private routeService: RouteService) {}

  ngOnInit(): void {
    this.routeService
      .getRoutes()
      .subscribe(routes => this.initializeGalaxyMap(routes));
  }

  private initializeGalaxyMap(routes: Route[]) {
    this.nodeById = new Map();
    this.edges = [];

    new Set(routes.flatMap(route => [route.origin, route.destination])).forEach(
      node => {
        var value = {
          id: node,
          label: node,
          data: {
            customColor: '#009157'
          }
        };
        this.nodes.push(value);
        this.nodeById.set(node, value);
      }
    );
    routes.forEach(route => {
      this.edges.push({
        id: `${route.origin}<->${route.destination}`,
        source: route.origin,
        target: route.destination,
        label: `${route.travel_time}`
      });
    });
  }

  readEmpireIntel(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const contents = e.target.result;
        try {
          const json = JSON.parse(contents);
          const empireIntel = Object.assign(new EmpireIntel(0, []), json);
          this.initializeBountyHunters(empireIntel);
          this.findTravelPlan(empireIntel);
          event.target.value = '';
        } catch (error) {
          console.error('Error parsing JSON', error);
        }
      };
      reader.readAsText(file);
    }
  }

  private initializeBountyHunters(empireIntel: EmpireIntel) {
    this.bountyHuntersByDay = new Map();
    empireIntel.bounty_hunters.forEach(bountyHunter => {
      if (!this.bountyHuntersByDay.has(bountyHunter.day)) {
        this.bountyHuntersByDay.set(bountyHunter.day, new Set());
      }
      this.bountyHuntersByDay.get(bountyHunter.day)?.add(bountyHunter.planet);
    });
  }

  findTravelPlan(empireIntel: EmpireIntel) {
    this.routeService.findTravelPlan(empireIntel).subscribe(travelPlan => {
      this.travelPlan = travelPlan;
      this.travelPlanDescription = this.describeTravelPlan(travelPlan);
      this.updateCurrentStep();
    });
  }

  describeTravelPlan(journey: TravelPlan): string[] {
    const descriptions: string[] = [];
    for (let i = 0; i < journey.path.length - 1; i++) {
      const step = journey.path[i];
      const nextStep = journey.path[i + 1];
      let riskDescription = '';

      // If risk increases, create risk description
      if (nextStep.risk > step.risk) {
        const riskPercent = (nextStep.risk * 100).toFixed(0);
        riskDescription = ` with ${riskPercent}% chance of being captured on day ${nextStep.day} on ${nextStep.node}`;
      }

      // Check for refueling
      if (step.node === nextStep.node && step.fuel !== nextStep.fuel) {
        descriptions.push(`Refuel on ${step.node}${riskDescription}.`);
      }
      // Check for waiting (same node, fuel doesn't change)
      else if (step.node === nextStep.node && step.fuel === nextStep.fuel) {
        descriptions.push(`Wait on ${step.node}${riskDescription}.`);
      }
      // Traveling to a new node
      else if (step.node !== nextStep.node) {
        const travelDescription = `Travel from ${step.node} to ${nextStep.node}${riskDescription}.`;
        descriptions.push(travelDescription);
      }
    }

    return descriptions;
  }

  previousStep() {
    this.currentStepIndex =
      this.currentStepIndex == 0 ? 0 : this.currentStepIndex - 1;
    this.updateCurrentStep();
  }

  private updateCurrentStep() {
    this.nodes.forEach(node => {
      node.data.customColor = '#009157';
    });
    var currentStep = this.travelPlan.path[this.currentStepIndex];
    var currentNode = this.nodeById.get(currentStep.node);
    if (!!currentNode) currentNode.data.customColor = '#7c95ff';
    this.bountyHuntersByDay.get(currentStep.day)?.forEach(bountyHunter => {
      var bountyHunterNode = this.nodeById.get(bountyHunter);
      if (!!bountyHunterNode && currentStep.node == bountyHunter) {
        bountyHunterNode.data.customColor = '#ee0b0b';
      } else if (!!bountyHunterNode) {
        bountyHunterNode.data.customColor = '#ee830b';
      }
    });
  }

  nextStep() {
    this.currentStepIndex =
      this.currentStepIndex == this.travelPlan.path.length - 1
        ? this.currentStepIndex
        : this.currentStepIndex + 1;
    this.updateCurrentStep();
  }
}
