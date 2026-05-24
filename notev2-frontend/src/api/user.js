import API from "./api";

export const getAllUsers  = () => API.get("/user");
export const registerUser = (userData) => API.post("/user", userData);