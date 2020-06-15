package java.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplateTest {
    public static void main(String[] args) {
        System.out.println("args = " + Arrays.deepToString(args));
        System.out.println("TemplateTest.main");
        System.out.println("args = " + args);

        // ifn
        List list = new ArrayList();
        if (list == null) {

        }

        // inn
        if (list != null) {

        }
    }


}
