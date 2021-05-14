import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String fileName = "src/file1.txt";
        System.out.println("Valid Phone Numbers:");
        validPhoneNumber(fileName);
        fileName = "src/file2.txt";
        System.out.println("\n Text to objects and then to Json:");
        userJson(fileName);
        fileName = "src/words.txt";
        System.out.println("\n Counting amount of words in the file words.txt:");
        words(fileName);
    }

    //        //только  два формата: (xxx) xxx-xxxx или xxx-xxx-xxxx (х обозначает цифру).
    public static void validPhoneNumber(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            Pattern pattern;
            Matcher matcher;
            boolean printLine;
            while (line != null) {
                printLine = false;
                pattern = Pattern.compile("\\(\\d{3}\\)\\s\\d{3}-\\d{4}");
                matcher = pattern.matcher(line);
                printLine = matcher.find();
                pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
                matcher = pattern.matcher(line);
                if (printLine |= matcher.find()) {
                    System.out.println(line);
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void userJson(String fileName) {
        ArrayList<User> users = new ArrayList<User>();
        try (FileReader reader = new FileReader(fileName)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            String[] columns;
            boolean foundHeader = false;
            boolean nameFirst = true;
            int positionOfName = 0;
            int positionOfAge = 0;
            while (line != null) {
                if (line.trim().length() >= 8) {
                    positionOfName = line.indexOf("name");
                    positionOfAge = line.indexOf("age");
                    line = bufferedReader.readLine();
                    if (positionOfName >= 0 && positionOfAge >= 0) {
                        nameFirst = positionOfName < positionOfAge ? true : false;
                        foundHeader = true;
                        break;
                    }
                }
                line = bufferedReader.readLine();
            }
            if (foundHeader) {
                while (line != null) {
                    if (line.trim().length() >= 3) {
                        columns = line.trim().split(" ");
                        if (columns.length == 2) {   // here We will create objects of User class
                            if (nameFirst) {
                                users.add(new User(columns[0], Integer.parseInt(columns[1])));
                            } else {
                                users.add(new User(columns[1], Integer.parseInt(columns[0])));
                            }
                        }
                    }
                    line = bufferedReader.readLine();
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(users);
        System.out.println(json);
        try (FileWriter writer = new FileWriter("src/user.json", false)) {
            writer.write(json);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public static void words(String fileName) {

        String[] tempWords = null;
        Map<String, Integer> words = new TreeMap<String, Integer>();
        try (FileReader reader = new FileReader(fileName)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                line = line.replaceAll("( )+", " ").trim();// Makes the line with only one delimiter between words
                if (line.length() != 0) {
                    tempWords = line.split(" ");
                    // taking our words from line to needed cell in words Arraylist and count them to another Arraylist(wordAmount)
                    for (String i : tempWords) {
                        if (words.get(i) != null) {
                            words.put(i, words.get(i) + 1);
                        } else {
                            words.put(i, 1);
                        }
                    }
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Sorting our Treemap and print........
        for (Map.Entry e : entriesSortedByValues(words)) {
            System.out.println(e.getKey() + "_" + e.getValue());
        }
    }

    static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        if (e1.getKey().equals(e2.getKey())) {
                            return -res;
                        } else {
                            return res != 0 ? -res : 1;
                        }
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

}
