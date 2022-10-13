package com.baturayucer.imageservice

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures.layeredArchitecture

@AnalyzeClasses(
    packages = ["com.baturayucer.imageservice"],
    importOptions = [ImportOption.DoNotIncludeTests::class]
)
class ArchitectureTest {

    @ArchTest
    @Suppress("unused")
    val layeredArchitecture: ArchRule = layeredArchitecture()
        .layer("Control").definedBy("..control..")
        .layer("Core").definedBy("..core..")
        .layer("Resource").definedBy("..resource..")
        .whereLayer("Control").mayNotBeAccessedByAnyLayer()
        .whereLayer("Core").mayOnlyBeAccessedByLayers("Control", "Resource")
        .whereLayer("Resource").mayNotBeAccessedByAnyLayer()
        .allowEmptyShould(true)

}
