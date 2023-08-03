package restraurant;

public class SynchronizedRestaurant {

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();
        FastFoodRestaurant fastFoodRestaurant = new FastFoodRestaurant();

        Thread t1 = new Thread(()-> fastFoodRestaurant.buyBurger("Customer1"));
        Thread t2 = new Thread(()-> fastFoodRestaurant.buyBurger("Customer2"));
        Thread t3 = new Thread(()-> fastFoodRestaurant.buyBurger("Customer3"));

        t1.start();
        t2.start();
        t3.start();

        //wait for other threads to complete the task
        t1.join();
        t2.join();
        t3.join();

        System.out.println("Total number of burgers sold: " + fastFoodRestaurant.getNoOfBurgerSold());
        System.out.println("Last name of client: " + fastFoodRestaurant.getLastName());
        System.out.println("Total execution time " + (System.currentTimeMillis() - startTime) + " in milliseconds");
    }

} //ENDCLASS
