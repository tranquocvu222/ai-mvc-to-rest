<!-- src/main/webapp/WEB-INF/views/task.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Task Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .container {
            width: 90%;
            margin: 0 auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input, .form-group textarea {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
        .btn {
            padding: 8px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            margin-right: 5px;
        }
        .btn-edit {
            background-color: #2196F3;
        }
        .btn-delete {
            background-color: #f44336;
        }
        .error {
            color: red;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Task Management</h1>

    <c:if test="${not empty errors}">
        <div class="error">
            <ul>
                <c:forEach items="${errors}" var="error">
                    <li>${error}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <!-- Task Form -->
    <h2>${empty body.id ? 'Add New Task' : 'Edit Task'}</h2>
    <form:form action="${empty body.id ? '/task?create' : '/task?update'}" method="post" modelAttribute="body">
        <form:hidden path="id" />

        <div class="form-group">
            <label for="title">Title:</label>
            <form:input path="title" required="required" />
        </div>

        <div class="form-group">
            <label for="description">Description:</label>
            <form:textarea path="description" rows="3" />
        </div>

        <div class="form-group">
            <label for="dueDate">Due Date:</label>
            <form:input path="dueDate" type="date" />
        </div>

        <div class="form-group">
            <label>
                <form:checkbox path="completed" /> Completed
            </label>
        </div>

        <button type="submit" class="btn">${empty body.id ? 'Add Task' : 'Update Task'}</button>

        <c:if test="${not empty body.id}">
            <a href="/task" class="btn">Cancel</a>
        </c:if>
    </form:form>

    <!-- Task List -->
    <h2>Task List</h2>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Description</th>
            <th>Due Date</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${body.taskList}" var="task">
            <tr>
                <td>${task.id}</td>
                <td>${task.title}</td>
                <td>${task.description}</td>
                <td>${task.dueDate}</td>
                <td>${task.completed ? 'Completed' : 'Pending'}</td>
                <td>
                    <a href="/task?edit&id=${task.id}" class="btn btn-edit">Edit</a>
                    <a href="/task?delete&id=${task.id}" class="btn btn-delete" onclick="return confirm('Are you sure you want to delete this task?')">Delete</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty body.taskList}">
            <tr>
                <td colspan="6">No tasks available</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>