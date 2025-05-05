package com.example.todo_management.service;

import java.util.List;

import com.example.todo_management.dto.TodoDto;

public interface TodoService {

	TodoDto addTodo(TodoDto todoDto);
	
	TodoDto getTodo(Long id);
	
	List<TodoDto> getAllTodos();
	
	TodoDto updateTodo(TodoDto todoDto, Long id);
	
	void deleteTodo(Long id);
	
	TodoDto completeTodo(Long id);
	
	TodoDto inCompleteTodo(Long id);
}
