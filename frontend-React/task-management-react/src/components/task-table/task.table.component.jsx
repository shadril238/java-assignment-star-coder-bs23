// TaskTableComponent.jsx
import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  TextField,
  Box,
} from "@mui/material";
import { Edit as EditIcon, Delete as DeleteIcon } from "@mui/icons-material";

const TaskTableComponent = ({ tasks, onDelete, onUpdate }) => {
  return (
    <TableContainer component={Paper}>
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        p={2}
      >
        <TextField label="Search Tasks" variant="outlined" size="small" />

        <IconButton color="primary" aria-label="add task">
          {/* Icon or Button for adding task */}
        </IconButton>
      </Box>

      <Table aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Task</TableCell>
            <TableCell align="right">Description</TableCell>
            <TableCell align="right">Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {tasks.map((task) => (
            <TableRow key={task.id}>
              <TableCell component="th" scope="row">
                {task.name}
              </TableCell>
              <TableCell align="right">{task.description}</TableCell>
              <TableCell align="right">
                <IconButton aria-label="edit" onClick={() => onUpdate(task)}>
                  <EditIcon />
                </IconButton>
                <IconButton
                  aria-label="delete"
                  onClick={() => onDelete(task.id)}
                >
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default TaskTableComponent;
