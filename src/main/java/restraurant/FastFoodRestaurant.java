package restraurant;

public class FastFoodRestaurant {

    private String lastName;
    public int noOfBurgerSold = 0;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNoOfBurgerSold() {
        return noOfBurgerSold;
    }

    public void setNoOfBurgerSold(int noOfBurgerSold) {
        this.noOfBurgerSold = noOfBurgerSold;
    }

    public void buyBurger(String clientName) {

        threading();
        System.out.println(clientName + "bought a burger");

        // prevent from race condition
        synchronized (this){
            this.lastName = clientName;
            noOfBurgerSold++;
        }
    }

    public void threading() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

} //ENDCLASS
