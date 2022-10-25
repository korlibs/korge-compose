import com.soywiz.korge.gradle.*

plugins {
        //alias(libs.plugins.korge)
    id("com.soywiz.korge") version "3.3.0"
    id("org.jetbrains.compose") version "1.2.0"
}

korge {
    id = "com.sample.democompose"

// To enable all targets at once

    //targetAll()

// To enable targets based on properties/environment variables
    //targetDefault()

// To selectively enable targets

    targetJvm()
    //targetJs()
    //targetDesktop()
    //targetDesktopCross()
    //targetIos()
    //targetAndroidIndirect() // targetAndroidDirect()
    //serializationJson()
    //targetAndroidDirect()
}

dependencies {
    add("commonMainApi", project(":deps"))
    //add("commonMainApi", project(":korge-dragonbones"))
}

