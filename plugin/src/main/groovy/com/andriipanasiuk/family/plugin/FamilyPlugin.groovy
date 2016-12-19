package com.andriipanasiuk.family.plugin

import com.android.annotations.NonNull
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator

import javax.inject.Inject

class FamilyPlugin implements Plugin<Project> {

    @NonNull final Instantiator instantiator;

    @Inject
    FamilyPlugin(@NonNull Instantiator instantiator) {
        this.instantiator = instantiator;
    }

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

}
