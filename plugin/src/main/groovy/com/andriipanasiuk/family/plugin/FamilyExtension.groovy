package com.andriipanasiuk.family.plugin

import com.android.annotations.NonNull
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator

class FamilyExtension {
    static class Person {
        enum Sex {
            MALE, FEMALE
        }
        String firstName, lastName
        Sex sex

        Person() {
        }

        @Override
        String toString() {
            return firstName + " " + lastName
        }
    }

    Person mother, father
    Project project

    FamilyExtension(@NonNull Instantiator instantiator,
                    Project project) {
        this.project = project
    }

    Person father(Closure closure) {
        father = new Person()
        project.configure(father, closure)
        father.sex = Person.Sex.MALE
        return father
    }

    Person mother(Closure closure) {
        mother = new Person()
        project.configure(mother, closure)
        mother.sex = Person.Sex.FEMALE
        return mother
    }

    static class Children {
        List<Person> children = []
        Project project

        Children(Project project) {
            this.project = project
        }

        void son(Closure closure) {
            Person son = new Person()
            project.configure(son, closure)
            son.sex = Person.Sex.MALE
            children.add(son)
        }

        void daughter(Closure closure) {
            Person daughter = new Person()
            project.configure(daughter, closure)
            daughter.sex = Person.Sex.FEMALE
            children.add(daughter)
        }

        @Override
        String toString() {
            StringBuilder builder = new StringBuilder()
            children.each { child ->
                builder.append(child).append(" (")
                        .append((child.sex == Person.Sex.MALE ? "son" : "daughter"))
                        .append("), ")
            }
            int length = builder.length()
            builder.delete(length - 2, length)
            return builder.toString()
        }
    }

}
