import org.gradle.api.Plugin
import org.gradle.api.Project

class ServicePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.apply(plugin: 'org.springframework.boot')

        project.dependencies {

            implementation "org.springframework.cloud:spring-cloud-starter-sleuth"
            implementation "org.springframework.cloud:spring-cloud-sleuth-zipkin"
            implementation "io.eventuate.tram.springcloudsleuth:eventuate-tram-spring-cloud-sleuth-tram-starter"
            implementation "org.springdoc:springdoc-openapi-ui"
        }

    }
}
