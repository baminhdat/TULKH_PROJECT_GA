import Model.Assignment;
import Model.Order;
import Model.Vehicle;

import java.util.Scanner;

public class GA {
    static int geNumber;
    static int orderNumber;
    static int vehicleNumber;
    static VehicleManager vehicleManager;
    static OrderManager orderManager;
    public GA(){
        Scanner scanner = new Scanner(System.in);
        orderNumber = scanner.nextInt();
        vehicleNumber = scanner.nextInt();
        orderManager = new OrderManager(orderNumber);
        vehicleManager = new VehicleManager(vehicleNumber);
        int tmp1, tmp2;
        for(int i=0;i<orderNumber;i++){
            tmp1 = scanner.nextInt();
            tmp2 = scanner.nextInt();
            Order o = new Order(tmp1,tmp2);
            orderManager.add(o);;
        }
        for(int i=0;i<vehicleNumber;i++){
            tmp1 = scanner.nextInt();
            tmp2 = scanner.nextInt();
            Vehicle v = new Vehicle(i+1,tmp1,tmp2);
            vehicleManager.add(v);;
        }
        geNumber = Settings.iterationNumber;
    }
    Population population;
    void run(){
        population = new Population();
        for(int i=0;i<geNumber;i++){
            //System.out.println((i+1)+" "+population.best.fitness);
//            for(Assignment a: population.best.assignments){
//                System.out.print(a.vehicleIndex+" ");
//            }
//            System.out.println();
            population.generateNextGeneration();
            if(population.wOImprovement==population.maxWO){
                //population.maxWO+=50;
                population.wOImprovement = 0;
                population.generateInitialIndividuals();
            }
        }
        //System.out.println(population.best.fitness);
//        for(Vehicle v: vehicleManager.vehicles){
//            System.out.println(v.low_cap+" "+v.up_cap);
//        }
        population.best.printSol();
    }
}
