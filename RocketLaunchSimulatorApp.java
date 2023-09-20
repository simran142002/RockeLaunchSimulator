import java.util.Scanner;

class RocketLaunchSimulator {
    //Initialise variables
    private String stage;
    private int fuel;
    private int altitude;
    private int speed;
    //Initially fuel is full and the rocket is on the land hence the altitude and speed are ze
    public RocketLaunchSimulator() {
        stage = "Pre-Launch";
        fuel = 100;
        altitude = 0;
        speed = 0;
    }
    //If the stage is equal to prelaunch if yes go for launch
    public void startChecks() {
        if ("Pre-Launch".equals(stage)) {
            System.out.println("All systems are  ready 'Go' for launch.");
        } else {
            System.out.println("Cannot perform pre-launch checks once the launch has started.");
        }
    }
    //If the rockets current stage is Pre-Launch and it has gone for launch
    //then update stage to stage 1 and simulate the flight
    public void launch() {
        if ("Pre-Launch".equals(stage)) {
            stage = "Stage 1";
            System.out.println("Launch initiated. Rocket is now in stage " + stage + "!");
            simulateFlight();
        } else {
            System.out.println("Rocket has already launched.");
        }
    }

    //Enter the seconds you want the rocket to fast-forward with
    public void fastForward(int seconds) {
        if ("Pre-Launch".equals(stage)) {
            System.out.println("Cannot fast forward before launch.");
        } else {
            for (int i = 0; i < seconds; i++) {
                simulateFlight();
            }
        }
    }

    //If stage is Stage 1 tha increment altitude by 10 fuel will get reduced by 10 and speen will increase by 100
    //We need to keep a check on fuel consumption since the mission fails if fuel is completely consumed.
    //Once the rocket crosses altitude of 100 it enters stage 2.
    //In stage 2 altitude will increse by 20, fuel will get consumed by 5 and speed will increase by 200
    //Orbit placement is achieved and the mission is successfull once the altitude is more than 200
    private void simulateFlight() {
        if ("Stage 1".equals(stage)) {
            altitude += 10;
            fuel -= 10;
            speed += 100;
            displayStatus();
            if (fuel <= 0) {
                System.out.println("Mission Failed due to insufficient fuel.");
                return;
            }
            if (altitude >= 100) {
                stage = "Stage 2";
                System.out.println("Stage 1 complete. Separating stage. Entering " + stage + ".");
            }
        } else if ("Stage 2".equals(stage)) {
            altitude += 20;
            fuel -= 5;
            speed += 200;
            displayStatus();
            if (fuel <= 0) {
                System.out.println("Mission Failed due to insufficient fuel.");
                return;
            }
            if (altitude >= 200) {
                stage = "Orbit Placement";
                System.out.println("Orbit achieved! Mission Successful.");
            }
        } else {
            System.out.println("Mission has already completed.");
        }
    }

    private void displayStatus() {
        System.out.println("Stage: " + stage + ", Fuel: " + fuel + "%, Altitude: " + altitude + " km, Speed: " + speed + " km/h");
    }
}
// This class is called first since it contains the main method.
// User can start checks and give a launch
public class RocketLaunchSimulatorApp {
    public static void main(String[] args) {
        RocketLaunchSimulator simulator = new RocketLaunchSimulator();
        Scanner s = new Scanner(System.in);

        while (true) {
            String input = s.nextLine();

            if ("start_checks".equals(input)) {
                simulator.startChecks();
            } else if ("launch".equals(input)) {
                simulator.launch();
            } else if (input.startsWith("fast_forward")) {
                String[] parts = input.split(" ");
                if (parts.length == 2) {
                    int seconds = Integer.parseInt(parts[1]);
                    simulator.fastForward(seconds);
                } else {
                    System.out.println("Invalid input format. Use 'fast_forward X' where X is the number of seconds.");
                }
            } else {
                System.out.println("Invalid command. Available commands: start_checks, launch, fast_forward X");
            }
        }
    }
}
