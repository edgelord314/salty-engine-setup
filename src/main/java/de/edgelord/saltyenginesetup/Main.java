package de.edgelord.saltyenginesetup;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static String location;      // -l
    private static String gameName;      // -n
    private static int resWidth;         //  -r
    private static int resHeight;        //  ^
    private static long fps;             // -fps
    private static long fixedTickMillis; // -f
    private static boolean splash;       // -s
    private static String groupID;       // -g
    private static String artifactID;    // -a

    private static String saltyEngineVersion;

    /**
     * Usage: <br>
     * <code>-l LOCATION -n GAME-NAME -r WIDTHxHEIGHT -fps FPS -f FIXED_MILLIS-s true/false -g GROUP-ID -a ARTIFACT</code>
     *
     * <p>
     * Example: <br>
     * <code>-l /Users/edgelord/workspace/myGame -n My Game -r 1920x1080 -fps 60 -f 5 -s false -g de.edgelord.myGame -a myGame-fps</code>
     *
     * @param args the args
     */
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            promptData();
        } else if (args.length < 8) {
            System.out.println("Not enough arguments!");
            promptData();
        } else if (args.length > 8) {
            System.out.println("Warning: Too many arguments!");
            readData(mapArgs(args));
        } else {
            readData(mapArgs(args));
        }

        saltyEngineVersion = getSaltyEngineVersion();

        createProject();
        System.out.println("Info: Successfully created project! You can now build it using maven an then run the jar " + location + File.separator + "target" + File.separator + artifactID + "-1.0-SNAPSHOT-jar-with-dependencies.jar");
    }

    private static void createProject() throws IOException {
        File dir = new File(location);
        String packageName = groupID.replace("-", ".") + ".main";

        if (!dir.exists()) {
            dir.mkdirs();
        } else if (dir.isFile()) {
            System.err.println("Error: " + location + " is a file, not a directory!");
            System.exit(1);
        }

        File mainDir = new File(dir, "src/main/");
        File javaDir = new File(mainDir, "java");
        File pomFile = new File(dir, "pom.xml");
        File packageDir = new File(javaDir + File.separator +  packageName.replaceAll("\\.", "/"));
        File mainFile = new File(packageDir, "Main.java");

        mainDir.mkdirs();
        javaDir.mkdirs();
        pomFile.createNewFile();
        packageDir.mkdirs();
        mainFile.createNewFile();

        FileWriter fw = new FileWriter(pomFile);
        BufferedWriter writer = new BufferedWriter(fw);

        String pomText = readStream(Main.class.getClassLoader().getResourceAsStream("pomTemplate.xml")).
                replaceAll("\\{groupId}", groupID).
                replaceAll("\\{artifactId}", artifactID).
                replaceAll("\\{gameName}", gameName).
                replaceAll("\\{mainClass}", packageName + ".Main").
                replaceAll("\\{saltyEngineVersion}", saltyEngineVersion);

        writer.write(pomText);
        writer.close();

        fw = new FileWriter(mainFile);
        writer = new BufferedWriter(fw);

        String startString;

        if (splash) {
            startString = "start(" + fps + ");";
        } else {
            startString = "start("+ fps + ", SplashWindow.Splash.NO_SPLASH);";
        }

        String mainText = readStream(Main.class.getClassLoader().getResourceAsStream("MainTemplate.java")).
                replaceAll("\\{package}", packageName).
                replaceAll("\\{resWidth}", String.valueOf(resWidth)).
                replaceAll("\\{resHeight}", String.valueOf(resHeight)).
                replaceAll("\\{gameName}", gameName).
                replaceAll("\\{fixedMillis}", String.valueOf(fixedTickMillis)).
                replaceAll("\\{start}", startString);

        writer.write(mainText);
        writer.close();

        new File(mainDir, "resources").mkdir();
        new File(dir, "src/test/java").mkdirs();
    }

    private static String readStream(InputStream stream) {
        Scanner scanner = new Scanner(stream).useDelimiter("//A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private static void promptData() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Location of the project (absolute path): ");
        location = scanner.nextLine();

        System.out.print("Name of the game: ");
        gameName = scanner.nextLine();

        System.out.print("Resolution width: ");
        String resWidthString = scanner.nextLine();
        resWidth = Integer.parseInt(resWidthString);

        System.out.print("Resolution height: ");
        String resHeightString = scanner.nextLine();
        resHeight = Integer.parseInt(resHeightString);

        System.out.print("FPS (max. 1000, -1 for dynamic): ");
        String fpsString = scanner.nextLine();
        fps = Long.parseLong(fpsString);

        System.out.print("Fixed tick millis: ");
        String fixedMillisString = scanner.nextLine();
        fixedTickMillis = Long.parseLong(fixedMillisString);

        System.out.print("Salty Engine Splash [true/false]: ");
        String splashString = scanner.nextLine();
        splash = Boolean.parseBoolean(splashString);

        System.out.print("Maven groupId (e.g. de.edgelord.myGame): ");
        groupID = scanner.nextLine();

        System.out.print("Maven artifactId (e.g. myGame): ");
        artifactID = scanner.nextLine();
    }

    private static void readData(HashMap<String, String> mappedData) {

        location = mappedData.get("-l");
        gameName = mappedData.get("-n");

        String[] res = mappedData.get("-r").split("x");
        resWidth = Integer.parseInt(res[0]);
        resHeight = Integer.parseInt(res[1]);

        fps = Long.parseLong(mappedData.get("-fps"));
        splash = Boolean.parseBoolean(mappedData.get("-s"));
        groupID = mappedData.get("-g");
        artifactID = mappedData.get("-a");
    }

    private static String getSaltyEngineVersion() {
        File saltyEngineVersions = new File(System.getProperty("user.home") + "/.m2/repository/de/edgelord/salty-engine/salty-engine");

        if (!saltyEngineVersions.exists()) {
            System.err.println("Error: It seems like Salty Engine isn't installed properly!");
            System.exit(1);
        }

        HashMap<Integer, File> versions = new HashMap<>();

        int index = 0;

        for (File file : Objects.requireNonNull(saltyEngineVersions.listFiles())) {
            if (file.isDirectory()) {
                versions.put(index, file);
                System.out.println(index + ": " + file.getName());
                index++;
            }
        }

        System.out.println("\n");
        System.out.println("Which version do you want [0 - " + (index - 1) + "]?");

        return versions.get(new Scanner(System.in).nextInt()).getName();
    }

    private static HashMap<String, String> mapArgs(String[] args) {
        HashMap<String, String> mappedArgs = new HashMap<>();
        String key = "";
        StringBuilder value = new StringBuilder("-");

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (!value.toString().equals("-")) {
                    mappedArgs.put(key, value.toString());
                }
                key = arg;
                value = new StringBuilder();
            } else {

                if (value.toString().equals("")) {
                    value.append(arg);
                } else {
                    value.append(" ").append(arg);
                }
            }
        }

        mappedArgs.put(key, value.toString());
        return mappedArgs;
    }
}
