public class Vehicle {
  private final long arrivalTime;
  private final String name;
  private long serviceStart;
  private long serviceEnd;
  private long timeInQueue;
  private long timeInSystem;
  private boolean inQueue;
  private boolean retired;

  public Vehicle(long arrivalTime, String name) {
    this.arrivalTime = arrivalTime;
    this.name = name;
    inQueue = true;
    retired = false;
  }
  public void retire(long globalStart) {
    serviceEnd = System.currentTimeMillis() - globalStart;
    timeInSystem = serviceEnd - arrivalTime;
    retired = true;
  }

  public void serviceStart(long globalStart) {
    serviceStart = System.currentTimeMillis() - globalStart;
    timeInQueue = serviceStart - arrivalTime;
    inQueue = false;
  }

  public long getArrivalTime() {
    return arrivalTime;
  }

  public String getName() {
    return name;
  }

  public long getServiceStart() {
    return serviceStart;
  }

  public long getServiceEnd() {
    return serviceEnd;
  }

  public long getTimeInQueue() {
    return timeInQueue;
  }

  public long getTimeInSystem() {
    return timeInSystem;
  }

  public boolean isRetired() {
    return retired;
  }

  public boolean isInQueue() {
    return inQueue;
  }
}

