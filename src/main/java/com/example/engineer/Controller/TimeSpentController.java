package com.example.engineer.Controller;

import com.example.engineer.Model.DTO.TimeSpent.TimeSpentByTaskDTO;
import com.example.engineer.Model.DTO.TimeSpent.TimeSpentByUserDTO;
import com.example.engineer.Model.DTO.TimeSpent.TimeSpentDTO;
import com.example.engineer.Model.Mappers.TaskMapper;
import com.example.engineer.Model.Mappers.UserMapper.UserMapper;
import com.example.engineer.Model.TimeSpentEntity;
import com.example.engineer.Service.TimeSpentService;
import com.example.engineer.Service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/time")
public class TimeSpentController {
    private final TimeSpentService timeSpentService;
    private final UserService userService;

    @GetMapping("/task/{id}")
    public ResponseEntity<String> getByTask(@PathVariable Long id) {
        TimeSpentByTaskDTO tst = timeSpentService.findTimeByTask(id);

        if (tst != null)
            return ResponseEntity.ok(new Gson().toJson(tst));
        else
            return ResponseEntity.notFound().build();
    }


    @GetMapping("/user/{email}")
    public ResponseEntity<String> getByUser(@PathVariable String email) {
        TimeSpentByUserDTO tsu = timeSpentService.findTimeByUser(email);

        if (tsu != null)
            return ResponseEntity.ok(new Gson().toJson(tsu));
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/{email}/{taskId}")
    public ResponseEntity<String> get(@PathVariable Long taskId, @PathVariable String email) {
        TimeSpentEntity ts = timeSpentService.get(taskId, email);

        if (ts != null)
            return ResponseEntity.ok(new Gson().toJson(TimeSpentDTO.builder()
                    .time(ts.getTime())
                    .user(UserMapper.userDtoWithoutRelations(ts.getUser()))
                    .task(TaskMapper.taskDtoWithoutRelations(ts.getTask()))
                    .build()));
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody String timeDTO) {
        TimeSpentDTO time = new Gson().fromJson(timeDTO, TimeSpentDTO.class);
        boolean updated = timeSpentService.update(time);

        if (updated)
            return ResponseEntity.ok("updated");
        else
            return ResponseEntity.badRequest().build();
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody String timeDTO) {
        TimeSpentDTO time = new Gson().fromJson(timeDTO, TimeSpentDTO.class);

        //TODO
        //if exists return bad request

        timeSpentService.create(time);
        return ResponseEntity.ok("Time registration created");

    }
}

