import React, { useState, useEffect } from "react";
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Grid,
  IconButton,
} from "@mui/material";
import axiosInstance from "../../utils/axiosInstance";
import TaskTableComponent from "../../components/task-table/task.table.component";
import LogoutIcon from "@mui/icons-material/Logout";
import "./task.page.css";

const TaskPage = () => {
  const [currentUser, setCurrentUser] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [taskForm, setTaskForm] = useState({ title: "", description: "" });
  const [isUpdating, setIsUpdating] = useState(false);
  const [currentTaskId, setCurrentTaskId] = useState(null);

  useEffect(() => {
    axiosInstance
      .get("/user/profile")
      .then((response) => {
        setCurrentUser(response.data);
        fetchTasks(response.data.id);
      })
      .catch((error) => console.error("Failed to fetch user profile", error));
  }, []);

  const fetchTasks = (userId) => {
    axiosInstance
      .get(`/tasks/user/${userId}/sort/createdAt/desc`)
      .then((response) => setTasks(response.data))
      .catch((error) => console.error("Failed to fetch tasks", error));
  };

  const handleAddOrUpdateTask = () => {
    const endpoint = isUpdating ? `/tasks/${currentTaskId}` : `/tasks/`;
    const taskData = { ...taskForm, userId: currentUser.id };

    axiosInstance[isUpdating ? "put" : "post"](endpoint, taskData)
      .then(() => {
        resetForm();
        fetchTasks(currentUser.id);
      })
      .catch((error) => console.error("Failed to add or update task", error));
  };

  const handleEditTask = (task) => {
    setTaskForm({ title: task.title, description: task.description });
    setIsUpdating(true);
    setCurrentTaskId(task.id);
  };

  const handleDeleteTask = (id) => {
    axiosInstance
      .delete(`/tasks/${id}`)
      .then(() => fetchTasks(currentUser.id))
      .catch((error) => console.error("Failed to delete task", error));
  };

  const handleLogout = () => {
    console.log("Logging out...");
    localStorage.clear();
    window.location.href = "/login";
  };

  const resetForm = () => {
    setTaskForm({ title: "", description: "" });
    setIsUpdating(false);
    setCurrentTaskId(null);
  };

  return (
    <Box className="home-container">
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        marginBottom={2}
      >
        <Typography variant="h4" gutterBottom>
          What's the Plan for Today?
        </Typography>
        <IconButton onClick={handleLogout} color="inherit" aria-label="logout">
          <LogoutIcon />
        </IconButton>
      </Box>
      <Box className="add-task-container">
        <Card
          className="add-task-card"
          style={{ width: "100%", marginTop: "15px" }}
        >
          <CardContent>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  variant="outlined"
                  label="Task Title"
                  value={taskForm.title}
                  onChange={(e) =>
                    setTaskForm({ ...taskForm, title: e.target.value })
                  }
                  fullWidth
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  variant="outlined"
                  label="Task Description"
                  value={taskForm.description}
                  onChange={(e) =>
                    setTaskForm({ ...taskForm, description: e.target.value })
                  }
                  fullWidth
                />
              </Grid>
              <Grid item xs={12} style={{ textAlign: "left" }}>
                <Button
                  variant="contained"
                  color={isUpdating ? "secondary" : "primary"}
                  onClick={handleAddOrUpdateTask}
                >
                  {isUpdating ? "Update Task" : "Add Task"}
                </Button>
                {isUpdating && (
                  <Button
                    variant="outlined"
                    onClick={resetForm}
                    style={{ marginLeft: "10px" }}
                  >
                    Cancel
                  </Button>
                )}
              </Grid>
            </Grid>
          </CardContent>
        </Card>
      </Box>
      <TaskTableComponent
        tasks={tasks}
        handleEditTask={handleEditTask}
        handleDeleteTask={handleDeleteTask}
        fetchTasks={() => fetchTasks(currentUser.id)}
      />
    </Box>
  );
};

export default TaskPage;
