import kw.team.plugin.setNamespace

plugins {
    id("cosmo.android.feature")
}

android {
    setNamespace("feature.main")
}

dependencies {
    // feature
    implementation(projects.feature.home)

    // domain
    implementation(projects.domain.ai)

    // core
    implementation(projects.core.designsystem)
    implementation(projects.core.network)
    implementation(projects.core.common)

    // android
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
}
