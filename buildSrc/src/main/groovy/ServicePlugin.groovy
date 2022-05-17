import org.gradle.api.Plugin
import org.gradle.api.Project

class ServicePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.apply(plugin: 'org.springframework.boot')

        project.dependencies {

            compile "org.springframework.cloud:spring-cloud-starter-sleuth"
            compile "io.eventuate.tram.springcloudsleuth:eventuate-tram-spring-cloud-sleuth-tram"
            compile "org.springdoc:springdoc-openapi-ui"
        }

    }
}
