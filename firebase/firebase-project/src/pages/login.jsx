import { signInWithEmailAndPassword } from "firebase/auth";
import { useState } from "react";
import { auth } from "../firebase";
import { useRouter } from "next/router";

export default function Login() {
  const router = useRouter();

  const [error, setError] = useState("");
  const [user, setUser] = useState({
    email: "",
    password: "",
  });

  const handleInput = (e) => {
    setUser((prev) => {
      return {
        ...prev,
        [e.target.name]: e.target.value,
      };
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(user);
    await signInWithEmailAndPassword(auth, user.email, user.password)
      .then((userCredential) => {
        // Signed in
        const user = userCredential.user;
        router.push("/");
      })
      .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorMessage);
        setError("Wrong credentials");
        router.push("/login");
      });
  };

  return (
    <div>
      <h1>Login</h1>
      <p style={{ color: "red" }}>{error}</p>
      <form action="">
        <input
          onChange={handleInput}
          type="email"
          name="email"
          placeholder="Enter your email"
        />
        <input
          onChange={handleInput}
          type="password"
          name="password"
          placeholder="password"
        />
        <input onClick={handleSubmit} type="submit" value="login" />
      </form>
      <p>
        Dont have an account <a href="/register">Register here</a>{" "}
      </p>
    </div>
  );
}
