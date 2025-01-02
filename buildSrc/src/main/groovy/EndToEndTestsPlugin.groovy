import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class EndToEndTestsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.sourceSets {
            endToEndTest {
                java {
                    compileClasspath += project.sourceSets.main.output + project.sourceSets.test.output
                    runtimeClasspath += project.sourceSets.main.output + project.sourceSets.test.output
                    srcDir project.file('src/endToEndTest/java')
                }
                resources.srcDir project.file('src/endToEndTest/resources')
            }
        }

        project.configurations {
            endToEndTestImplementation.extendsFrom testImplementation
            endToEndTestRuntime.extendsFrom testRuntime
        }

        project.dependencies {
            endToEndTestRuntimeOnly "org.junit.platform:junit-platform-launcher"
        }

        project.task("endToEndTest", type: Test) {
            testClassesDirs = project.sourceSets.endToEndTest.output.classesDirs
            classpath = project.sourceSets.endToEndTest.runtimeClasspath
            systemProperty "eventuate.servicecontainer.baseimage.version", project.ext.eventuateExamplesBaseImageVersion
            systemProperty "eventuate.servicecontainer.serviceimage.version", project.version
        }

        project.tasks.findByName("check").dependsOn(project.tasks.endToEndTest)


        project.tasks.withType(Test) {
            reports.html.destination = project.file("${project.reporting.baseDir}/${name}")
        }
    }
}
