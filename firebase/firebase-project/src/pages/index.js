import { onAuthStateChanged, signOut } from "firebase/auth";
import { useEffect, useState } from "react";
import { auth, db } from "../firebase";
import { useRouter } from "next/router";
import {
  doc,
  setDoc,
  onSnapshot,
  query,
  collection,
  orderBy,
} from "firebase/firestore";
import uuid from "react-uuid";

export default function Home() {
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null);
  const [messages, setMessages] = useState([]);

  const [message, setMessage] = useState("");

  useEffect(() => {
    onAuthStateChanged(auth, (user) => {
      if (user) {
        const uid = user.uid;
        console.log("uid", uid);
        setUser(user);
        setLoading(false);
      } else {
        // User is signed out
        console.log("user is logged out");
        router.push("/login");
      }
      // const mRef = db.collection("messages").where().orderBy("timestampt");
      const q = query(collection(db, "messages"), orderBy("timestamp"));
      const unsubscribe = onSnapshot(q, (querySnapshot) => {
        const messages = [];

        querySnapshot.forEach((doc) => {
          messages.push(doc.data());
        });

        console.log("messages", messages);
        setMessages(messages);
      });
    });
  }, []);

  const sendMessage = async (e) => {
    e.preventDefault();
    console.log(message);
    await setDoc(doc(db, "messages", uuid()), {
      email: user.email,
      data: message,
      timestamp: new Date(),
    });

    setMessage("");
  };

  const handleLogout = () => {
    signOut(auth)
      .then(() => {
        // Sign-out successful.
        console.log("Signed out successfully");
        router.push("/login");
      })
      .catch((error) => {
        router.push("/login");
      });
  };

  if (loading) {
    return <h1>loading</h1>;
  }

  return (
    <div>
      <h1>hello {user.email}</h1>
      <button onClick={handleLogout}>Logout</button>
      <br />
      <br />
      <br />
      <br />
      <h1>Global Messages:</h1>
      <div>
        {messages.map((message) => {
          return (
            <li key={uuid()}>
              {message.email}: {message.data}
            </li>
          );
        })}

        <br />
        <br />
        <form action="">
          <input
            placeholder="Send a message"
            onChange={(e) => setMessage(e.target.value)}
            type="text"
            name="message"
            id="message"
            value={message}
          />
          <button type="submit" onClick={sendMessage} id="send">
            send
          </button>
        </form>
      </div>
    </div>
  );
}
