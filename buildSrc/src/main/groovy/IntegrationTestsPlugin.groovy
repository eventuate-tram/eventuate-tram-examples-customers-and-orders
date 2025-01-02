import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class IntegrationTestsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.sourceSets {
            integrationTest {
                java {
                    compileClasspath += main.output + test.output
                    runtimeClasspath += main.output + test.output
                    srcDir project.file('src/integrationTest/java')
                }
                resources.srcDir project.file('src/integrationTest/resources')
            }
        }

        project.configurations {
            integrationTestImplementation.extendsFrom testImplementation
            integrationTestRuntime.extendsFrom testRuntime
        }

        project.dependencies {
            integrationTestRuntimeOnly "org.junit.platform:junit-platform-launcher"
        }

        project.task("integrationTest", type: Test) {
            testClassesDirs = project.sourceSets.integrationTest.output.classesDirs
            classpath = project.sourceSets.integrationTest.runtimeClasspath
            shouldRunAfter("test")
        }
        project.tasks.findByName("check").dependsOn(project.tasks.integrationTest)



        project.tasks.withType(Test) {
            reports.html.destination = project.file("${project.reporting.baseDir}/${name}")
        }
    }
}
