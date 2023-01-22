package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String taskName;
    private String delta;

    public Task(String name) {
        this.taskName = name;
        this.startTime = LocalDateTime.now();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return "Task: " + taskName + "\t" + "Start: " + TimeUtils.getFancyDateTime(startTime) + "\t" + "End: " + TimeUtils.getFancyDateTime(endTime) + "\t" + "Delta: " + delta;
    }

    public String getDelta() {
        return delta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime) && Objects.equals(taskName, task.taskName) && Objects.equals(delta, task.delta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, taskName, delta);
    }

}
