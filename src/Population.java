import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Population {
    Individual best = new Individual("");
    ArrayList<Individual> individuals;
    int size;
    int candidateNumber;
    public int wOImprovement = 0;
    public int maxWO = 50;
    public Population(){
        individuals = new ArrayList<>();
        size = Settings.populationSize;
        candidateNumber = Settings.candidateNumber;
        generateInitialIndividuals();
    }

    void generateInitialIndividuals() {
        individuals.clear();
        for(int i=0;i<size;i++){
            Individual individual = new Individual();
            individuals.add(individual);
        }
        sortAcFitness();
    }
    void generateNextGeneration(){
        ArrayList<Individual> selected = binaryTournamentSelection();
        ArrayList<Individual> next = new ArrayList<>();
        Random rand = new Random();
        for(int i=0;i<size;i++){
            int tmp1 = rand.nextInt(candidateNumber);
            int tmp2 = rand.nextInt(candidateNumber);
            next.add(new Individual(selected.get(tmp1),selected.get(tmp2)));
        }
        individuals.clear();
        individuals.addAll(next);
        sortAcFitness();
    }
    private ArrayList<Individual> selectCandidates(){
        ArrayList<Individual> tmp = new ArrayList<>();
        for(int i=0;i<candidateNumber;i++){
            tmp.add(individuals.get(i));
        }
        return tmp;
    }
    private ArrayList<Individual> binaryTournamentSelection(){
        ArrayList<Individual> tmp = new ArrayList<>();
        int i = 0;
        Random random = new Random();
        while(i<candidateNumber){
            int tmp1 = random.nextInt(size);
            int tmp2 = random.nextInt(size);
            if(tmp1!=tmp2){
                if(individuals.get(tmp1).fitness>individuals.get(tmp2).fitness){
                    tmp.add(individuals.get(tmp1));
                }
                else tmp.add(individuals.get(tmp2));
                i++;
            }
        }
        return tmp;
    }
    private void sortAcFitness(){
        individuals.sort(new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Integer.compare(o2.fitness,o1.fitness);
            }
        });
        if(best.fitness<individuals.get(0).fitness){
            wOImprovement = 0;
            best = individuals.get(0);
        }
        else{
            wOImprovement++;
        }
    }
}
