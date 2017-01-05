# family-gradle-plugin
Gradle plugin supporting nested extension objects

## Usage
This example project shows how to create gradle plugin to be able to use the following construction in the client's `build.gradle` file.

```groovy
family {
    father {
        firstName = "Ivan"
        lastName = "Karamazon"
    }
    mother {
        firstName = "Katerina"
        lastName = "Verchovtseva"
    }
    children {
        son {
            firstName = "Alesha"
            lastName = "Rakitka"
        }
        daughter {
            firstName = "Agrappina"
            lastName = "Aleksandrovna"
        }
        son {
            firstName = "Peter"
            lastName = "Misuov"
        }

    }
}
```

The plugin also adds `displayFamily` task to the client's project 
so that if you call `./gradlew sample:displayFamily` you will see the next response:
```
:sample:displayFamily
        Father: Ivan Karamazon
        Mother: Katerina Verchovtseva
        Children: Alesha Rakitka (son), Agrappina Aleksandrovna (daughter), Peter Misuov (son)
```

## Installation
You have to call `./gradlew plugin:installArchives` to put `plugin` artifact into the local Maven repository. 
`sample` module gets it from there with:
```
buildscript {
    repositories {
        jcenter()
        mavenLocal()
    }
    dependencies {
        classpath 'com.andriipanasiuk.family:plugin:1.0'
        ...
    }
}
...
apply plugin: 'com.andriipanasiuk.family.plugin'
```

## Under the hood

The main plugin class [`FamilyPlugin`](plugin/src/main/groovy/com/andriipanasiuk/family/plugin/FamilyPlugin.groovy) 
adds `family` extension, `family.children` extension and `displayFamily` task
with the next code:

```groovy
@Override
void apply(Project project) {
    project.extensions.create("family", FamilyExtension, instantiator, project)
    project.family.extensions.create("children", FamilyExtension.Children, project)
    project.task('displayFamily') << {
        println "\tFather: $project.family.father"
        println "\tMother: $project.family.mother"
        println "\tChildren: $project.family.children"
    }
}
```

To support `father`, `mother`, `son` and `daughter` closures 
we add appropriate methods into the [`FamilyExtension`](plugin/src/main/groovy/com/andriipanasiuk/family/plugin/FamilyExtension.groovy#L1) 
or [`Children`](plugin/src/main/groovy/com/andriipanasiuk/family/plugin/FamilyExtension.groovy#L46) class
```groovy
Person father(Closure closure) {
    father = new Person()
    project.configure(father, closure)
    father.sex = Person.Sex.MALE
    return father
}
```
```groovy
void son(Closure closure) {
    Person son = new Person()
    project.configure(son, closure)
    son.sex = Person.Sex.MALE
    children.add(son)
}
```
## StackOverflow

This project is used in answers for
- http://stackoverflow.com/questions/28548887/adding-object-instance-to-gradle-plugin-extension 
- http://stackoverflow.com/questions/17626607/writing-gradle-plugin-with-nested-extension-objects
