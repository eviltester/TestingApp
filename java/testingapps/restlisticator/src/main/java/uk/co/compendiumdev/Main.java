package uk.co.compendiumdev;

import spark.Spark;
import uk.co.compendiumdev.restlisticator.sparkrestserver.RestServer;
import java.util.Arrays;
import java.util.List;

import static spark.Spark.get;

/**
 * Created by Alan on 04/07/2017.
 */
public class Main {


    static boolean hasHerokuAssignedPort(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        return (processBuilder.environment().get("PORT") != null);
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (hasHerokuAssignedPort()) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return -1; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) {


        Integer proxyport = 4567;    // default for spark

        // added to support heroku as per https://sparktutorials.github.io/2015/08/24/spark-heroku.html
        // environment can override config for port
        if(hasHerokuAssignedPort()) {
            proxyport = getHerokuAssignedPort();
        }

        int resetTimer=0; // by default no reset timer

        // By default run in single user mode
        // use a -multiuser argument to force it into multi user mode and have to use GET /sessionid

        final List<String> argsAsList = Arrays.asList(args);
        if(System.getenv().containsKey("RestListicatorMultiUser")){
            argsAsList.add("-multiuser");
        }
        if(System.getProperties().containsKey("RestListicatorMultiUser")){
            argsAsList.add("-multiuser");
        }


        // we can automatically reset the default api details by using -resettimmer=MILLISECONDS
        // allow setting this from environment config or properties to support different deployment and startup mechanisms
        if(System.getenv().containsKey("RestListicatorApiResetMillis")){
            argsAsList.add("-resettimer="+System.getenv("RestListicatorApiResetMillis"));
        }
        if(System.getProperties().containsKey("RestListicatorApiResetMillis")){
            argsAsList.add("-resettimer="+System.getProperty("RestListicatorApiResetMillis"));
        }


        for(String arg : argsAsList){
            System.out.println("Args: " + arg);

            if(arg.startsWith("-port")){
                String[]details = arg.split("=");
                if(details!=null && details.length>1){
                    proxyport = Integer.parseInt(details[1].trim());
                    System.out.println("Will configure web server to use port " + proxyport);
                }
            }
        }



        // TODO: add some tests around multiuser mode

        // TODO: add a shutdown verb as configurable through arguments e.g. -shutdownable=false

        Spark.port(proxyport);

        String []newargs = argsAsList.toArray(new String[argsAsList.size()]);

        // redirects for documentation when running as own app
        get("/", (req, res) -> { res.redirect("/listicator/"); return "";});
        get("", (req, res) -> { res.redirect("/listicator/"); return "";});

        RestServer restServer = new RestServer(newargs, "/listicator"); // added /listicator to make standalone same as main
        restServer.documentationDetails(proxyport);


        System.out.println("Running on " + Spark.port());

    }


}
