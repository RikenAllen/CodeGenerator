 class Main {
    public static void main(String[] args) {
        final String filePath = "C:\\Users\\Riken\\Documents\\2AA4\\CodeGenerator\\Diagrams\\Computer.drawio";
        final String outputDirectory = "C:\\Users\\Riken\\Documents\\2AA4\\CodeGenerator\\GeneratedCode\\Computer\\";


        Reader r = new Reader();

        r.readFile(filePath);
        r.generateAll(outputDirectory);
    }
}
