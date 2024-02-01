import React from "react";
import { Box, Card, CardContent, Typography, IconButton } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";

const TaskTableComponent = ({ tasks, handleEditTask, handleDeleteTask }) => {
  return (
    <Box className="task-list">
      {tasks.map((task) => (
        <Card key={task.id} className="task-card">
          <CardContent>
            <Typography variant="h5" gutterBottom>
              {task.title}
            </Typography>
            <Typography variant="body2">{task.description}</Typography>
            <Box display="flex" justifyContent="flex-end">
              <IconButton
                aria-label="edit"
                onClick={() => handleEditTask(task)}
              >
                <EditIcon />
              </IconButton>
              <IconButton
                aria-label="delete"
                onClick={() => handleDeleteTask(task.id)}
              >
                <DeleteIcon />
              </IconButton>
            </Box>
          </CardContent>
        </Card>
      ))}
    </Box>
  );
};

export default TaskTableComponent;
