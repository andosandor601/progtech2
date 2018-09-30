package hu.elte.progtech.solid.company;

public class Company {

    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.setWorker(new Worker());
        manager.manage();
    }
    
}

class Worker {
    public void work() {
        System.out.println("work a lot");
    }
}

class Manager {

    Worker worker;

    public void setWorker(Worker w) {
        worker = w;
    }

    public void manage() {
        worker.work();
    }
}

class SuperWorker {
    public void work() {
        System.out.println("Work much more");
    }
}
