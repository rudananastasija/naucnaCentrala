package com.example.naucna.model;

public class TaskDto {
	String taskId;
	String name;
	String assignee;
	
	public TaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public TaskDto(String taskId, String name, String assignee) {
		super();
		this.taskId = taskId;
		this.name = name;
		this.assignee = assignee;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

}
