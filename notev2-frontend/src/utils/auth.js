export const getTokenPayload = () => {
  const token = localStorage.getItem("token");
  if (!token) return null;
  try {
    const base64 = token.split(".")[1];
    return JSON.parse(atob(base64));
  } catch {
    return null;
  }
};

export const isAdmin = () => {
  const payload = getTokenPayload();
  return payload?.role === "ROLE_ADMIN";
};

export const isLoggedIn = () => !!localStorage.getItem("token");