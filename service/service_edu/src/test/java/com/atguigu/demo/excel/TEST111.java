package com.atguigu.demo.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TEST111 {
    public static void main(String[] args) {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person(1,"111"));
        personList.add(new Person(2,""));
        personList.add(new Person(3,"222"));
        personList.add(new Person(4,""));

        List<String> str = personList.stream().map(Person::getAge).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
    }
}
