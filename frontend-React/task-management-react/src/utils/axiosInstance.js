import axios from "axios";

export const axiosInstance = axios.create({
  baseURL: "http://localhost:8090/api",
  timeout: 3000,
});

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export default axiosInstance;
