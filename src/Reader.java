import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Reader {

    private ArrayList<UMLClass> classes = new ArrayList<>();

    public void readFile (String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            String currentId = "";
            boolean currentAb = false;
            String currentValue = "";

            while ((line = reader.readLine()) != null) {
                // This is a class
                if (line.contains("vertex=")) {
                    currentId = extractID(line);
                    currentValue = extractValue(line);
                    currentAb = isAbstract(line);
                    classes.add(new UMLClass(currentId, currentValue, currentAb));
                }

            }

        }
        catch (IOException e){
            System.out.println("Error");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("source=") && !line.contains("shape=flexArrow")) {
                    inheritanceRelationship(line);
                }
            }

        }
        catch (IOException e){
            System.out.println("Error");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("source=") && line.contains("shape=flexArrow")) {
                    compositionRelationship(line);
                }
            }

        }
        catch (IOException e){
            System.out.println("Error");
        }

        print();
    }

    private String extractID(String line) {
        String id = null;
        int start = line.indexOf("id=") + 3;

        char firstChar = line.charAt(start);

        if (firstChar == '"') {
            int end = line.indexOf(firstChar, start + 1);
            id = line.substring(start + 1, end);
        }

        return id;
    }

    private boolean isAbstract(String line) {

        if (line.contains("rounded=1")) {
            return true;
        }
        else if (line.contains("rounded=0")) {
            return false;
        }
        else {
            return false;
        }
    }

    private String extractValue(String line) {
        String value = null;

        int start = line.indexOf("value=") + 6;

        char firstChar = line.charAt(start);

        if (firstChar == '"') {
            int end = line.indexOf(firstChar, start + 1);
            value = line.substring(start + 1, end);
        }
        return value;
    }

    private void inheritanceRelationship(String line) {
        String superClass = getSuperClass(line);
        String subClass = getSubClass(line);

        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getId().equals(subClass)) {
                classes.get(i).setSuperClassId(superClass);
                break;
            }
        }
    }

    private void compositionRelationship(String line) {
        String superClass = getSubClass(line);
        String subClass = getSuperClass(line);
        UMLClass curSuper = null;
        UMLClass curSub = null;

        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getId().equals(superClass)) {
                curSuper = classes.get(i);
            }
        }

        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getId().equals(subClass)) {
                curSub = classes.get(i);
            }
        }

        if (curSub.isAb()) {
            for (int i = 0; i < classes.size(); i++) {
                if (classes.get(i).getSuperClassId() != null && classes.get(i).getSuperClassId().equals(curSub.getId())) {
                    curSuper.addComposition(classes.get(i));
                }
            }
        }
        else {
            curSuper.addComposition(curSub);
        }

    }

    private String getSuperClass (String line) {
        String superClass = null;

        int start = line.indexOf("target=") + 7;
        int end = line.indexOf(line.charAt(start), start + 1);
        superClass = line.substring(start + 1, end);

        return superClass;
    }

    private String getSubClass (String line) {
        String subClass = null;

        int start = line.indexOf("source=") + 7;
        int end = line.indexOf((line.charAt(start)), start + 1);
        subClass = line.substring(start + 1, end);

        return subClass;
    }

    private void print () {
        for (int i = 0; i < classes.size(); i++) {
            UMLClass cur = classes.get(i);
            System.out.println("ID=" + cur.getId() + "   Value=" + cur.getValue() + "   Superclass=" + cur.getSuperClassId() + "   Abstract=" + cur.isAb() + "   Contains:" + cur.getContains());
        }
    }

    private String generateJavaCode(UMLClass c) {
        StringBuilder sb = new StringBuilder();

        // class header
        if (c.isAb()) {
            sb.append("public abstract class ");
        } else {
            sb.append("public class ");
        }

        sb.append(c.getValue());

        // inheritance
        if (c.getSuperClassId() != null) {
            for (int i = 0; i < classes.size(); i++) {
                if (c.getSuperClassId().equals(classes.get(i).getId())) {
                    sb.append(" extends ").append(classes.get(i).getValue());
                }
            }

        }

        sb.append(" {\n\n");

        // composition → fields
        for (int i = 0; i < c.getContains().size(); i++) {
            sb.append("    private ")
                    .append(c.getContains().get(i).getValue())
                    .append(" ")
                    .append(((c.getContains().get(i).getValue())).toLowerCase())
                    .append(";\n");
        }

        sb.append("\n}");

        return sb.toString();
    }

    public void generateAll(String fileDirectory) {
        for (int i = 0; i < classes.size(); i++) {
            String code = generateJavaCode(classes.get(i));
            writeToFile(classes.get(i).getValue() + ".java", code, fileDirectory);
        }
    }

    private void writeToFile(String fileName, String content, String fileDirectory) {
        try (PrintWriter out = new PrintWriter(fileDirectory + fileName)) {
            out.println(content);
        } catch (IOException e) {
            System.out.println("Failed to write " + fileName);
        }
    }

}