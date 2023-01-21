import { createUserWithEmailAndPassword } from "firebase/auth";
import { useState } from "react";
import { auth } from "../firebase";
import { useRouter } from "next/router";

export default function Register() {
  const [error, setError] = useState("");

  const router = useRouter();
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
    await createUserWithEmailAndPassword(auth, user.email, user.password)
      .then((userCredential) => {
        // Signed in
        const user = userCredential.user;
        router.push("/");
      })
      .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorMessage);
        setError("User already exists");
        router.push("/register");
      });
  };

  return (
    <div>
      <h1>Register</h1>
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
        <input onClick={handleSubmit} type="submit" value="register" />
      </form>
      <p>
        Dont have an account <a href="/login">Login here</a>{" "}
      </p>
    </div>
  );
}
