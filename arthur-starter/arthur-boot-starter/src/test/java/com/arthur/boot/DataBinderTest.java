package com.arthur.boot;

import com.arthur.common.utils.MapUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.MutablePropertySources;

import java.util.*;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author DearYang
 * @date 2022-08-03
 * @since 1.0
 */
public class DataBinderTest {


    public static void main(String[] args) throws Exception {
        Person person = new Person();
        //DataBinder binder = new DataBinder(person, "person");
        MutablePropertyValues pvs = new MutablePropertyValues();
        //pvs.add("dog.dogName", "dawang");
        //pvs.add("name[0]", "dmz0");
        //pvs.add("name[1]", "dmz1");
        //pvs.add("age", 18);
        //binder.bind(pvs);
        //System.out.println(person);

        HashMap<String, Object> map = new HashMap<>();
        map.put("dog.dogName", "dawang1");
        map.put("name[0]", "dmz0");
        map.put("name[1]", "dmz1");
        map.put("age", 18);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("dog.dogName", "dawang2");
        map1.put("name[0]", "dmz00");
        map1.put("name[1]", "dmz11");
        map1.put("age", 19);
        List<HashMap<String, Object>> configDataList = Arrays.asList(map, map1);

        MutablePropertySources propertySources = new MutablePropertySources();
        int i = 1;
        for (Map<String, Object> configData : configDataList) {
            Map<String, Object> result = MapUtils.flattenedMap(configData);
            propertySources.addLast(new OriginTrackedMapPropertySource("" + i++, Collections.unmodifiableMap(result), true));
        }

        MapConfigurationPropertySource configurationPropertySource = new MapConfigurationPropertySource(map);
        Binder binder = new Binder(ConfigurationPropertySources.from(propertySources),
                new PropertySourcesPlaceholdersResolver(propertySources),
                new DefaultConversionService(), null, null);
        //List<Person> people = binder.bind("", Bindable.listOf(Person.class)).get();
        Dog dog = binder.bind("dog", Dog.class).get();
        int a = 1;
    }

    static class Dog {
        // 省略getter/setter方法
        String dogName;

        public String getDogName() {
            return dogName;
        }

        public void setDogName(String dogName) {
            this.dogName = dogName;
        }
    }

    static class Person {
        // 省略getter/setter方法
        List<String> name;
        Dog dog;
        int age;

        public List<String> getName() {
            return name;
        }

        public void setName(List<String> name) {
            this.name = name;
        }

        public Dog getDog() {
            return dog;
        }

        public void setDog(Dog dog) {
            this.dog = dog;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name=" + name +
                    ", dog=" + dog +
                    ", age=" + age +
                    '}';
        }
    }
}
