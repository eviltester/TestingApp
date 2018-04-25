package uk.co.compendiumdev.sparktesting;

import spark.Spark;

public abstract class SparkStarter {

    protected int sparkport = 4567;

    public abstract boolean isRunning();
    public abstract void startServer();

    public void startSparkAppIfNotRunning(int expectedPort){


        sparkport = expectedPort;


        try {

            System.out.println("Checking if running for integration tests");

            if(!isRunning()) {

                System.out.println("Not running - starting");

                startServer();

                System.out.println("Running spark to start");

            }
        }catch(IllegalStateException e){
            e.printStackTrace();
            System.out.println("TODO: Investigate - " + e.getMessage());
        }

        try{
            sparkport = Spark.port();
        }catch(Exception e){
            System.out.println("Warning: could not get actual Spark port");
        }

        waitForServerToRun();
    }



    private void waitForServerToRun() {
        int tries = 10;
        while(tries>0) {
            if(!isRunning()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }else{
                return;
            }
            tries --;
        }

        System.out.println("Warning: Server might not have started");
    }

    public void killServer(){

        Spark.stop();
        //Spark.halt();

        // wait until server has stopped
        int tries = 10;
        while(tries>0) {

            System.out.println("Checking if server has stopped");

            if(isRunning()){
                try {

                    System.out.println("Spark threads " + Spark.activeThreadCount());
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }else{
                System.out.println("Server has stopped");
                return;
            }
            tries --;
        }

        System.out.println("Server might not have stopped");
    }

    public int getPort() {
        return sparkport;
    }
}
