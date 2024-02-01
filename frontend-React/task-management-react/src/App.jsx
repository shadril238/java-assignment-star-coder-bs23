import { createContext, useEffect, useState } from "react";
import "./App.css";
import axiosInstance from "./utils/axiosInstance";
import { Route, Routes } from "react-router-dom";
import RegistrationPage from "./pages/registration-page/registration.page";
import LoginPage from "./pages/login-page/login.page";
import TaskPage from "./pages/task-page/task.page";

export const UserContext = createContext();

function App() {
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    axiosInstance.get("/user/profile").then((res) => {
      setUserData(res.data);
      console.log("Current User: ", res.data);
    });
  }, []);

  return (
    <UserContext.Provider value={userData}>
      <Routes>
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="*" element={<h1>404 - Not Found</h1>} />
        <Route path="/" element={<TaskPage />} />
      </Routes>
    </UserContext.Provider>
  );
}

export default App;
