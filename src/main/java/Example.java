import dto.Task;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@RestController
@EnableAutoConfiguration
public class Example {
    private static final Logger log = LoggerFactory.getLogger(Example.class);
//    private String api = "http://localhost:4000/tasks/";
    private String api = "https://todo-api-007.herokuapp.com/tasks/";

    @GetMapping("/tasks")
    String all(RestTemplate restTemplate) {
        String url = api;
        Task[] tasks = restTemplate.getForObject(url, Task[].class);
        String response = new String();
        for (Task task : tasks) {
            response += "\n" + task;
        }
        return response;
    }

    @GetMapping("/tasks/{id}")
    String one(RestTemplate restTemplate, @PathVariable String id) {
        String url = api + id;
        Task task = restTemplate.getForObject(url, Task.class);
        return task.toString();
    }

    @PostMapping("/tasks")
    String newTask(RestTemplate restTemplate, @RequestBody Task newTask) {
        String url = api;
        Task task = restTemplate.postForObject(url, newTask, Task.class);
        return "Task added: " + task.toString();
    }

    @PutMapping("/tasks/{id}")
    String replaceTask(RestTemplate restTemplate, @RequestBody Task newTask, @PathVariable String id) {
        String url = api + id;
        HttpEntity httpEntity = new HttpEntity(newTask);
//        restTemplate.put(url, newTask);
        ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Task.class);
        return "putting";
//        return responseEntity.toString();
    }

    @DeleteMapping("/tasks/{id}")
    String deleteTask(RestTemplate restTemplate, @PathVariable String id) {
        String url = api + id;
        restTemplate.delete(url);
        return null;
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}