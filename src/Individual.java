import Model.Assignment;
import Model.Order;
import Model.Vehicle;

import java.util.*;

public class Individual {
    int fitness;
    ArrayList<Assignment> assignments;
    Individual(){
        //a = new ArrayList<>();
        assignments = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<GA.orderNumber;i++) {
            int tmp = random.nextInt(GA.vehicleNumber+1);
            //a.add(random.nextInt(GA.vehicleNumber + 1));
            assignments.add(new Assignment(i,tmp));
            //assignments.add(new Assignment(GA.orderManager.orders.get(i),GA.vehicleManager.vehicles.get(random.nextInt(GA.vehicleNumber + 1))));
        }
        computeFitness(false);
        computeFitness(false);
    }
    Individual(Individual i1, Individual i2){
        //a = new ArrayList<>();
        assignments = new ArrayList<>();
        Random random = new Random(10);
        for(int i=0;i<GA.orderNumber;i++){
            if(random.nextInt()>4){
                //a.add(i1.a.get(i));
                assignments.add(i1.assignments.get(i));
            }
            else {
                //a.add(i2.a.get(i));
                assignments.add(i2.assignments.get(i));
            }
        }
        mutate();
        computeFitness(false);
        computeFitness(false);
    }
    Individual(String s){
        assignments = new ArrayList<>();
        fitness = Integer.MIN_VALUE;
    }
    void mutate(){
        Random rand = new Random();
        int tmp1 = rand.nextInt(GA.orderNumber);
        int tmp2 = rand.nextInt(GA.orderNumber);
        int tmp = assignments.get(tmp1).vehicleIndex;
        assignments.get(tmp1).vehicleIndex = assignments.get(tmp2).vehicleIndex;
        assignments.get(tmp2).vehicleIndex = tmp;
    }
    void computeFitness(boolean isLast){
        assignments.sort(new Comparator<Assignment>() {
            @Override
            public int compare(Assignment o1, Assignment o2) {
                return Integer.compare(GA.orderManager.orders.get(o2.orderIndex).cost, GA.orderManager.orders.get(o1.orderIndex).cost);
            }
        });
        GA.vehicleManager.assign(this);
        GA.vehicleManager.reAssign(this);
        assignments.sort(new Comparator<Assignment>() {
            @Override
            public int compare(Assignment o1, Assignment o2) {
                return Integer.compare(o1.orderIndex,o2.orderIndex);
            }
        });
        fitness = GA.vehicleManager.computeFitness();
        if(!isLast){
            GA.vehicleManager.reset();
        }
    }
    public boolean isLast = false;
    void printSol(){
        isLast = true;
        computeFitness(true);
        System.out.println("Highest profit found is "+fitness);
        int tmp = 0;
        for(Assignment a: assignments){
            if(a.vehicleIndex!=0){
                tmp+=1;
            }
            else{
                System.out.println("Unserved: "+GA.orderManager.orders.get(a.orderIndex).cost);
            }
        }
        System.out.println("Number of orders served is "+tmp);
        GA.vehicleManager.capacityCheck();
//        for(Assignment a: assignments){
//            //if(a.vehicleIndex!=0){
//                System.out.println((a.orderIndex+1)+" "+GA.orderManager.orders.get(a.orderIndex).profit+" "+a.vehicleIndex);
//            //}
//        }
    }

    public void change(Order o,Vehicle v1, Vehicle v2) {
        int orderIndex=-1;
        for(int i=0;i<GA.orderNumber;i++){
            if(GA.orderManager.orders.get(i)==o){
                orderIndex = i;
                break;
            }
        }
        for(int i=0;i<GA.orderNumber;i++){
            if(assignments.get(i).orderIndex==orderIndex){
                assignments.get(i).vehicleIndex=v1.id;
            }
        }
    }
}
