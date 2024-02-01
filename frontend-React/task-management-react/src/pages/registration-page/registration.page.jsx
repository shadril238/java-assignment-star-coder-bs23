import { Container, Typography, CssBaseline } from "@mui/material";
import "./registration.page.css";
import RegistrationFormComponent from "../../components/registration-form/registration.form.component";

const RegistrationPage = () => {
  const handleSubmit = (event) => {
    event.preventDefault();
    console.log("Form submitted!");
  };

  return (
    <div className="page-container">
      <CssBaseline />

      <header className="header">
        <Typography variant="h4" component="h1">
          Java Assignment - Task Management
        </Typography>
      </header>

      <Container component="main" maxWidth="xs" className="main-content">
        <RegistrationFormComponent />
      </Container>

      <footer className="footer">
        <Typography variant="body2">
          Developed by - Shadril Hassan Shifat
        </Typography>
      </footer>
    </div>
  );
};

export default RegistrationPage;
