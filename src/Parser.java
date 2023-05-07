import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
  public static void main(String[] args) throws IOException {
    File file = new File("data/experiment(2023-05-07|12:18:12).txt");
    Scanner scanner = new Scanner(file);
    String in;
    String[] split;

    float prevArrival = 0;
    float prevServiceEnd = 0;
    float serviceStart = 0;

    float interArrivalTotal = 0;
    float timeInQueueTotal = 0;
    float serviceTimeTotal = 0;
    float timeInSystemTotal = 0;

    float avgInterArrivalTime = 0;
    float avgServiceTime = 0;
    float avgTimeInQueue = 0;
    float avgTimeInSystem = 0;

    float runTime = 0;
    int num = 0;

    while (scanner.hasNextLine()) {
      in = scanner.nextLine();
      split = in.split(" ");

      switch (split[0]) {
        case "arr" -> {
          interArrivalTotal += Float.parseFloat(split[2]) - prevArrival;
          prevArrival = Float.parseFloat(split[2]);
        }
        case "serv_start" -> {
          serviceStart = Float.parseFloat(split[2]);
        }
        case "serv_end" -> {
          serviceTimeTotal += (Float.parseFloat(split[2]) - serviceStart);
        }
        case "q_time" -> {
          timeInQueueTotal += Float.parseFloat(split[2]);
        }
        case "sys_time" -> {
          timeInSystemTotal += Float.parseFloat(split[2]);
        }
        case "exp_runtime" -> {
          runTime = Float.parseFloat(split[2]);
        }
        case "total" -> {
          num = Integer.parseInt(split[3]);
        }
        default -> {}
      }
    }

    avgInterArrivalTime = interArrivalTotal/num;
    avgServiceTime = serviceTimeTotal/num;
    avgTimeInQueue = timeInQueueTotal/num;
    avgTimeInSystem = timeInSystemTotal/num;

    // Print stats
    System.out.println("inter arrival time is " + avgInterArrivalTime + " s on average");
    System.out.println("service time is " + avgServiceTime + " s on average");
    System.out.println("time in queue is " + avgTimeInQueue + " s on average");
    System.out.println("time in system is " + avgTimeInSystem + " s on average");
    System.out.println("running time was " + runTime + " s");
    System.out.println("for " + num + " cars");

    scanner.close();
  }
}
