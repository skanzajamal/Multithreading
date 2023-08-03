package bank_transfer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Synchronized_BankTransfer {

    private int id;
    private double balance;
    private String accountName;

    //supports timeout
    final Lock lock = new ReentrantLock();

    public Synchronized_BankTransfer(int id, double balance, String accountName) {
        this.id = id;
        this.balance = balance;
        this.accountName = accountName;
    }

    public boolean withdraw(double amount) throws InterruptedException {
        if(this.lock.tryLock()) {
            Thread.sleep(100);
            balance -= amount;
            this.lock.unlock();
            return true;
        }
        return false;
    }

    public boolean deposit(double amount) throws InterruptedException {
       if(this.lock.tryLock()){
           Thread.sleep(100);
           balance += amount;
           this.lock.unlock();
           return true;
       }
       return false;
    }

    public boolean transfer(Synchronized_BankTransfer to, double amount) throws InterruptedException {
        if (withdraw(amount)) {
            System.out.println("withdrawing amount " + amount + " from " + accountName);
            if(to.deposit(amount)) {
                System.out.println("depositing amount " + amount + " to " + accountName);
            }
            return true;
        }
        else {
            System.out.println("failed to acquire locks: refunding " + amount + "to: " + accountName);
            while (!deposit(amount)) {
                continue;
            }
        }
        return false;
    }

    public static void main(String[] args) {

        Synchronized_BankTransfer studentBankAccount = new Synchronized_BankTransfer(1, 5000, "Student");
        Synchronized_BankTransfer universityBankAccount = new Synchronized_BankTransfer(2, 100000, "University");

        System.out.println("staring balance of student account: " + studentBankAccount.balance);
        System.out.println("starting balance of university account: " + universityBankAccount.balance);

        //running task in asynchronous mode
        ExecutorService service = Executors.newFixedThreadPool(10);

        //Thread for transfer process
        Thread thread = new Thread(()-> {
            System.out.println(Thread.currentThread().getName() + " executing transfer");
            try {
                while (!studentBankAccount.transfer(universityBankAccount, 1000)) {
                    Thread.sleep(100);
                    continue;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " transfer is successful");
        });

        for (int i=0; i<20; i++) {

            //assign task to executor service
            service.submit(thread);
        }
        service.shutdown();

        //to prevent from deadlock
        try {
            while (!service.awaitTermination(24L, TimeUnit.HOURS)) {
                System.out.println("still waiting for termination");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("current balance of student account: " + studentBankAccount.balance);
        System.out.println("current balance of university account: " + universityBankAccount.balance);
    }

} //ENDCLASS
