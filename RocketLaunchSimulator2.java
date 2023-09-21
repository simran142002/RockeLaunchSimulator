// The code now follows the State design pattern, which helps in managing the rocket's state transitions in a more organized manner.

// included error messages for invalid commands and handled mission failure due to insufficient fuel.

//The code now adheres to SOLID principles, particularly the Single Responsibility Principle (each class has a single responsibility) and the Open/Closed Principle (it's easy to add new states).


import java.util.Scanner;

interface RocketState{
    void handle(RocketContext context);
}

class PreLaunchState implements RocketState {
   @Override
   public void handle(RocketContext context){
   context.setRocketState(new LaunchState());
   System.out.println("All systems are  ready 'Go' for launch.");
   }
}

class LaunchState implements RocketState {
    @Override
    public void handle(RocketContext context) {
        context.setRocketState(new Stage1State());
        System.out.println("Launch initiated. Rocket is now in Stage 1!");
    }
}

//At Every stage fuel consumption is checked and based on the altitude next stage is reached. 
class Stage1State implements RocketState {
    @Override
    public void handle(RocketContext context) {
        context.updateRocket(10, 10, 100);
        if (context.getFuel() <= 0) {
            context.setRocketState(new MissionFailedState("Mission Failed due to insufficient fuel."));
        } else if (context.getAltitude() >= 100) {
            context.setRocketState(new Stage2State());
            System.out.println("Stage 1 complete. Separating stage. Entering Stage 2.");
        }
    }
}

class Stage2State implements RocketState {
    @Override
    public void handle(RocketContext context) {
        context.updateRocket(20, 5, 200);
        if (context.getFuel() <= 0) {
            context.setRocketState(new MissionFailedState("Mission Failed due to insufficient fuel."));
        } else if (context.getAltitude() >= 200) {
            context.setRocketState(new OrbitPlacementState());
            System.out.println("Orbit achieved! Mission Successful.");
        }
    }
}
//Once stage 2 is reached orbit placement state is obtained
class OrbitPlacementState implements RocketState {
    @Override
    public void handle(RocketContext context) {
        System.out.println("Mission has already completed.");
    }
}

class MissionFailedState implements RocketState {
    private final String message;
    //generate message once the mission fails
    public MissionFailedState(String message) {
        this.message = message;
    }

    @Override
    public void handle(RocketContext context) {
        System.out.println(message);
    }
}
//Contains all the required variables and there initial states along with there respective set and get methods which makes the program more generic and user friendly.
class RocketContext {
    RocketState rocketState;
    private int fuel;
    private int altitude;
    private int speed;

    public RocketContext() {
        rocketState = new PreLaunchState();
        fuel = 100;
        altitude = 0;
        speed = 0;
    }

    public void setRocketState(RocketState state) {
    rocketState = state;
    }

    public void updateRocket(int altitudeChange, int fuelConsumption, int speedChange) {
    altitude += altitudeChange;
    fuel -= fuelConsumption;
    speed += speedChange;
    displayStatus();
    }

    public RocketState getRocketState(){
        return rocketState;
    }

    // The displayStatus method provides real-time updates on the rocket's status and hence enables logging.
    public void displayStatus() {
    System.out.println("Stage: " + rocketState.getClass().getSimpleName() +
            ", Fuel: " + fuel + "%, Altitude: " + altitude + " km, Speed: " + speed + " km/h");
    }

    public int getFuel() {
        return fuel;
    }

    public int getAltitude() {
        return altitude;
    }

    public void performChecks() {
        rocketState.handle(this);
    }    
}

//This is the first class which is triggered
// It takes the user input and decides the initial stage
public class RocketLaunchSimulator2 {
    public static void main(String[] args) {
        RocketContext rocketContext = new RocketContext();
        Scanner s = new Scanner(System.in);

        while(true){
            String input = s.nextLine();

            if("start_checks".equals(input)) {
                rocketContext.performChecks();
            } else if("launch".equals(input)){
                rocketContext.setRocketState(new LaunchState());
            } else if (input.startsWith("fast_forward")) {
                String[] parts = input.split(" ");
                if(parts.length == 2){
                    int seconds = Integer.parseInt(parts[1]);
                    for(int i=0; i<seconds; i++){
                        rocketContext.rocketState.handle(rocketContext);
                        if (rocketContext.getRocketState() instanceof MissionFailedState ||
                        rocketContext.getRocketState() instanceof OrbitPlacementState) {
                        break;
                    }
                    }
                } else {
                    System.out.println("Invalid input format. Use 'fast_forward ' where input is the number of seconds.");
                }
            } else {
                System.out.println("Invalid command. Available commands: start_checks, launch, fast_forward ");
            }
        }
    }    
}
