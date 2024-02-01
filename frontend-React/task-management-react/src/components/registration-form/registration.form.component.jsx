import { useState } from "react";
import {
  Box,
  TextField,
  Button,
  Typography,
  Container,
  Paper,
} from "@mui/material";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate } from "react-router-dom";

const RegistrationFormComponent = () => {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    firstname: "",
    lastname: "",
    birthdate: "",
    role: "USER",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axiosInstance.post("/register", formData);
      navigate("/login");
    } catch (error) {
      console.error(
        "Registration error:",
        error.response?.data || error.message
      );
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <Paper
        elevation={3}
        sx={{
          mt: 8,
          p: 4,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Typography component="h1" variant="h5">
          Sign Up
        </Typography>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <TextField
            required
            fullWidth
            id="username"
            label="Username"
            name="username"
            autoComplete="username"
            margin="normal"
            value={formData.username}
            onChange={handleChange}
          />
          <TextField
            required
            fullWidth
            id="email"
            label="Email Address"
            name="email"
            autoComplete="email"
            margin="normal"
            value={formData.email}
            onChange={handleChange}
          />
          <TextField
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="new-password"
            margin="normal"
            value={formData.password}
            onChange={handleChange}
          />
          <TextField
            fullWidth
            id="firstname"
            label="First Name"
            name="firstname"
            autoComplete="given-name"
            margin="normal"
            value={formData.firstname}
            onChange={handleChange}
          />
          <TextField
            fullWidth
            id="lastname"
            label="Last Name"
            name="lastname"
            autoComplete="family-name"
            margin="normal"
            value={formData.lastname}
            onChange={handleChange}
          />
          <TextField
            fullWidth
            id="birthdate"
            label="Birthdate"
            type="date"
            name="birthdate"
            autoComplete="bday"
            margin="normal"
            InputLabelProps={{
              shrink: true,
            }}
            value={formData.birthdate}
            onChange={handleChange}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Sign Up
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default RegistrationFormComponent;
