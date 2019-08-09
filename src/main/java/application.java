import com.jfinal.core.JFinal;

public class application {
    public static void main(String[] args) {
        JFinal.start("src/main/webapp",80,"/",5);
    }

}
