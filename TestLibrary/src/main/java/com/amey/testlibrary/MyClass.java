package com.amey.testlibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;



interface abc {
    void interface_abc();

}

abstract class A{
    //    public void testA(){
//        System.out.println("testA");
//    }
    A(){
        System.out.println("Constructor called");
    }
    public abstract void testA();
}

class B implements abc {
    public void testB() {
        System.out.println("testB");
    }

    public void testC() {
        System.out.println("testC");
    }

    @Override
    public void interface_abc() {
        System.out.println("interface_abc called.......");
    }
}


public class MyClass {



    public static void main(String[] args){


        abc a = new B();
        a.interface_abc();


        List<String> tempList = new ArrayList<>();
        Double number, sum = 0.0;
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a number: ");
            number = input.nextDouble();

            if (number < 0.0) {
                break;
            }

            sum += number;
        }
        System.out.println("Sum = " + sum);

        Optional empty =Optional.empty();
    }

    private static void testmethod() {

    }


}





