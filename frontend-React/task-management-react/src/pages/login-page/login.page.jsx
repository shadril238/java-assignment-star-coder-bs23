import LoginFormComponent from "../../components/login-form/login.form.component";
import { Typography, Container, Paper } from "@mui/material";
import "./login.page.css";

const LoginPage = () => {
  return (
    <div className="page-container">
      <header className="header">
        <Typography variant="h4">Java Assignment - Task Management</Typography>
      </header>

      <Container component="main" maxWidth="xs" className="main-content">
        <Paper elevation={3} className="form-card">
          <Typography component="h1" variant="h5" className="form-title">
            Log In
          </Typography>
          <LoginFormComponent />
        </Paper>
      </Container>

      <footer className="footer">
        <Typography variant="body1">
          Developed by - Shadril Hassan Shifat
        </Typography>
      </footer>
    </div>
  );
};

export default LoginPage;
