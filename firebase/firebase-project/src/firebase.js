// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyABb-dWLPlSlvq2aKFF4PxpraQAkxMclIg",
  authDomain: "fir-chat-demo-a1b87.firebaseapp.com",
  projectId: "fir-chat-demo-a1b87",
  storageBucket: "fir-chat-demo-a1b87.appspot.com",
  messagingSenderId: "781615922309",
  appId: "1:781615922309:web:b964f49826640885873dec",
};

const app = initializeApp(firebaseConfig);

export const auth = getAuth(app);
export const db = getFirestore(app);
export default app;
