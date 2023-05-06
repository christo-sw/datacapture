import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {
  static BufferedReader reader;
  static ArrayList<Vehicle> vehicles;

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Press enter to start");
    reader.readLine();
    vehicles = new ArrayList<>();

    Path data = Path.of("data");
    if (!Files.exists(data)) {
      Files.createDirectory(data);
    }
    DateFormat dateFormat = new SimpleDateFormat("(yyyy-MM-dd|HH:mm:ss)");
    Date date = new Date();

    File file = new File("data/experiment" + dateFormat.format(date) + ".txt");

    if (!file.createNewFile()) {
      System.err.println("could not create file");
      System.exit(0);
    }

    FileWriter fw = new FileWriter(file);

    String input;
    String[] splitInput;
    boolean running = true;
    long globalStart = System.currentTimeMillis();

    while (running) {
      System.out.print("COMMAND: ");
      input = reader.readLine();
      splitInput = input.split(" ");

      if (splitInput.length == 1 && !splitInput[0].equals("stop")) {
        System.out.println("ERR: NO NAME");
        continue;
      }

      switch (splitInput[0]) {
        case "a" -> {
          // Add
          Vehicle vehicle = new Vehicle(System.currentTimeMillis() - globalStart, splitInput[1]);
          vehicles.add(vehicle);
        }
        case "s" -> {
          // Start service, requires name
          for (int i = (vehicles.size() - 1); i >= 0; i--) {
            if (vehicles.get(i).getName().equals(splitInput[1]) && vehicles.get(i).isInQueue()) {
              vehicles.get(i).serviceStart(globalStart);
              break;
            }
          }
        }
        case "j" -> {
          // Add and start service immediately
          Vehicle vehicle = new Vehicle(System.currentTimeMillis() - globalStart, splitInput[1]);
          vehicle.serviceStart(globalStart);
          vehicles.add(vehicle);
        }
        case "r" -> {
          // Retire, requires ID
          for (int i = (vehicles.size() - 1); i >= 0; i--) {
            if (vehicles.get(i).getName().equals(splitInput[1])
                && !vehicles.get(i).isInQueue()
                && !vehicles.get(i).isRetired()) {
              vehicles.get(i).retire(globalStart);
              break;
            }
          }
        }
        case "stop" -> {
          long globalStop = System.currentTimeMillis();
          running = false;
          fw.write("experiment started on " + new Timestamp(globalStart) + "\n");
          for (Vehicle vehicle : vehicles) {
            fw.write("\n");
            fw.write(vehicle.getName() + "\n");
            fw.write("arr = " + (vehicle.getArrivalTime() / 1000F + " s\n"));
            fw.write("serv_start = " + (vehicle.getServiceStart() / 1000F) + " s\n");
            fw.write("serv_end = " + (vehicle.getServiceEnd() / 1000F) + " s\n");
            fw.write("q_time = " + (vehicle.getTimeInQueue() / 1000F) + " s\n");
            fw.write("sys_time = " + (vehicle.getTimeInSystem() / 1000F) + " s\n");
          }
          fw.write("\nexp_runtime = " + (globalStop - globalStart) / 1000F + " s\n");
          fw.write("total vehicles = " + vehicles.size());
          fw.flush();
          fw.close();
          continue;
        }
        default -> {
          System.out.println("INVALID");
          continue;
        }
      }

      printDetails();
    }
  }

  private static void printDetails() {
    ArrayList<Vehicle> inSystem = new ArrayList<>();
    ArrayList<Vehicle> retired = new ArrayList<>();
    for (Vehicle vehicle : vehicles) {
      if (vehicle.isRetired()) {
        retired.add(vehicle);
      } else {
        inSystem.add(vehicle);
      }
    }

    // Print
    StringBuilder str = new StringBuilder();
    StringBuilder qStr = new StringBuilder();
    StringBuilder sStr = new StringBuilder();
    str.append("====================SYS====================\n");

    qStr.append("QUEUE: ");
    sStr.append("SERVICE: ");

    if (!inSystem.isEmpty()) {
      for (Vehicle vehicle : inSystem) {
        if (vehicle.isInQueue()) {
          qStr.append(vehicle.getName());
          qStr.append(" | ");
        } else {
          sStr.append(vehicle.getName());
          sStr.append(" | ");
        }
      }
      if (qStr.lastIndexOf("|") != -1) {
        qStr.deleteCharAt(qStr.lastIndexOf("|"));
      }
      if (sStr.lastIndexOf("|") != -1) {
        sStr.deleteCharAt(sStr.lastIndexOf("|"));
      }
    }
    qStr.append("\n");
    sStr.append("\n");

    str.append(qStr).append(sStr);
    str.append("====================RET====================\n");

    str.append("LATEST: ");
    if (!retired.isEmpty()) {
      for (int i = (retired.size() - 1); (i > (retired.size() - 5)) && (i >= 0); i--) {
        str.append(retired.get(i).getName());
        str.append(" | ");
      }
      str.deleteCharAt(str.lastIndexOf("|"));
    }

    str.append("\n");
    System.out.println(str);
  }

  private static String getInput() {
    try {
      return reader.readLine();
    } catch (IOException e) {
      System.out.println("invalid");
      return "";
    }
  }
}
