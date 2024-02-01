import "./task.page.css";
import { Container, Typography } from "@mui/material";
import { useState } from "react";
import TaskTableComponent from "../../components/task-table/task.table.component";

const TaskPage = () => {
  const [tasks, setTasks] = useState([
    { id: 1, name: "Task 1", description: "Description 1" },
    { id: 2, name: "Task 2", description: "Description 2" },
    // Add more tasks
  ]);

  const handleDelete = (id) => {
    // Delete task logic
  };

  const handleUpdate = (task) => {
    // Update task logic
  };

  return (
    <div className="page-container">
      <header className="header">
        <Typography variant="h4">Task Management Dashboard</Typography>
      </header>

      <Container className="main-content">
        <TaskTableComponent
          tasks={tasks}
          onDelete={handleDelete}
          onUpdate={handleUpdate}
        />
      </Container>

      <footer className="footer">
        <Typography variant="body1">
          Developed by - Shadril Hassan Shifat
        </Typography>
      </footer>
    </div>
  );
};

export default TaskPage;
