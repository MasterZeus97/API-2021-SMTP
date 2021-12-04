package prank;

public class Prank {
    private PrankConfig prankconfig;
    private String message;
    private Group group;

    public Prank(String[] configFiles) {
        prankconfig = new PrankConfig(configFiles);
        generatePrank();
    }

    public void generatePrank(){
        message = prankconfig.getMessage();
        group = prankconfig.getGroup();
    }

    public String getMessage() {
        return message;
    }

    public Group getGroup() {
        return group;
    }
}
