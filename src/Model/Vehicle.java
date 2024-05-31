package Model;

import java.util.ArrayList;

import static java.lang.Math.min;

public class Vehicle {
    public int id;
    public int low_cap;
    public int up_cap;
    public int curWeight;
    public int curProfit;
    private int penalty = 100;
    public ArrayList<Order> orders;
    public Vehicle(int id, int low, int up){
        this.id = id;
        low_cap = low;
        up_cap = up;
        orders = new ArrayList<>();
        reset();
    }
    public void reset(){
        curProfit = 0;
        curWeight = 0;
        orders.clear();
        //bestOrder=-1;
    }
    public boolean assign(Order o){
        if(curWeight+o.cost<=up_cap) {
            curWeight += o.cost;
            curProfit += o.profit;
            orders.add(o);
//            if (o.profit > bestOrder) {
//                bestOrder = o.profit;
//            }
            return true;
        }
        else return false;
    }
    public void remove(Order o){
        //if(orders.contains(o)){
            orders.remove(o);
            curWeight-=o.cost;
            curProfit-=o.profit;
        //}
    }
    public int computeFitness(){
//        int tmp =  curProfit+(min(curWeight-low_cap,0)+min(up_cap-curWeight,0))*penalty;
        int tmp = curProfit;
        tmp+= min(curWeight-low_cap,0)*penalty;
        return tmp;
    }

    public boolean isViolated() {
        return (curWeight-low_cap<0);
    }
}
