//dependencyResolutionManagement {
//    this.components {
//        this.all {
//            //this.
//        }
//    }
//}
pluginManagement {
    repositories {
        mavenLocal(); mavenCentral(); google(); gradlePluginPortal()
    }
    //resolutionStrategy {
    //    eachPlugin {
    //        println("details: ${this.requested}")
    //    }
    //    /*
    //    eachDependency { details: DependencyResolveDetails ->
    //        println("details")
    //        /*
    //        if (details.requested.group == 'io.swagger.parser.v3' && details.requested.name =='swagger-parser') {
    //            details.useVersion("2.0.26")
    //        }
    //         */
    //    }
    //     */
    //}
}

plugins {
    //id("com.soywiz.kproject.settings") version "0.0.1-SNAPSHOT"
    id("com.soywiz.kproject.settings") version "0.1.3"
}

rootProject.name = "korge-compose-example"

kproject("./deps")
