import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.WordResult;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Class which makes the game run by calling methods from other classes.
 */
public class GamePlay {
    /**
     * Status code
     */
    private static final int urlStatusCode = 200;

    /**
     *
     */
    private static final ArrayList<String> scramSynonyms = new ArrayList<>(
            Arrays.asList("scram", "disengage", "skrrt-skrrt", "gtfo", "bugger-off","scoot", "vanish", "hightail")
    );
    /**
     * Synonyms for take
     */
    private static final ArrayList<String> takeSynonyms = new ArrayList<>(
            Arrays.asList("pickup", "catch", "take", "pick", "steal", "keep", "catch", "acquire", "get")
    );
    /**
     *
     */
    private static final ArrayList<String> statusSynonyms = new ArrayList<>(
            Arrays.asList("status", "health", "life-remaining","life", "state", "condition")
    );
    /**
     * Synonyms for stats
     */
    private static final ArrayList<String> statsSynonyms = new ArrayList<>(
            Arrays.asList("stats", "playerinfo", "myself", "level", "showstats")
    );
    /**
     * Synonyms for drop
     */
    private static final ArrayList<String> dropSynonyms = new ArrayList<>(
            Arrays.asList("set", "put", "shed", "cast", "leave",
                    "drop", "unload", "abandon", "discard", "expel", "eject")
    );
    /**
     * Synonyms for go
     */
    private static final ArrayList<String> goSynonyms = new ArrayList<>(
            Arrays.asList("go", "travel", "move", "run",
                    "depart", "proceed", "transpire", "drive", "enter")
    );
    /**
     * Synonyms for list
     */
    private static final ArrayList<String> listSynonyms = new ArrayList<>(
            Arrays.asList("list", "items", "itemize", "show", "enumerate")
    );
    /**
     * Synonyms for exit
     */
    private static final ArrayList<String> exitSynonyms = new ArrayList<>(
            Arrays.asList("stop", "exit", "quit", "die", "giveup", "cease")
    );
    /**
     *
     */
    private static final ArrayList<String> fightSynonyms = new ArrayList<>(
            Arrays.asList("fight", "duel", "beat", "bitchslap", "engage", "eliminate", "kill", "battle",
                    "combat", "shoot","attack")
    );


    private static void configurationSetup(Configuration configuration) {
        // Set path to acoustic model.
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        // Set path to dictionary.
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        // Set language model.
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
    }

    public static void availableCommandsPrinter(boolean isDueling) {
        if (isDueling) {
            System.out.println("You can :\nAttack\nAttack with Item\nDisengage\nStatus\nList\nPlayerInfo\nExit");
        }
        else {
            System.out.println("You can :\nAttack\nMove\nTake an Item\nDrop an Item\nList\nPlayer Stats\nExit");
        }
    }
    /**
     * url access function for getting json from url
     * @param url url
     * @return return the map object
     */
    public static Layout urlAccess(String url) {
        try {
            Gson localGson = new Gson();

            new URL(url);

            final HttpResponse<String> stringHttpResponse = Unirest.get(url).asString();

            if (stringHttpResponse.getStatus() == urlStatusCode) {
                String layoutJson = stringHttpResponse.getBody();
                return localGson.fromJson(layoutJson, Layout.class);

            }
            else {
                return null;
            }
        }
        catch (UnirestException e) {
            System.out.println("Network Unresponsive!");
            return null;
        }
        catch (MalformedURLException e) {
            System.out.println("Bad URL " + url);
            return null;
        }

    }

    /**
     * getting json from filepath
     * @param filepath string of filepath
     * @return return the map object
     */
    public static Layout filePathAccess(String filepath) {
        Gson localGson = new Gson();
        try {
            JsonReader jsonReader = new JsonReader(new FileReader(filepath));
            return localGson.fromJson(jsonReader, Layout.class);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        }
    }


    /**
     * move a player
     * @param directionName the direction to move in
     * @param player player object for moving
     * @param gameMap game map for code purposes
     */
    public static void movePlayer(String directionName, Player player, Layout gameMap) {

        /*
            Null arguments filter
         */
        if (directionName == null || player == null || gameMap == null) {
            throw new NullPointerException("null pointer");
        }

        /*
            Empty string filter
         */
        if (directionName.length() == 0) {
            throw new IllegalArgumentException("empty direction");
        }

        /*
            Looking through all the directions of the current room
            if found then finding the room with the room name of that direction
            and then assigning that room to the player's current room
         */
        for (Direction direction : player.getCurrentRoom().getDirections()) {

            if (directionName.equalsIgnoreCase(direction.getDirectionName())) {

                for (Room roomSearch : gameMap.getRooms()) {

                    if (roomSearch.getName().equalsIgnoreCase(direction.getRoom())) {
                        player.setCurrentRoom(roomSearch);
                        return;
                    }
                }
            }
        }
        //if direction not found print I can't go aDirection
        System.out.println("I can't go " + directionName);
    }


    /**
     * function for showing duel information
     * @param player player object
     * @param monster monster
     * @param monsterHealth health of the monster
     */
    private static void showDuelStatus(Player player, Monster monster, double monsterHealth) {
        System.out.print("Player:  ");
        for (int i = 0; i < player.getRemainingHealth(); i++) {
            System.out.print("#");
        }
        for (int i = 0; i < (player.getHealthPoints() - player.getRemainingHealth()); i++) {
            System.out.print("_");
        }

        System.out.println("");
        System.out.print(monster.getName() + ":  ");

        for (int i = 0; i < monster.getHealth(); i++) {
            System.out.print("#");
        }
        for (int i = 0; i < (monsterHealth - monster.getHealth()); i++) {
            System.out.print("_");
        }
        System.out.println("");
    }

    /**
     * function for dueling a monster
     * @param monster monster object
     * @param input input string containing info
     * @param player player object
     */
    private static void attackMonster(Monster monster, String[] input, Player player) {
        if (input.length > 1 && input[1].equalsIgnoreCase("with")) {
            if (input.length == 2) {
                System.out.println("Attack with what?");
            }
            else if (player.getPlayerInventory().contains(player.getItemFromNamePl(input[2]))) {
                double damage =  player.getAttack() + player.getItemFromNamePl(input[2]).getDamage() - monster.getDefence();
                System.out.println("You attacked " + monster.getName() + " for " + damage + " damage.");

                monster.setHealth(monster.getHealth() - damage);

                player.setRemainingHealth( player.getRemainingHealth() - monster.getAttack() + player.getDefence());
            }
        }

        else {
            monster.setHealth(monster.getHealth() - player.getAttack() + monster.getDefence());
            player.setRemainingHealth( player.getRemainingHealth() - monster.getAttack() + player.getDefence());
        }
    }

    /**
     * function for simulating duels
     * @param duelScanner scanner object for input
     * @param monster monster object
     * @param player player object
     */
    private static void duel(LiveSpeechRecognizer duelScanner, Monster monster, Player player) {
        String duelInput;
        double monsterHealth = monster.getHealth();
        player.setDueling(true);

        while(player.getRemainingHealth() > 0 && monster.getHealth() > 0 && player.isDueling()) {

            System.out.println("You are fighting " + monster.getName() + "!");
            System.out.println("What do you do?");

            duelScanner.startRecognition(true);
            duelInput = duelScanner.getResult().getHypothesis();

            duelCommandReader(duelInput, player, monster, monsterHealth);

            if (player.getRemainingHealth() < 0) {
                System.out.println("You are dead!");
                System.exit(0);
            }

        }

        if (monster.getHealth() <= 0) {
            double newExp = ((monster.getAttack() + monster.getDefence())/2 + monsterHealth)*20;
            System.out.println("You defeated " + monster.getName() + "\nExperience earned: " + newExp);
            System.out.println(monster.getName() + " dropped a " + monster.getMonsterDrop().getName());

            player.getCurrentRoom().getItems().add(monster.getMonsterDrop());

            player.getCurrentRoom().getMonstersInRoom().remove(monster.getName());

            player.addExperiencePoints(newExp);

            player.setRemainingHealth(player.getHealthPoints());
        }

        while (player.getExperiencePoints() >= player.experienceRequired(player.getLevel() + 1)) {
            player.levelUp();
        }
    }

    /**
     * function for reading commands while in duel mode
     * @param command command string
     * @param player player object
     * @param monster monster object
     * @param monsterHealth monster's health
     */
    public static void duelCommandReader(String command, Player player, Monster monster, double monsterHealth) {
        /*
            Null arguments filter
         */
        if (command == null || player == null || monster == null) {
            throw new NullPointerException("null");
        }
        /*
            Empty string filter
         */
        if (command.length() == 0) {
            throw new IllegalArgumentException("empty string");
        }
        String[] separatedInput = command.trim().split(" +");
        //String nounForAction = command.trim().substring(command.trim().indexOf(separatedInput[0].length()) + 1);


        if (fightSynonyms.contains(separatedInput[0])) {
            attackMonster(monster, separatedInput, player);
        }

        else if (scramSynonyms.contains(separatedInput[0])) {
            player.setDueling(false);
        }

        else if (statsSynonyms.contains(separatedInput[0])) {
            player.showStats();
        }

        else if (listSynonyms.contains(separatedInput[0])) {
            player.listPlayerItems();
        }

        else if (statusSynonyms.contains(separatedInput[0])) {
            showDuelStatus(player, monster, monsterHealth);
        }
        else if (exitSynonyms.contains(separatedInput[0])) {
            System.out.println("Exiting");
            System.exit(0);
        }
        else if (separatedInput[0].equalsIgnoreCase("help")) {
            availableCommandsPrinter(player.isDueling());
        }
        else {
            System.out.println("I don't understand what you mean. You are in a duel right now");
        }
    }

    /**
     * Funcition for reading commands from the terminal
     * @param userInput input line by the user
     * @param player player object in the game
     * @param gameMap map on which we're playing
     * @param recognizer live speech recognizer object for input
     */
    private static void playerCommandRead(String userInput, Player player, Layout gameMap, LiveSpeechRecognizer recognizer) {

        //Null filtering
        if (userInput == null || player == null || gameMap == null) {
            throw new NullPointerException("null pointer exception");
        }

        //empty string filtering
        if (userInput.length() == 0) {
            return;
        }

        //split the input with while spaces
        String[] separatedInput = userInput.trim().split(" +");

        //if the input was intended to move Player
        if (goSynonyms.contains(separatedInput[0])) {
            //if the user just typed go and then nothing
            if (player.getCurrentRoom().getMonstersInRoom().size() > 0) {
                System.out.println("There are still monsters here, I can't move.");
            }
            if (separatedInput.length == 1) {
                System.out.println("Go where?");
            }
            else {
                movePlayer(separatedInput[1], player, gameMap);
            }
        }

        else if (fightSynonyms.contains(separatedInput[0])) {
            if (separatedInput.length == 1) {
                System.out.println("Fight what?");
            }
            else if (player.getCurrentRoom().getMonstersInRoom().contains(separatedInput[1])){
                duel(recognizer,gameMap.getMonsterFromName(separatedInput[1]), player);
            }
            else {
                System.out.println("I can't fight " + separatedInput[1]);
            }
        }

        //showing the player stats
        else if (statsSynonyms.contains(separatedInput[0])) {
            player.showStats();
        }

        //if the input was intended to take an item
        else if (takeSynonyms.contains(separatedInput[0])) {
            //if the user just typed take

            if (player.getCurrentRoom().getMonstersInRoom().size() > 0) {
                System.out.println("There are still monsters here, I can't take anything.");
            }
            else if (separatedInput.length == 1) {
                System.out.println("Take what?");
            }
            else {
                player.addItem(player.getCurrentRoom().getItemFromName(separatedInput[1]));
            }
        }

        //if input was intended to drop an item
        else if (dropSynonyms.contains(separatedInput[0])) {
            //if the user only typed drop and then nothing
            if (separatedInput.length == 1) {
                System.out.println("Drop what?");
            }
            else {
                player.removeItem(player.getItemFromNamePl(separatedInput[1]));
            }
        }

        //if the input was intended to list the items
        else if (listSynonyms.contains(separatedInput[0])) {
            player.listPlayerItems();
        }

        //if the input was intended to exit the game
        else if (exitSynonyms.contains(separatedInput[0])) {
            System.out.println("Exiting");
            System.exit(0);
        }

        else if (separatedInput[0].equalsIgnoreCase("help")) {
            availableCommandsPrinter(player.isDueling());
        }

        //if the input wasn't recognized by the above filters
        else {
            System.out.println("I don't understand " + userInput);
        }
    }


    /**
     * Method that runs the game description till the player reaches the map's endroom.
     * @param config the input received from the console
     * @param gameMap the map on which the game is being played
     */
    private static void playGame(Configuration config, Layout gameMap) throws Exception{

        //input method for speech
        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(config);
        Player player = gameMap.getPlayer();
        player.setCurrentRoom(gameMap.getRoomFromName(gameMap.getStartingRoom()));
        player.setHealthPoints(player.getRemainingHealth());
        /*
            Continue the while loop i.e printing game description till the current room of the
            player is not the ending room of the map.
         */
        while (!(player.getCurrentRoom().getName().equals(gameMap.getEndingRoom()))) {
            //prints room description
            System.out.println(player.getCurrentRoom().getDescription());

            //checks to see if the current room is the starting room and then prints
            //your journey starts here if it is
            if (player.getCurrentRoom().getName().equals(gameMap.getStartingRoom())) {
                System.out.println("Your journey begins here");
            }

            //prints the items in the current room
            player.getCurrentRoom().printItems();
            //prints the monsters in the room
            player.getCurrentRoom().printMonsters();
            if (player.getCurrentRoom().getMonstersInRoom().size() == 0) {
                //prints the available directions for the current room
                player.getCurrentRoom().printDirections();
            }
            //Live input by the user taken as a line
            recognizer.startRecognition(true);
            SpeechResult userInput = recognizer.getResult();
            //process the live input
            playerCommandRead(userInput.getHypothesis(), player, gameMap, recognizer);

        }
        recognizer.stopRecognition();
        //prints ending statement after exiting while loop if the end room is reached
        System.out.println("Congratulations you reached " + gameMap.getEndingRoom() + "! Your journey has ended");
    }





    /**
     * Main method for the game
     * @param args args for main method
     */
    public static void main(String[] args) throws Exception {
        Layout gameMap;
        Configuration config = new Configuration();
        configurationSetup(config);

        if (args.length == 0){
            gameMap = urlAccess("https://courses.engr.illinois.edu/cs126/adventure/siebel.json");
        }
        else {
            gameMap = filePathAccess(args[0]);
        }

        if (gameMap == null) {
            System.out.println("Map Not Working!");
            System.exit(0);
        }
        playGame(config, gameMap);

    }

}
