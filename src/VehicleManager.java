import Model.Assignment;
import Model.Order;
import Model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class VehicleManager {
    ArrayList<Vehicle> vehicles;
    ArrayList<Vehicle> low;
    ArrayList<Vehicle> up;
    public VehicleManager(int k){
        vehicles = new ArrayList<>(k);
        low = new ArrayList<>();
        up = new ArrayList<>();
    }
    public void add(Vehicle v){
        //vehicles.add(v);
        low.add(v);
    }
    public void assign(Individual individual){
        Collections.shuffle(individual.assignments);
        for(Assignment a: individual.assignments){
            if(a.vehicleIndex!=0){
                if(!low.get(a.vehicleIndex-1).assign(GA.orderManager.orders.get(a.orderIndex))){
                    a.vehicleIndex = 0;
                }
            }
        }
        updateLowUp();
    }
    public void reAssign(Individual individual){
        //Collections.shuffle(individual.assignments);
        Collections.shuffle(low);
        Collections.shuffle(up);
        for(Assignment a: individual.assignments){
            if(a.vehicleIndex==0){
                for(Vehicle v: low){
                    if(v.assign(GA.orderManager.orders.get(a.orderIndex))){
                        a.vehicleIndex = v.id;
                        break;
                    }
                }
                for(Vehicle v: up){
                    if(v.assign(GA.orderManager.orders.get(a.orderIndex))){
                        a.vehicleIndex = v.id;
                        break;
                    }
                }
            }
        }
        updateLowUp();
        repair(individual);
        vehicles.addAll(low);
        vehicles.addAll(up);
        vehicles.sort(new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle o1, Vehicle o2) {
                return Integer.compare(o1.id,o2.id);
            }
        });
    }
    private void updateLowUp(){
        for(Vehicle v: low){
            if(v.curWeight>=v.low_cap){
                up.add(v);
            }
        }
        low.removeAll(up);
    }
    private void repair(Individual individual){
        Collections.shuffle(low);
        Collections.shuffle(up);
        for(Vehicle v1: low){
            int lowReq = v1.low_cap - v1.curWeight;
            int upReq = v1.up_cap - v1.curWeight;
            for(Vehicle v2: up){
                int overLow = v2.curWeight - v2.low_cap;
                ArrayList<Order> acc = new ArrayList<>();
                for(Order o: v2.orders){
                    if(o.cost<=overLow){
                        acc.add(o);
                    }
                }
                for(Order o: acc){
                    if(o.cost<=overLow){
                        if(o.cost<=upReq){
                            v1.assign(o);
                            v2.remove(o);
                            individual.change(o,v1,v2);
                            overLow-=o.cost;
                            upReq-=o.cost;
                        }
                    }
                    if(overLow==0||upReq==0){
                        break;
                    }
                }
            }
        }
    }
    public int computeFitness(){
        int tmp=0;
        //int violated = 0;
        for(Vehicle v: vehicles){
            tmp += v.computeFitness();
        }
        return tmp;
    }
    void reset(){
        low.clear();
        up.clear();
        for(Vehicle v: vehicles){
            v.reset();
            low.add(v);
        }
        vehicles.clear();
    }

    public void printSol() {
        for(Vehicle v: vehicles){
            System.out.println("Vehicle "+v.id+", low_cap is "+v.low_cap+", up_cap is "+v.up_cap+", current cap is "+v.curWeight);
        }
    }
    public void capacityCheck(){
        int violated = 0;
        for(Vehicle v: vehicles){
            if(v.isViolated()){
                violated++;
                System.out.println(v.low_cap+" "+v.curWeight);
            }
        }
        if(violated==0){
            System.out.println("Satisfied all capacity checks");
        }
    }
}
