package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Alphabet {

    public ArrayList<Character> CreateAlphabetRus() {
        ArrayList<Character> alphabetRus = new ArrayList<>();
        for (char i = 1040; i < 1104; i++) {
            alphabetRus.add(i);
        }
        alphabetRus.add(6,'Ё');
        alphabetRus.add(39,'ё');
        alphabetRus.add('.');
        alphabetRus.add(',');
        alphabetRus.add('"');
        alphabetRus.add(':');
        alphabetRus.add('—');
        alphabetRus.add('!');
        alphabetRus.add('?');
        alphabetRus.add(' ');
        return alphabetRus;
    }
    public ArrayList<Character> createAlphabetCripto (ArrayList<Character> arrayList, int key) {
        ArrayList<Character> alphabetCripto = new ArrayList<>();
        if (Math.abs(key) > arrayList.size()) {
            key = key % arrayList.size();
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (key > 0) {
                if ((i + key) >= arrayList.size()) {
                    alphabetCripto.add(arrayList.get((i + key) - arrayList.size()));
                } else {
                    alphabetCripto.add(arrayList.get(i + key));
                }
            } else {
                if ((i + key) < 0) {
                    alphabetCripto.add(arrayList.get(arrayList.size() + (i + key)));
                } else {
                    alphabetCripto.add(arrayList.get(i + key));
                }
            }
        }
        return alphabetCripto;
    }

    public String ceaserCript (Path path, ArrayList<Character> arrayList,ArrayList<Character> arrayList1) throws IOException {
        List<String> list = Files.readAllLines(path);
        StringBuilder stringBuilder = new StringBuilder();

        for (String str : list) {
            for (char ch: str.toCharArray()) {
                int originalPosition = arrayList.indexOf(ch);
                if (originalPosition > 0) {
                    char charNew = (arrayList1.get(originalPosition));
                    stringBuilder.append(charNew);
                }
                else {
                    stringBuilder.append(ch);
                }
            }
        }
        Path path1 = Path.of("texts/cryptCeaser.txt");
        Files.writeString(path1, stringBuilder);
        return stringBuilder.toString();
    }

    public String ceaserKeyDecript (Path path, ArrayList<Character> alphabetCripto, int key) throws IOException {
        String string = Files.readString(path);
        key = key % alphabetCripto.size();
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int index = alphabetCripto.indexOf(chars[i]) - key;
            if (index > 0) {
                chars[i] = alphabetCripto.get(index);
            }
            if (index < 0) {
                chars[i] = alphabetCripto.get(alphabetCripto.size() - Math.abs(index));
            }
            if (index == 0) {
                chars[i] = alphabetCripto.get(alphabetCripto.size() - Math.abs(index) - 1);
            }
            stringBuilder.append(chars[i]);

        }

        Path path1 = Path.of("texts/decryptKeyCeaser.txt");
        if (!Files.exists(path1)) {
            Files.createFile(path1);
        }
        Files.writeString(path1, stringBuilder);
        return stringBuilder.toString();
    }

    public String ceaserDecript (Path path, ArrayList<Character> arrayList) throws IOException {
        StringBuilder sb = new StringBuilder();
        ArrayList<Integer> whitespaces = new ArrayList<>();
        ArrayList<Integer> whitespacesMax = new ArrayList<>();
        HashMap<Integer, String> result = new HashMap<>();
        Path path1 = Path.of("texts/decryptCeaser.txt");
        String str = Files.readString(path);
        char[] chars = str.toCharArray();
        char[] chars1 = new char[chars.length];
        for (int j = 0; j < arrayList.size(); j++) {
            for (int i = 0; i < chars1.length; i++) {
                if (arrayList.indexOf(chars[i]) > 0) {
                    int index = arrayList.indexOf(chars[i]) + j;
                    if (index >= arrayList.size()) {
                        index = index - arrayList.size();
                    }
                    chars1[i] = arrayList.get(index);
                }
                else chars1[i] = chars[i];
            }

            for (char ch: chars1) {
                sb.append(ch);
            }

            char[] chars2 = sb.toString().toCharArray();
            for (int i = 0; i < chars2.length; i++) {
                if (chars2[i] == ' ')
                    whitespaces.add(i);
            }
            whitespacesMax.add(whitespaces.size());
            result.put(whitespaces.size(),sb.toString());
            whitespaces.clear();
            sb.delete(0,chars.length);
        }
        if (!Files.exists(path1)) {
            Files.createFile(path1);
        }
        Files.writeString(path1, result.get(Collections.max(whitespacesMax)));

        return result.get(Collections.max(whitespacesMax));
    }
    public HashMap<Character,Double> Statistic(Path path,ArrayList<Character> alphabetRus) throws IOException {
        String str = Files.readString(path);
        HashMap<Character, Double> Probabilities = new LinkedHashMap<>();
        char[] chars = str.toCharArray();

        int index = 0;

        for (char ch: alphabetRus) {
            for (int i = 0; i < chars.length; i++) {
                if (ch == chars[i]) {
                    index++;
                }
            }
            double charProbability = 100*((double) index/ (double) chars.length);
            Probabilities.put(ch, charProbability);
            index = 0;
        }
        return Probabilities;
    }

    public ArrayList<Character> sortHash(HashMap<Character, Double> hashMap) {
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<Double> doubles = new ArrayList<>();
        for (HashMap.Entry<Character, Double> e : hashMap.entrySet()) {
            double value = e.getValue();
            boolean isAdded = false;
            for (int i = 0; i < doubles.size(); i++) {
                if (value > doubles.get(i)) {
                    doubles.add(i, value);
                    characters.add(i, e.getKey());
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) {
                doubles.add(value);
                characters.add(e.getKey());
            }
        }
        return characters;
    }

    public HashMap<Character, Character> keyHash (ArrayList<Character> keys, ArrayList<Character> values) {
        HashMap<Character, Character> hashMap2 = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            hashMap2.put(values.get(i),keys.get(i));
        }
        return hashMap2;
    }

    public String decryptStat(Path path, HashMap<Character, Character> hashMap) throws IOException {
        String string = Files.readString(path);
        Path path1 = Path.of("texts/decryptCeaserStat.txt");
        if (!Files.exists(path1)) {
            Files.createFile(path1);
        }
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            for (Map.Entry<Character, Character> pair: hashMap.entrySet()) {
                if (chars[i] == pair.getKey()) {
                    chars[i] = pair.getValue();
                    break;
                }
            }
        }
        for (int i = 0; i < chars.length; i++) {
            stringBuilder.append(chars[i]);
        }
        Files.writeString(path1, stringBuilder.toString());
        return stringBuilder.toString();
    }
}