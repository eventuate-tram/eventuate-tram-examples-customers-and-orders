import org.gradle.api.Plugin
import org.gradle.api.Project

class ServicePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.apply(plugin: 'org.springframework.boot')

        project.dependencies {

            implementation "org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j"
            implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui'
        }

    }
}
